package org.daijie.mybatis.configure;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.aspectj.apache.bcel.util.ClassLoaderRepository;
import org.daijie.mybatis.core.DbContextHolder;
import org.daijie.mybatis.core.ReadWriteSplitRoutingDataSource;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 多数据源配置
 * @author daijie_jay
 * @date 2017年11月20日
 */
@Configuration
@Import(value = DataSourceConfiguration.class)
@AutoConfigureAfter({DataSourceConfiguration.class})
public class MybatisConfiguration extends MybatisAutoConfiguration {

	private static Log logger = LogFactory.getLog(MybatisConfiguration.class);

//    private RelaxedPropertyResolver propertyResolver;

    public MybatisConfiguration(
			MybatisProperties properties,
			ObjectProvider<Interceptor[]> interceptorsProvider,
			ResourceLoader resourceLoader,
			ObjectProvider<DatabaseIdProvider> databaseIdProvider,
			ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
		super(properties, interceptorsProvider, resourceLoader, databaseIdProvider,
				configurationCustomizersProvider);
	}

    @Resource(name = "masterDataSource")
    private DataSource masterDataSource;
    @Resource(name = "slaveDataSource")
    private DataSource slaveDataSource;

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
    	logger.debug("Initializing data sources");
        return super.sqlSessionFactory(roundRobinDataSouceProxy());
    }

//    public void setEnvironment(Environment environment) {
//        this.propertyResolver = new RelaxedPropertyResolver(environment,"mybatis.");
//    }

//    @Bean
//    @ConditionalOnMissingBean
//    public SqlSessionFactory sqlSessionFactory(){
//        try{
//            SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
//            sessionFactoryBean.setDataSource(roundRobinDataSouceProxy());
//            sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
//            	.getResources(propertyResolver.getProperty("mapperLocations")));
//            return sessionFactoryBean.getObject();
//        }catch (Exception e){
//            logger.warn("Could not confiure mybatis session factory");
//            return null;
//        }
//    }

    @SuppressWarnings("unchecked")
	public AbstractRoutingDataSource roundRobinDataSouceProxy(){
        ReadWriteSplitRoutingDataSource proxy = new ReadWriteSplitRoutingDataSource();
        Map<Object,Object> targetDataResources = new ClassLoaderRepository.SoftHashMap();
        targetDataResources.put(DbContextHolder.DbType.MASTER,masterDataSource);
        targetDataResources.put(DbContextHolder.DbType.SLAVE,slaveDataSource);
        proxy.setDefaultTargetDataSource(masterDataSource);
        proxy.setTargetDataSources(targetDataResources);
        return proxy;
    }
}
