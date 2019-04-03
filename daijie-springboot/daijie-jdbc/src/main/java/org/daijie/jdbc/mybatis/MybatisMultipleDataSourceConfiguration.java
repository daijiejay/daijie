package org.daijie.jdbc.mybatis;

import java.util.List;

import javax.sql.DataSource;
import javax.transaction.SystemException;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.daijie.core.util.bean.RegisterBeanHolder;
import org.daijie.jdbc.BaseMultipleDataSourceConfiguration;
import org.daijie.jdbc.MultipleDataSourceProperties;
import org.daijie.jdbc.interceptor.DefaultRoutingDataSource;
import org.daijie.jdbc.mybatis.transaction.MybatisMultipleTransactionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;

/**
 * mybatis多数据源相关bean配置
 * 单数据源时使用DataSourceTransactionManager事务管理
 * 多数据源时使用JtaTransactionManager事务管理
 * @author daijie_jay
 * @since 2018年1月2日
 */
@Configuration
@Import({RegisterBeanHolder.class, BaseMultipleDataSourceConfiguration.class})
public class MybatisMultipleDataSourceConfiguration extends MybatisAutoConfiguration {

	private final MybatisProperties properties;

	private final Interceptor[] interceptors;

	private final ResourceLoader resourceLoader;

	private final DatabaseIdProvider databaseIdProvider;

	private final List<ConfigurationCustomizer> configurationCustomizers;

	public MybatisMultipleDataSourceConfiguration(
			MybatisProperties properties,
			ObjectProvider<Interceptor[]> interceptorsProvider,
			ResourceLoader resourceLoader,
			ObjectProvider<DatabaseIdProvider> databaseIdProvider,
			ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
		super(properties, interceptorsProvider, resourceLoader, databaseIdProvider,
				configurationCustomizersProvider);
		this.properties = properties;
		this.interceptors = interceptorsProvider.getIfAvailable();;
		this.resourceLoader = resourceLoader;
		this.databaseIdProvider = databaseIdProvider.getIfAvailable();
		this.configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
	}

	@Bean
	@ConditionalOnMissingBean
	@Override
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);
		factory.setVfs(SpringBootVFS.class);
		factory.setTransactionFactory(new MybatisMultipleTransactionFactory());
		if (StringUtils.hasText(this.properties.getConfigLocation())) {
			factory.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
		}
		org.apache.ibatis.session.Configuration configuration = this.properties.getConfiguration();
		if (configuration == null && !StringUtils.hasText(this.properties.getConfigLocation())) {
			configuration = new org.apache.ibatis.session.Configuration();
		}
		if (configuration != null && !CollectionUtils.isEmpty(this.configurationCustomizers)) {
			for (ConfigurationCustomizer customizer : this.configurationCustomizers) {
				customizer.customize(configuration);
			}
		}
		factory.setConfiguration(configuration);
		if (this.properties.getConfigurationProperties() != null) {
			factory.setConfigurationProperties(this.properties.getConfigurationProperties());
		}
		if (!ObjectUtils.isEmpty(this.interceptors)) {
			factory.setPlugins(this.interceptors);
		}
		if (this.databaseIdProvider != null) {
			factory.setDatabaseIdProvider(this.databaseIdProvider);
		}
		if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
			factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
		}
		if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
			factory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
		}
		if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
			factory.setMapperLocations(this.properties.resolveMapperLocations());
		}
		return factory.getObject();
	}

	@Bean
	@ConditionalOnMissingBean
	@Override
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		ExecutorType executorType = this.properties.getExecutorType();
		if (executorType != null) {
			return new MybatisMultipleSqlSessionTemplate(sqlSessionFactory, executorType);
		} else {
			return new MybatisMultipleSqlSessionTemplate(sqlSessionFactory);
		}
	}

	@Bean
	@Conditional(IsSignle.class)
	@ConditionalOnMissingBean
	public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "userTransactionManager", initMethod = "init", destroyMethod = "close")
	@Conditional(IsMultiple.class)
	@ConditionalOnMissingBean
	public UserTransactionManager userTransactionManager() {
		UserTransactionManager userTransactionManager = new UserTransactionManager();
		userTransactionManager.setForceShutdown(true);
		return userTransactionManager;
	}

	@Bean("userTransactionImp")
	@Conditional(IsMultiple.class)
	@ConditionalOnMissingBean
	public UserTransactionImp userTransactionImp(MultipleDataSourceProperties multipleDataSourceProperties) throws SystemException {
		UserTransactionImp userTransactionImp = new UserTransactionImp();
		userTransactionImp.setTransactionTimeout(multipleDataSourceProperties.getTransactionTimeout());
		return userTransactionImp;
	}

	@Bean("jtaTransactionManager")
	@DependsOn({"userTransactionImp", "userTransactionManager"})
	@Conditional(IsMultiple.class)
	@ConditionalOnMissingBean
	public JtaTransactionManager jtaTransactionManager(UserTransactionManager userTransactionManager,
			UserTransactionImp userTransactionImp) {
		JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
		jtaTransactionManager.setTransactionManager(userTransactionManager);
		jtaTransactionManager.setUserTransaction(userTransactionImp);
		return jtaTransactionManager;
	}
	
	private static class IsMultiple implements Condition {

		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			DefaultRoutingDataSource dataSouce = context.getBeanFactory().getBean("dataSource", DefaultRoutingDataSource.class);
			return dataSouce.getTargetDataSources().size() > 1;
		}
	}
	
	private static class IsSignle implements Condition {
		
		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			DefaultRoutingDataSource dataSouce = context.getBeanFactory().getBean("dataSource", DefaultRoutingDataSource.class);
			return dataSouce.getTargetDataSources().size() == 1;
		}
	}
}
