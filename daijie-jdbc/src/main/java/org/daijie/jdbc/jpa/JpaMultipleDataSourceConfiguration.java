package org.daijie.jdbc.jpa;

import javax.sql.DataSource;

import org.daijie.core.util.bean.ApplicationContextHolder;
import org.daijie.jdbc.BaseMultipleDataSourceConfiguration;
import org.daijie.jdbc.MultipleDataSourceProperties;
import org.daijie.jdbc.jpa.repository.BaseSearchJpaRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;

/**
 * jpa多数据源相关bean配置
 * MultipleTransactionSynchronizationEntityManager做分布式数据式事务处理
 * @author daijie_jay
 * @since 2018年1月2日
 */
@Configuration
@ConditionalOnClass(BaseSearchJpaRepository.class)
@ConditionalOnProperty(prefix = "spring.data.jpa.repositories", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnMissingBean({ JpaMultipleRepositoryFactoryBean.class,
	JpaMultipleRepositoryConfigExtension.class })
@EnableJpaMultipleRepositories(basePackages = {"org.daijie"}, repositoryFactoryBeanClass = JpaMultipleRepositoryFactoryBean.class)
@Import({BaseMultipleDataSourceConfiguration.class, JpaMultipleRepositoriesAutoConfigureRegistrar.class, ApplicationContextHolder.class})
@AutoConfigureAfter(HibernateJpaAutoConfiguration.class)
@EnableAutoConfiguration(exclude = JpaRepositoriesAutoConfiguration.class)
public class JpaMultipleDataSourceConfiguration {

	private MultipleDataSourceProperties multipleDataSourceProperties;
	
	public JpaMultipleDataSourceConfiguration(
			MultipleDataSourceProperties multipleDataSourceProperties) {
		this.multipleDataSourceProperties = multipleDataSourceProperties;
	}
	
//	@Bean("transactionManager")
//	@ConditionalOnMissingBean
//	public PlatformTransactionManager transactionManagerPrimary(AbstractEntityManagerFactoryBean entityManagerFactory) {  
//		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(entityManagerFactory.getObject());
//		return jpaTransactionManager;
//	}
	
//	@Bean(name = "userTransactionManager", initMethod = "init", destroyMethod = "close")
//	@ConditionalOnMissingBean
//	public UserTransactionManager userTransactionManager() {
//		UserTransactionManager userTransactionManager = new UserTransactionManager();
//		userTransactionManager.setForceShutdown(true);
//		return userTransactionManager;
//	}
//
//	@Bean("userTransactionImp")
//	@ConditionalOnMissingBean
//	public UserTransactionImp userTransactionImp() throws SystemException {
//		UserTransactionImp userTransactionImp = new UserTransactionImp();
//		userTransactionImp.setTransactionTimeout(10000);
//		return userTransactionImp;
//	}
//
//	@Bean("transactionManager")
//	@DependsOn({"userTransactionImp", "userTransactionManager"})
//	@ConditionalOnMissingBean
//	public JtaTransactionManager jtaTransactionManager(UserTransactionManager userTransactionManager,
//			UserTransactionImp userTransactionImp) {
//		JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
//		jtaTransactionManager.setTransactionManager(userTransactionManager);
//		jtaTransactionManager.setUserTransaction(userTransactionImp);
//		return jtaTransactionManager;
//	}
	
	@Bean("EntityManagerFactoryBuilder")
	@ConditionalOnMissingBean
	public JpaMultipleEntityManagerFactoryBuilder entityManagerFactoryBuilder(
			JpaVendorAdapter jpaVendorAdapter,
			ObjectProvider<PersistenceUnitManager> persistenceUnitManager,
			JpaProperties jpaProperties) {
		JpaMultipleEntityManagerFactoryBuilder builder = new JpaMultipleEntityManagerFactoryBuilder(
				jpaVendorAdapter, jpaProperties.getProperties(),
				persistenceUnitManager.getIfAvailable());
		builder.setCallback(null);
		return builder;
	}
	
	@Bean("entityManagerFactory")
	@ConditionalOnMissingBean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, 
			JpaMultipleEntityManagerFactoryBuilder entityManagerFactoryBuilder,
			JpaProperties jpaProperties){
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = entityManagerFactoryBuilder  
				.dataSource(dataSource)
				.properties(jpaProperties.getHibernateProperties(new HibernateSettings()))  
				.packages(multipleDataSourceProperties.getJpaEntityPackages())
				.build();
		return entityManagerFactoryBean;
	}
}
