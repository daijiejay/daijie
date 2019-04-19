package org.daijie.jdbc.jpa;

import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.net.URL;
import java.util.*;

/**
 * 重写EntityManagerFactoryBuilder
 * 需要构建自己的EntityManagerFactory
 * @author daijie_jay
 * @since 2018年5月25日
 */
public class JpaMultipleEntityManagerFactoryBuilder {

	private final JpaVendorAdapter jpaVendorAdapter;

	private final PersistenceUnitManager persistenceUnitManager;

	private final Map<String, Object> jpaProperties;

	private final URL persistenceUnitRootLocation;
	
	public JpaMultipleEntityManagerFactoryBuilder(JpaVendorAdapter jpaVendorAdapter, Map<String, ?> jpaProperties,
			PersistenceUnitManager persistenceUnitManager) {
		this(jpaVendorAdapter, jpaProperties, persistenceUnitManager, null);
	}

	public JpaMultipleEntityManagerFactoryBuilder(JpaVendorAdapter jpaVendorAdapter,
			Map<String, ?> jpaProperties, PersistenceUnitManager persistenceUnitManager,
			URL persistenceUnitRootLocation) {
		this.jpaVendorAdapter = jpaVendorAdapter;
		this.persistenceUnitManager = persistenceUnitManager;
		this.jpaProperties = new LinkedHashMap<>(jpaProperties);
		this.persistenceUnitRootLocation = persistenceUnitRootLocation;
	}

	public Builder dataSource(DataSource dataSource) {
		return new Builder(dataSource);
	}

	/**
	 * 一个本地集装箱entitymanagerfactorybean的流畅构建器
	 * @author daijie_jay
	 * @since 2018年5月25日
	 */
	public final class Builder {

		private DataSource dataSource;

		private String[] packagesToScan;

		private String persistenceUnit;

		private Map<String, Object> properties = new HashMap<>();

		private String[] mappingResources;

		private boolean jta;

		private Builder(DataSource dataSource) {
			this.dataSource = dataSource;
		}

		/**
		 * 用于扫描@entity注释的包的名称。
		 * @param packagesToScan 包扫描
		 * @return Builder
		 */
		public Builder packages(String... packagesToScan) {
			this.packagesToScan = packagesToScan;
			return this;
		}

		/**
		 * 这些类的包下扫描为@entity注释的类
		 * @param basePackageClasses 要使用的类
		 * @return Builder
		 */
		public Builder packages(Class<?>... basePackageClasses) {
			Set<String> packages = new HashSet<>();
			for (Class<?> type : basePackageClasses) {
				packages.add(ClassUtils.getPackageName(type));
			}
			this.packagesToScan = StringUtils.toStringArray(packages);
			return this;
		}

		public Builder persistenceUnit(String persistenceUnit) {
			this.persistenceUnit = persistenceUnit;
			return this;
		}

		public Builder properties(Map<String, ?> properties) {
			this.properties.putAll(properties);
			return this;
		}

		public Builder mappingResources(String... mappingResources) {
			this.mappingResources = mappingResources;
			return this;
		}

		public Builder jta(boolean jta) {
			this.jta = jta;
			return this;
		}

		public LocalContainerEntityManagerFactoryBean build() {
			JpaMultipleEntityManagerFactoryBean entityManagerFactoryBean = new JpaMultipleEntityManagerFactoryBean();
			if (persistenceUnitManager != null) {
				entityManagerFactoryBean.setPersistenceUnitManager(
						persistenceUnitManager);
			}
			if (this.persistenceUnit != null) {
				entityManagerFactoryBean.setPersistenceUnitName(this.persistenceUnit);
			}
			entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
			if (this.jta) {
				entityManagerFactoryBean.setJtaDataSource(this.dataSource);
			}
			else {
				entityManagerFactoryBean.setDataSource(this.dataSource);
			}
			entityManagerFactoryBean.setPackagesToScan(this.packagesToScan);
			entityManagerFactoryBean.getJpaPropertyMap()
					.putAll(jpaProperties);
			entityManagerFactoryBean.getJpaPropertyMap().putAll(this.properties);
			if (!ObjectUtils.isEmpty(this.mappingResources)) {
				entityManagerFactoryBean.setMappingResources(this.mappingResources);
			}
			URL rootLocation = persistenceUnitRootLocation;
			if (rootLocation != null) {
				entityManagerFactoryBean
						.setPersistenceUnitRootLocation(rootLocation.toString());
			}
			return entityManagerFactoryBean;
		}
	}
}
