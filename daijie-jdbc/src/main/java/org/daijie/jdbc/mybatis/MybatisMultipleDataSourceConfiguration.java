package org.daijie.jdbc.mybatis;

import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.daijie.jdbc.BaseMultipleDataSourceConfiguration;
import org.daijie.jdbc.MultipleDataSourceProperties;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableConfigurationProperties(MultipleDataSourceProperties.class)
@Import(BaseMultipleDataSourceConfiguration.class)
@EnableTransactionManagement
public class MybatisMultipleDataSourceConfiguration extends MybatisAutoConfiguration {

	public MybatisMultipleDataSourceConfiguration(
			MybatisProperties properties,
			ObjectProvider<Interceptor[]> interceptorsProvider,
			ResourceLoader resourceLoader,
			ObjectProvider<DatabaseIdProvider> databaseIdProvider,
			ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
		super(properties, interceptorsProvider, resourceLoader, databaseIdProvider,
				configurationCustomizersProvider);
	}

	@Override
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		return super.sqlSessionFactory(dataSource);
	}
}
