package org.daijie.jdbc;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.daijie.jdbc.interceptor.DefaultRoutingDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
public class BaseMultipleDataSourceConfiguration implements EnvironmentAware {

	private MultipleDataSourceProperties multipleDataSourceProperties;

	private Environment environment;

	public BaseMultipleDataSourceConfiguration(
			MultipleDataSourceProperties multipleDataSourceProperties) {
		this.multipleDataSourceProperties = multipleDataSourceProperties;
	}

	@Bean("dataSource")
	@ConditionalOnMissingBean
	public AbstractRoutingDataSource routingDataSource(){
		RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(environment, "spring.datasource.");
		DefaultRoutingDataSource proxy = new DefaultRoutingDataSource();
		Map<Object,Object> targetDataResources = new HashMap<Object, Object>();
		Class<? extends DataSource> dataSourceType = null;
		try {
			if(multipleDataSourceProperties.getDataSourceType() != null){
				dataSourceType = multipleDataSourceProperties.getDataSourceType();
			}else{
				dataSourceType = DruidDataSource.class;
			}
			if(multipleDataSourceProperties.getNames() == null){
				DataSource dataSource = DataSourceUtil.getDataSource(dataSourceType.getName(), propertyResolver.getSubProperties(""));
				targetDataResources.put(DbContextHolder.DATA_SOURCE, dataSource);
				proxy.setDefaultTargetDataSource(dataSource);
			}else{
				String[] names = StringUtils.tokenizeToStringArray(multipleDataSourceProperties.getNames(), 
						ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
				for (String name : names) {
					Map<String, Object> dataSourceProps = propertyResolver.getSubProperties(name + ".");
					DataSource dataSource = DataSourceUtil.getDataSource(dataSourceType.getName(), dataSourceProps);
					targetDataResources.put(name, dataSource);
					if(name.equals(multipleDataSourceProperties.getDefaultName())){
						proxy.setDefaultTargetDataSource(dataSource);
					}else{
						throw new JdbcException("Can't find datasource default name!");
					}
				}
			}
		} catch (ReflectiveOperationException e) {
			throw new JdbcException("Can't find datasource type!", e);
		}
		proxy.setTargetDataSources(targetDataResources);
		return proxy;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}
