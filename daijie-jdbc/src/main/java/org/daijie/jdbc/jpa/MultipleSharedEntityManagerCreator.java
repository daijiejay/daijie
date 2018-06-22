package org.daijie.jdbc.jpa;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.Nullable;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.EntityManagerProxy;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

/**
 * 重写SharedEntityManagerCreator类
 * @author daijie_jay
 * @since 2018年5月28日
 */
public class MultipleSharedEntityManagerCreator {

	private static final Class<?>[] NO_ENTITY_MANAGER_INTERFACES = new Class<?>[0];

	private static final Set<String> transactionRequiringMethods = new HashSet<>(8);

	private static final Set<String> queryTerminatingMethods = new HashSet<>(8);
	
	static {
		transactionRequiringMethods.add("joinTransaction");
		transactionRequiringMethods.add("flush");
		transactionRequiringMethods.add("persist");
		transactionRequiringMethods.add("merge");
		transactionRequiringMethods.add("remove");
		transactionRequiringMethods.add("refresh");

		queryTerminatingMethods.add("executeUpdate");
		queryTerminatingMethods.add("getSingleResult");
		queryTerminatingMethods.add("getResultList");
		queryTerminatingMethods.add("getResultStream");
	}
	
	public static EntityManager createSharedEntityManager(EntityManagerFactory emf) {
		return createSharedEntityManager(emf, null, true);
	}

	public static EntityManager createSharedEntityManager(EntityManagerFactory emf, @Nullable Map<?, ?> properties) {
		return createSharedEntityManager(emf, properties, true);
	}

	public static EntityManager createSharedEntityManager(
			EntityManagerFactory emf, @Nullable Map<?, ?> properties, boolean synchronizedWithTransaction) {

		Class<?> emIfc = (emf instanceof EntityManagerFactoryInfo ?
				((EntityManagerFactoryInfo) emf).getEntityManagerInterface() : EntityManager.class);
		return createSharedEntityManager(emf, properties, synchronizedWithTransaction,
				(emIfc == null ? NO_ENTITY_MANAGER_INTERFACES : new Class<?>[] {emIfc}));
	}

	public static EntityManager createSharedEntityManager(
			EntityManagerFactory emf, @Nullable Map<?, ?> properties, Class<?>... entityManagerInterfaces) {

		return createSharedEntityManager(emf, properties, true, entityManagerInterfaces);
	}
	
	public static EntityManager createSharedEntityManager(EntityManagerFactory emf, @Nullable Map<?, ?> properties,
			boolean synchronizedWithTransaction, Class<?>... entityManagerInterfaces) {

		ClassLoader cl = null;
		if (emf instanceof EntityManagerFactoryInfo) {
			cl = ((EntityManagerFactoryInfo) emf).getBeanClassLoader();
		}
		Class<?>[] ifcs = new Class<?>[entityManagerInterfaces.length + 1];
		System.arraycopy(entityManagerInterfaces, 0, ifcs, 0, entityManagerInterfaces.length);
		ifcs[entityManagerInterfaces.length] = EntityManagerProxy.class;
		return (EntityManager) Proxy.newProxyInstance(
				(cl != null ? cl : MultipleSharedEntityManagerCreator.class.getClassLoader()),
				ifcs, new MultipleEntityManagerInvocationHandler(emf, properties, synchronizedWithTransaction));
	}

	@SuppressWarnings("serial")
	private static class MultipleEntityManagerInvocationHandler implements InvocationHandler, Serializable {

		private final Log logger = LogFactory.getLog(getClass());

		private final EntityManagerFactory targetFactory;

		@Nullable
		private final Map<?, ?> properties;

		private final boolean synchronizedWithTransaction;

		@Nullable
		private transient volatile ClassLoader proxyClassLoader;
		
		public MultipleEntityManagerInvocationHandler(
				EntityManagerFactory target, @Nullable Map<?, ?> properties, boolean synchronizedWithTransaction) {
			this.targetFactory = target;
			this.properties = properties;
			this.synchronizedWithTransaction = synchronizedWithTransaction;
			initProxyClassLoader();
		}

		private void initProxyClassLoader() {
			if (this.targetFactory instanceof EntityManagerFactoryInfo) {
				this.proxyClassLoader = ((EntityManagerFactoryInfo) this.targetFactory).getBeanClassLoader();
			} else {
				this.proxyClassLoader = this.targetFactory.getClass().getClassLoader();
			}
		}

		@Override
		@Nullable
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (method.getName().equals("equals")) {
				return (proxy == args[0]);
			} else if (method.getName().equals("hashCode")) {
				return hashCode();
			}
			else if (method.getName().equals("toString")) {
				return "Shared EntityManager proxy for target factory [" + this.targetFactory + "]";
			} else if (method.getName().equals("getEntityManagerFactory")) {
				return this.targetFactory;
			} else if (method.getName().equals("getCriteriaBuilder") || method.getName().equals("getMetamodel")) {
				try {
					return EntityManagerFactory.class.getMethod(method.getName()).invoke(this.targetFactory);
				} catch (InvocationTargetException ex) {
					throw ex.getTargetException();
				}
			} else if (method.getName().equals("unwrap")) {
				Class<?> targetClass = (Class<?>) args[0];
				if (targetClass != null && targetClass.isInstance(proxy)) {
					return proxy;
				}
			} else if (method.getName().equals("isOpen")) {
				return true;
			} else if (method.getName().equals("close")) {
				return null;
			} else if (method.getName().equals("getTransaction")) {
				throw new IllegalStateException(
						"Not allowed to create transaction on shared EntityManager - " +
						"use Spring transactions or EJB CMT instead");
			}

			// 这里调一下任意方法为了刷新数据源
			EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager.getResource(this.targetFactory);
			if(emHolder != null && emHolder.getEntityManager() != null){
				emHolder.getEntityManager().isJoinedToTransaction();
			}
			
			EntityManager target = EntityManagerFactoryUtils.doGetTransactionalEntityManager(
					this.targetFactory, this.properties, this.synchronizedWithTransaction);
			if (method.getName().equals("getTargetEntityManager")) {
				if (target == null) {
					throw new IllegalStateException("No transactional EntityManager available");
				}
				return target;
			} else if (method.getName().equals("unwrap")) {
				Class<?> targetClass = (Class<?>) args[0];
				if (targetClass == null) {
					return (target != null ? target : proxy);
				}
				if (target == null) {
					throw new IllegalStateException("No transactional EntityManager available");
				}
			} else if (transactionRequiringMethods.contains(method.getName())) {
				if (target == null || (!TransactionSynchronizationManager.isActualTransactionActive() &&
						!target.getTransaction().isActive())) {
					throw new TransactionRequiredException("No EntityManager with actual transaction available " +
							"for current thread - cannot reliably process '" + method.getName() + "' call");
				}
			}
			boolean isNewEm = false;
			if (target == null) {
				logger.debug("Creating new EntityManager for shared EntityManager invocation");
				target = (!CollectionUtils.isEmpty(this.properties) ?
						this.targetFactory.createEntityManager(this.properties) :
						this.targetFactory.createEntityManager());
				isNewEm = true;
			}
			try {
				Object result = method.invoke(target, args);
				if (result instanceof Query) {
					Query query = (Query) result;
					if (isNewEm) {
						Class<?>[] ifcs = ClassUtils.getAllInterfacesForClass(query.getClass(), this.proxyClassLoader);
						result = Proxy.newProxyInstance(this.proxyClassLoader, ifcs,
								new DeferredQueryInvocationHandler(query, target));
						isNewEm = false;
					} else {
						EntityManagerFactoryUtils.applyTransactionTimeout(query, this.targetFactory);
					}
				}
				return result;
			} catch (InvocationTargetException ex) {
				throw ex.getTargetException();
			} finally {
				if (isNewEm) {
					EntityManagerFactoryUtils.closeEntityManager(target);
				}
			}
		}

		private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
			ois.defaultReadObject();
			initProxyClassLoader();
		}
	}
	
	private static class DeferredQueryInvocationHandler implements InvocationHandler {

		private final Query target;

		@Nullable
		private EntityManager em;

		public DeferredQueryInvocationHandler(Query target, EntityManager em) {
			this.target = target;
			this.em = em;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (method.getName().equals("equals")) {
				return (proxy == args[0]);
			} else if (method.getName().equals("hashCode")) {
				return hashCode();
			} else if (method.getName().equals("unwrap")) {
				Class<?> targetClass = (Class<?>) args[0];
				if (targetClass == null) {
					return this.target;
				} else if (targetClass.isInstance(proxy)) {
					return proxy;
				}
			}
			try {
				Object retVal = method.invoke(this.target, args);
				return (retVal == this.target ? proxy : retVal);
			} catch (InvocationTargetException ex) {
				throw ex.getTargetException();
			} finally {
				if (queryTerminatingMethods.contains(method.getName())) {
					EntityManagerFactoryUtils.closeEntityManager(this.em);
					this.em = null;
				}
			}
		}
	}
}
