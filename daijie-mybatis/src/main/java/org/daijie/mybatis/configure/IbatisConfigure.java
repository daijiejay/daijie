package org.daijie.mybatis.configure;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 
 * @author daijie
 * @since 2017年6月5日
 * ibatis数据源配置
 * 
 */
public class IbatisConfigure {

	@Value("${jdbc.driverClass}")
	private String driverClassName;

	@Value("${jdbc.url}")
	private String url;

	@Value("${jdbc.username}")
	private String username;

	@Value("${jdbc.password}")
	private String password;

	@Value("${jdbc.maxActive}")
	private int maxActive;

	@Value("${jdbc.maxIdel}")
	private int maxIdel;

	@Value("${jdbc.maxWait}")
	private long maxWait;

	@Value("${mybatis.mapperLocations}")
	private String mapperLocations;
	
	@Value("${mybatis.packages}")
	private String packages;
	
	@Value("${mybatis.configLocation}")
	private String configLocation;

	@SuppressWarnings("deprecation")
	@Bean(name = "dataSource")
    @Primary
	public DataSource dataSource(){
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setMaxActive(maxActive);
		dataSource.setMaxIdle(maxIdel);
		dataSource.setMaxWait(maxWait);
		return dataSource;
	}

	@Bean(name = "sqlSessionFactory")
    @Primary
	public SqlSessionFactory sqlSessionFactoryBean(@Qualifier(value="dataSource") DataSource dataSource) {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		try {
			bean.setDataSource(dataSource);
			bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
			bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource(configLocation));
			return bean.getObject();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager masterTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
