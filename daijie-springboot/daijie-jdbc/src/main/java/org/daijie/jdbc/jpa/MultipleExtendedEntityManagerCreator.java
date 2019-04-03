package org.daijie.jdbc.jpa;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.daijie.jdbc.DbContextHolder;
import org.daijie.jdbc.transaction.MultipleTransactionSynchronizationEntityManager;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.lang.Nullable;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.orm.jpa.EntityManagerProxy;
import org.springframework.orm.jpa.ExtendedEntityManagerCreator;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

/**
 * 多数据源EntityManager构造器
 * 创建单例线程的EntityManager
 * @author daijie_jay
 * @since 2018年5月25日
 */
public class MultipleExtendedEntityManagerCreator {

	/**
	 * 创建EntityManager代理
	 * @param rawEntityManager 新的EntityManager
	 * @param emfInfo 获得JpaDialect的EntityManagerFactoryInfo和PersistenceUnitInfo
	 * @return EntityManager
	 */
	public static EntityManager createApplicationManagedEntityManager(
			EntityManager rawEntityManager, EntityManagerFactoryInfo emfInfo) {

		return createProxy(rawEntityManager, emfInfo, false, false);
	}


	/**
	 * 创建EntityManager代理
	 * @param rawEntityManager 新的EntityManager
	 * @param emfInfo 获得JpaDialect的EntityManagerFactoryInfo和PersistenceUnitInfo
	 * @param synchronizedWithTransaction 是否自动加入正在进行的事务
	 * @return EntityManager
	 */
	public static EntityManager createApplicationManagedEntityManager(
			EntityManager rawEntityManager, EntityManagerFactoryInfo emfInfo, boolean synchronizedWithTransaction) {
		return createProxy(rawEntityManager, emfInfo, false, synchronizedWithTransaction);
	}

	/**
	 * 创建EntityManager代理
	 * @param rawEntityManager 新的EntityManager
	 * @param emfInfo 获得JpaDialect的EntityManagerFactoryInfo和PersistenceUnitInfo
	 * @return EntityManager
	 */
	public static EntityManager createContainerManagedEntityManager(
			EntityManager rawEntityManager, EntityManagerFactoryInfo emfInfo) {
		return createProxy(rawEntityManager, emfInfo, true, true);
	}

	/**
	 * 创建EntityManager代理
	 * @param emf EntityManagerFactory创建EntityManager。如果这实现了EntityManagerFactoryInfo接口，对应的是相应的将对JpaDialect和PersistenceUnitInfo进行相应的检测。
	 * @return EntityManager
	 */
	public static EntityManager createContainerManagedEntityManager(EntityManagerFactory emf) {
		return createContainerManagedEntityManager(emf, null, true);
	}

	/**
	 * 创建EntityManager代理
	 * @param emf EntityManagerFactory创建EntityManager。如果这实现了EntityManagerFactoryInfo接口，对应的是相应的将对JpaDialect和PersistenceUnitInfo进行相应的检测。
	 * @param properties createEntityManager的属性
	 * @return EntityManager
	 */
	public static EntityManager createContainerManagedEntityManager(EntityManagerFactory emf, @Nullable Map<?, ?> properties) {
		return createContainerManagedEntityManager(emf, properties, true);
	}

	/**
	 * 创建EntityManager代理
	 * @param emf EntityManagerFactory创建EntityManager。如果这实现了EntityManagerFactoryInfo接口，对应的是相应的将对JpaDialect和PersistenceUnitInfo进行相应的检测。
	 * @param properties createEntityManager的属性
	 * @param synchronizedWithTransaction 是否自动加入正在进行的事务
	 * @return EntityManager
	 */
	public static EntityManager createContainerManagedEntityManager(
			EntityManagerFactory emf, @Nullable Map<?, ?> properties, boolean synchronizedWithTransaction) {
		Assert.notNull(emf, "EntityManagerFactory must not be null");
		if (emf instanceof EntityManagerFactoryInfo) {
			EntityManagerFactoryInfo emfInfo = (EntityManagerFactoryInfo) emf;
			EntityManagerFactory nativeEmf = emfInfo.getNativeEntityManagerFactory();
			EntityManager rawEntityManager = (!CollectionUtils.isEmpty(properties) ?
					nativeEmf.createEntityManager(properties) : nativeEmf.createEntityManager());
			return createProxy(rawEntityManager, emfInfo, true, synchronizedWithTransaction);
		}
		else {
			EntityManager rawEntityManager = (!CollectionUtils.isEmpty(properties) ?
					emf.createEntityManager(properties) : emf.createEntityManager());
			return createProxy(rawEntityManager, null, null, null, null, true, synchronizedWithTransaction);
		}
	}

	/**
	 * 创建EntityManager代理
	 * @param rawEntityManager 新EntityManager
	 * @param emfInfo 获得JpaDialect的EntityManagerFactoryInfo和PersistenceUnitInfo
	 * @param containerManaged 是否遵循容器管理的EntityManager或者应用程序管理的EntityManager
	 * @param synchronizedWithTransaction 是否自动加入正在进行的事务
	 * @return EntityManager
	 */
	private static EntityManager createProxy(EntityManager rawEntityManager,
			EntityManagerFactoryInfo emfInfo, boolean containerManaged, boolean synchronizedWithTransaction) {

		Assert.notNull(emfInfo, "EntityManagerFactoryInfo must not be null");
		JpaDialect jpaDialect = emfInfo.getJpaDialect();
		PersistenceUnitInfo pui = emfInfo.getPersistenceUnitInfo();
		Boolean jta = (pui != null ? pui.getTransactionType() == PersistenceUnitTransactionType.JTA : null);
		return createProxy(rawEntityManager, emfInfo.getEntityManagerInterface(),
				emfInfo.getBeanClassLoader(), jpaDialect, jta, containerManaged, synchronizedWithTransaction);
	}

	/**
	 * 创建EntityManager代理
	 * @param rawEm 新EntityManager
	 * @param emIfc 用于默认检测repository所有接口
	 * @param cl 用于代理创建的类加载器
	 * @param exceptionTranslator 使用的异常解析器
	 * @param jta 是否要创建一个jta感知的EntityManager
	 * @param containerManaged 是否遵循容器管理的EntityManager或者应用程序管理的EntityManager
	 * @param synchronizedWithTransaction 是否自动加入正在进行的事务
	 * @return EntityManager 
	 */
	private static EntityManager createProxy(
			EntityManager rawEm, @Nullable Class<? extends EntityManager> emIfc, @Nullable ClassLoader cl,
			@Nullable PersistenceExceptionTranslator exceptionTranslator, @Nullable Boolean jta,
			boolean containerManaged, boolean synchronizedWithTransaction) {

		Assert.notNull(rawEm, "EntityManager must not be null");
		Set<Class<?>> ifcs = new LinkedHashSet<>();
		if (emIfc != null) {
			ifcs.add(emIfc);
		}
		else {
			ifcs.addAll(ClassUtils.getAllInterfacesForClassAsSet(rawEm.getClass(), cl));
		}
		ifcs.add(EntityManagerProxy.class);
		return (EntityManager) Proxy.newProxyInstance(
				(cl != null ? cl : ExtendedEntityManagerCreator.class.getClassLoader()),
				ifcs.toArray(new Class<?>[ifcs.size()]),
				new MultipleExtendedEntityManagerInvocationHandler(
						rawEm, exceptionTranslator, jta, containerManaged, synchronizedWithTransaction));
	}


	/**
	 * 多数据源模型管理工具代理类
	 * 每次repository接口的访问将进行代理
	 * @author daijie_jay
	 * @since 2018年5月25日
	 */
	@SuppressWarnings("serial")
	private static class MultipleExtendedEntityManagerInvocationHandler implements InvocationHandler, Serializable {

		private static final Log logger = LogFactory.getLog(MultipleExtendedEntityManagerInvocationHandler.class);

		private final EntityManager target;

		@Nullable
		private final PersistenceExceptionTranslator exceptionTranslator;

		private final boolean jta;

		private final boolean containerManaged;

		private final boolean synchronizedWithTransaction;
		
		private EntityManager defaultEntityManager;

		private MultipleExtendedEntityManagerInvocationHandler(EntityManager target,
				@Nullable PersistenceExceptionTranslator exceptionTranslator, @Nullable Boolean jta,
				boolean containerManaged, boolean synchronizedWithTransaction) {

			this.target = target;
			this.defaultEntityManager = target;
			this.exceptionTranslator = exceptionTranslator;
			this.jta = (jta != null ? jta : isJtaEntityManager());
			this.containerManaged = containerManaged;
			this.synchronizedWithTransaction = synchronizedWithTransaction;
		}

		/**
		 * 是否jta事务管理
		 * @return boolean
		 */
		private boolean isJtaEntityManager() {
			try {
				this.defaultEntityManager.getTransaction();
				return false;
			}
			catch (IllegalStateException ex) {
				logger.debug("Cannot access EntityTransaction handle - assuming we're in a JTA environment");
				return true;
			}
		}

		@Override
		@Nullable
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			// 切换数据源
			MultipleTransactionSynchronizationEntityManager synchronizationEntityManager = ((MultipleSessionImpl) target).getEntityManagers().get(DbContextHolder.getDataSourceName());
			if (this.target instanceof MultipleSessionImpl && synchronizationEntityManager != null) {
				this.defaultEntityManager = synchronizationEntityManager.getEntityManager();
				synchronizationEntityManager.begin();
			}
			if (defaultEntityManager == null) {
				defaultEntityManager = target;
			}

			if (method.getName().equals("equals")) {
				return (proxy == args[0]);
			} else if (method.getName().equals("hashCode")) {
				return hashCode();
			} else if (method.getName().equals("getTargetEntityManager")) {
				return this.defaultEntityManager;
			} else if (method.getName().equals("unwrap")) {
				Class<?> targetClass = (Class<?>) args[0];
				if (targetClass == null) {
					return this.defaultEntityManager;
				}
				else if (targetClass.isInstance(proxy)) {
					return proxy;
				}
			}
			else if (method.getName().equals("isOpen")) {
				if (this.containerManaged) {
					return true;
				}
			} else if (method.getName().equals("close")) {
				if (this.containerManaged) {
					throw new IllegalStateException("Invalid usage: Cannot close a container-managed EntityManager");
				}
				MultipleTransactionSynchronizationEntityManager synch = (MultipleTransactionSynchronizationEntityManager)
						TransactionSynchronizationManager.getResource(this.defaultEntityManager);
				if (synch != null) {
					synch.closeOnCompletion = true;
					return null;
				}
			} else if (method.getName().equals("getTransaction")) {
				if (this.synchronizedWithTransaction) {
					throw new IllegalStateException(
							"Cannot obtain local EntityTransaction from a transaction-synchronized EntityManager");
				}
			}
			else if (method.getName().equals("joinTransaction")) {
				if (synchronizationEntityManager != null) {
					synchronizationEntityManager.begin();
				}
				return null;
			} else if (method.getName().equals("isJoinedToTransaction")) {
				if (!this.jta) {
					return TransactionSynchronizationManager.hasResource(this.defaultEntityManager);
				}
			}
			if (this.synchronizedWithTransaction && method.getDeclaringClass().isInterface()) {
				if (synchronizationEntityManager != null) {
					synchronizationEntityManager.begin();
				}
			}
			try {
				return method.invoke(this.defaultEntityManager, args);
			} catch (InvocationTargetException ex) {
				throw ex.getTargetException();
			}
		}
	}

}
