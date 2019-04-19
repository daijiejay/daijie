package org.daijie.jdbc.jpa;

import org.daijie.jdbc.interceptor.DefaultRoutingDataSource;
import org.daijie.jdbc.transaction.MultipleTransactionSynchronizationEntityManager;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.datasource.lookup.SingleDataSourceLookup;
import org.springframework.lang.Nullable;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.persistenceunit.SmartPersistenceUnitInfo;
import org.springframework.util.ClassUtils;

import javax.persistence.*;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 重写LocalContainerEntityManagerFactoryBean部分方法
 * 创建多数据源的EntityManagerFactory
 * @author daijie_jay
 * @since 2018年5月25日
 */
public class JpaMultipleEntityManagerFactoryBean extends LocalContainerEntityManagerFactoryBean {

	@Nullable
	private PersistenceUnitManager persistenceUnitManager;

	@Nullable
	private PersistenceUnitInfo persistenceUnitInfo;

	@Nullable
	private Class<? extends EntityManagerFactory> entityManagerFactoryInterface;
	
	private final MultiplePersistenceUnitManager internalPersistenceUnitManager = new MultiplePersistenceUnitManager();

	private String[] dateSourceNames;

	private Map<Object, DataSource> targetDataSources;

	@Override
	public void setEntityManagerFactoryInterface(Class<? extends EntityManagerFactory> emfInterface) {
		super.setEntityManagerFactoryInterface(emfInterface);
		this.entityManagerFactoryInterface = emfInterface;
	}

	@Override
	public void setPersistenceUnitManager(PersistenceUnitManager persistenceUnitManager) {
		super.setPersistenceUnitManager(persistenceUnitManager);
		this.persistenceUnitManager = persistenceUnitManager;
	}

	@Override
	public void setPersistenceUnitName(@Nullable String persistenceUnitName) {
		super.setPersistenceUnitName(persistenceUnitName);
		if (persistenceUnitName != null) {
			this.internalPersistenceUnitManager.setDefaultPersistenceUnitName(persistenceUnitName);
		}
	}

	@Override
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
		this.internalPersistenceUnitManager.setDataSourceLookup(new SingleDataSourceLookup(dataSource));
		this.internalPersistenceUnitManager.setDefaultDataSource(dataSource);
		if (dataSource instanceof DefaultRoutingDataSource) {
			this.targetDataSources = ((DefaultRoutingDataSource) dataSource).getTargetDataSources();
			Set<Object> keySet = this.targetDataSources.keySet();
			this.dateSourceNames = new String[keySet.size()];
			keySet.toArray(this.dateSourceNames);
		}
	}

	@Override
	public void setPackagesToScan(String... packagesToScan) {
		super.setPackagesToScan(packagesToScan);
		this.internalPersistenceUnitManager.setPackagesToScan(packagesToScan);
	}

	@Override
	public void setMappingResources(String... mappingResources) {
		super.setMappingResources(mappingResources);
		this.internalPersistenceUnitManager.setMappingResources(mappingResources);
	}

	@Override
	public void setPersistenceUnitRootLocation(String defaultPersistenceUnitRootLocation) {
		super.setPersistenceUnitRootLocation(defaultPersistenceUnitRootLocation);
		this.internalPersistenceUnitManager.setDefaultPersistenceUnitRootLocation(defaultPersistenceUnitRootLocation);
	}

	@Override
	public void afterPropertiesSet() throws PersistenceException {
		PersistenceUnitManager managerToUse = this.persistenceUnitManager;
		if (this.persistenceUnitManager == null) {
			this.internalPersistenceUnitManager.afterPropertiesSet();
			managerToUse = this.internalPersistenceUnitManager;
		}

		if (managerToUse instanceof MultiplePersistenceUnitManager) {
			this.persistenceUnitInfo = ((MultiplePersistenceUnitManager) managerToUse).getPersistenceUnitInfo();
			JpaVendorAdapter jpaVendorAdapter = getJpaVendorAdapter();
			if (jpaVendorAdapter != null && this.persistenceUnitInfo instanceof SmartPersistenceUnitInfo) {
				String rootPackage = jpaVendorAdapter.getPersistenceProviderRootPackage();
				if (rootPackage != null) {
					((SmartPersistenceUnitInfo) this.persistenceUnitInfo).setPersistenceProviderPackageName(rootPackage);
				}
			}
		}
		super.afterPropertiesSet();
	}

	@Override
	protected EntityManagerFactory createNativeEntityManagerFactory() throws PersistenceException {
		MultipleProxySessionFactory entityManagerFactory = new MultipleProxySessionFactory();
		for (String datesSourceName : dateSourceNames) {
			final PersistenceUnitInfo persistenceUnitInfo = this.internalPersistenceUnitManager.getPersistenceUnitInfo(datesSourceName);
			entityManagerFactory.getEntityManagerFactories().put(datesSourceName, createMultipleEntityManagerFactory(persistenceUnitInfo));
		}
		return entityManagerFactory;
	}

	protected EntityManagerFactory createMultipleEntityManagerFactory(PersistenceUnitInfo persistenceUnitInfo) throws PersistenceException {
		PersistenceProvider provider = getPersistenceProvider();
		if (provider == null) {
			String providerClassName = persistenceUnitInfo.getPersistenceProviderClassName();
			if (providerClassName == null) {
				throw new IllegalArgumentException(
						"No PersistenceProvider specified in EntityManagerFactory configuration, " +
						"and chosen PersistenceUnitInfo does not specify a provider class name either");
			}
			Class<?> providerClass = ClassUtils.resolveClassName(providerClassName, getBeanClassLoader());
			provider = (PersistenceProvider) BeanUtils.instantiateClass(providerClass);
		}
		if (logger.isInfoEnabled()) {
			logger.info("Building JPA container EntityManagerFactory for persistence unit '" +
					this.persistenceUnitInfo.getPersistenceUnitName() + "'");
		}
		EntityManagerFactory emf =
				provider.createContainerEntityManagerFactory(persistenceUnitInfo, getJpaPropertyMap());
		postProcessEntityManagerFactory(emf, persistenceUnitInfo);
		return emf;
	}

	@Override
	protected EntityManagerFactory createEntityManagerFactoryProxy(@Nullable EntityManagerFactory emf) {
		Set<Class<?>> ifcs = new LinkedHashSet<>();
		Class<?> entityManagerFactoryInterface = this.entityManagerFactoryInterface;
		if (entityManagerFactoryInterface != null) {
			ifcs.add(entityManagerFactoryInterface);
		}
		else if (emf != null) {
			ifcs.addAll(ClassUtils.getAllInterfacesForClassAsSet(emf.getClass(), this.getBeanClassLoader()));
		}
		else {
			ifcs.add(EntityManagerFactory.class);
		}
		ifcs.add(EntityManagerFactoryInfo.class);
		try {
			return (EntityManagerFactory) Proxy.newProxyInstance(
					this.getBeanClassLoader(), ifcs.toArray(new Class<?>[ifcs.size()]),
					new MultipleEntityManagerFactoryInvocationHandler(this));
		}
		catch (IllegalArgumentException ex) {
			if (entityManagerFactoryInterface != null) {
				throw new IllegalStateException("EntityManagerFactory interface [" + entityManagerFactoryInterface +
						"] seems to conflict with Spring's EntityManagerFactoryInfo mixin - consider resetting the "+
						"'entityManagerFactoryInterface' property to plain [javax.persistence.EntityManagerFactory]", ex);
			}
			else {
				throw new IllegalStateException("Conflicting EntityManagerFactory interfaces - " +
						"consider specifying the 'jpaVendorAdapter' or 'entityManagerFactoryInterface' property " +
						"to select a specific EntityManagerFactory interface to proceed with", ex);
			}
		}
	}

	/**
	 * 官方定义的父类私有方法，重写invokeProxyMethod方法
	 * @param method 方法名
	 * @param args 参数
	 * @return Object
	 * @throws Throwable 抛出异常
	 */
	public Object invokeProxyMethod2(Method method, @Nullable Object[] args) throws Throwable {
		if (method.getDeclaringClass().isAssignableFrom(EntityManagerFactoryInfo.class)) {
			return method.invoke(this, args);
		}
		else if (method.getName().equals("createEntityManager") && args != null && args.length > 0 &&
				args[0] == SynchronizationType.SYNCHRONIZED) {
			EntityManager rawEntityManager = (args.length > 1 ?
					getNativeEntityManagerFactory().createEntityManager((Map<?, ?>) args[1]) :
					getNativeEntityManagerFactory().createEntityManager());
			return MultipleExtendedEntityManagerCreator.createApplicationManagedEntityManager(rawEntityManager, this, true);
		}
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				Object arg = args[i];
				if (arg instanceof Query && Proxy.isProxyClass(arg.getClass())) {
					try {
						args[i] = ((Query) arg).unwrap(null);
					}
					catch (RuntimeException ex) {
					}
				}
			}
		}
		Object retVal = null;
		EntityManagerFactory entityManagerFactory = getNativeEntityManagerFactory();
		if (method.getName().contains("createEntityManager")) {
			MultipleSessionImpl sessionImpl = new MultipleSessionImpl();
			if (entityManagerFactory instanceof MultipleProxySessionFactory) {
				for (String datesSourceName : dateSourceNames) {
					sessionImpl.getEntityManagers().put(datesSourceName, new MultipleTransactionSynchronizationEntityManager((EntityManager) method
							.invoke(((MultipleProxySessionFactory) entityManagerFactory).getEntityManagerFactories().get(datesSourceName), args),
							this.getJpaDialect()));
					sessionImpl.getEntityManagers().get(datesSourceName);
				}

				retVal = sessionImpl;
			} else {
				retVal = method.invoke(entityManagerFactory, args);
			}
		} else {
			retVal = method.invoke(entityManagerFactory, args);
		}
		if (retVal instanceof EntityManager) {
			EntityManager rawEntityManager = (EntityManager) retVal;
			retVal = MultipleExtendedEntityManagerCreator.createApplicationManagedEntityManager(rawEntityManager, this, false);
		}
		return retVal;
	}
	
	/**
	 * 创建MultipleExtendedEntityManagerCreator的代理类
	 * @author daijie_jay
	 * @since 2018年5月25日
	 */
	@SuppressWarnings("serial")
	private static class MultipleEntityManagerFactoryInvocationHandler implements InvocationHandler, Serializable {

		private JpaMultipleEntityManagerFactoryBean defaultEntityManagerFactoryBean;

		public MultipleEntityManagerFactoryInvocationHandler(AbstractEntityManagerFactoryBean emfb) {
			this.defaultEntityManagerFactoryBean = (JpaMultipleEntityManagerFactoryBean) emfb;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			try {
				if (method.getName().equals("equals")) {
					return (proxy == args[0]);
				}
				else if (method.getName().equals("hashCode")) {
					return System.identityHashCode(proxy);
				}
				else if (method.getName().equals("unwrap")) {
					Class<?> targetClass = (Class<?>) args[0];
					if (targetClass == null) {
						return this.defaultEntityManagerFactoryBean.getNativeEntityManagerFactory();
					}
					else if (targetClass.isInstance(proxy)) {
						return proxy;
					}
				}
				return this.defaultEntityManagerFactoryBean.invokeProxyMethod2(method, args);
			}
			catch (InvocationTargetException ex) {
				throw ex.getTargetException();
			}
		}
	}
}
