package org.daijie.jdbc.jpa;

import javax.sql.DataSource;

import org.daijie.jdbc.BaseMultipleDataSourceConfiguration;
import org.daijie.jdbc.MultipleDataSourceProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * jpa多数据源相关bean配置
 * @author daijie_jay
 * @since 2018年1月2日
 */
@Configuration
@EnableConfigurationProperties({MultipleDataSourceProperties.class})
@Import(BaseMultipleDataSourceConfiguration.class)
public class JpaMultipleDataSourceConfiguration {

	private MultipleDataSourceProperties multipleDataSourceProperties;
	
	public JpaMultipleDataSourceConfiguration(
			MultipleDataSourceProperties multipleDataSourceProperties) {
		this.multipleDataSourceProperties = multipleDataSourceProperties;
	}
	
	@Bean("transactionManager")
	@ConditionalOnMissingBean
	public PlatformTransactionManager transactionManagerPrimary(AbstractEntityManagerFactoryBean entityManagerFactory) {  
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(entityManagerFactory.getObject());
		return jpaTransactionManager;
	}
	
	@Bean("entityManagerFactory")
	@ConditionalOnMissingBean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, 
			EntityManagerFactoryBuilder entityManagerFactoryBuilder,
			JpaProperties jpaProperties){
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = entityManagerFactoryBuilder  
				.dataSource(dataSource)
				.properties(jpaProperties.getHibernateProperties(dataSource))  
				.packages(multipleDataSourceProperties.getJpaEntityPackages())  
				.build();
		return entityManagerFactoryBean;
	}
}
