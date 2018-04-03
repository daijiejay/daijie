package org.daijie.jdbc;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.sql.DataSource;

import org.daijie.jdbc.interceptor.DefaultRoutingDataSource;
import org.daijie.jdbc.interceptor.SelectDataSourceInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 多数据源bean配置
 * 
 * @author daijie_jay
 * @since 2018年1月2日
 */
@Import(SelectDataSourceInterceptor.class)
@Configuration
public class BaseMultipleDataSourceConfiguration implements EnvironmentAware {
	
	protected Logger logger = LoggerFactory.getLogger(BaseMultipleDataSourceConfiguration.class);

	private MultipleDataSourceProperties multipleDataSourceProperties;

	private Environment environment;

	public BaseMultipleDataSourceConfiguration(
			MultipleDataSourceProperties multipleDataSourceProperties) {
		this.multipleDataSourceProperties = multipleDataSourceProperties;
	}

	@SuppressWarnings("unchecked")
	@Bean("dataSource")
	@ConditionalOnMissingBean
	public AbstractRoutingDataSource routingDataSource(){
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
				DataSource dataSource = DataSourceUtil.getDataSource(dataSourceType.getName(), 
						Binder.get(environment).bind("spring.datasource", Map.class).get());
				targetDataResources.put(DbContextHolder.DATA_SOURCE, dataSource);
				proxy.setDefaultTargetDataSource(dataSource);
			}else{
				if(StringUtils.isEmpty(multipleDataSourceProperties.getDefaultName())){
					throw new JdbcException("没有找到默认的数据源名！");
				}
				String[] names = StringUtils.tokenizeToStringArray(multipleDataSourceProperties.getNames(), 
						ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
				for (String name : names) {
					try {
						Map<String, Object> dataSourceProps = Binder.get(environment).bind("spring.datasource."+name, Map.class).get();
						DataSource dataSource = DataSourceUtil.getDataSource(dataSourceType.getName(), dataSourceProps);
						targetDataResources.put(name, dataSource);
						if(name.equals(multipleDataSourceProperties.getDefaultName())){
							proxy.setDefaultTargetDataSource(dataSource);
						}
					} catch (NoSuchElementException e) {
						logger.error("没有读取到数据名<{}>配置spring.datasource.{}属性！", name, name, e);
						throw e;
					}
				}
			}
		} catch (ReflectiveOperationException e) {
			throw new JdbcException("没有找到数据源类型！", e);
		} catch (NoSuchElementException e) {
			logger.error("没有读取到配置spring.datasource属性，请加上相关配置！", e);
			throw e;
		}
		proxy.setTargetDataSources(targetDataResources);
		return proxy;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}
