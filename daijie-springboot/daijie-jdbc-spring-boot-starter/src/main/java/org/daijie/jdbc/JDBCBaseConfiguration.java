package org.daijie.jdbc;

import org.daijie.jdbc.datasource.DataSourceManage;
import org.daijie.jdbc.datasource.SimpleDataSource;
import org.daijie.jdbc.transaction.DataSourceTransaction;
import org.daijie.jdbc.transaction.DataSourceTransactionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.Collection;

/**
 * JDBC相关基础类自动配置加载
 * @author daijie_jay
 * @since 2019年11月10日
 */
@Import(MapperClassPathBeanDefinitionScanner.class)
@Configuration
public class JDBCBaseConfiguration {

    @Bean("dataSourceTransaction")
    public DataSourceTransaction dataSourceTransaction(@Qualifier("dataSource") DataSource dataSource) {
        DataSourceManage.setDataSource(new SimpleDataSource(dataSource));
        return new DataSourceTransaction(DataSourceManage.getDataSource());
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    @Configuration
    public static class ProxyTransactionManagementConfiguration {

        protected PlatformTransactionManager txManager;

        @Autowired(required = false)
        void setConfigurers(Collection<TransactionManagementConfigurer> configurers) {
            if (CollectionUtils.isEmpty(configurers)) {
                return;
            }
            if (configurers.size() > 1) {
                throw new IllegalStateException("Only one TransactionManagementConfigurer may exist");
            }
            TransactionManagementConfigurer configurer = configurers.iterator().next();
            this.txManager = configurer.annotationDrivenTransactionManager();
        }

        @Bean
        @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
        public TransactionInterceptor dataSourceTransactionInterceptor(TransactionAttributeSource transactionAttributeSource) {
            TransactionInterceptor interceptor = new DataSourceTransactionInterceptor();
            interceptor.setTransactionAttributeSource(transactionAttributeSource);
            if (this.txManager != null) {
                interceptor.setTransactionManager(this.txManager);
            }
            return interceptor;
        }

        @Bean
        @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
        public BeanFactoryTransactionAttributeSourceAdvisor transactionAdvisor(TransactionAttributeSource transactionAttributeSource) {
            BeanFactoryTransactionAttributeSourceAdvisor advisor = new BeanFactoryTransactionAttributeSourceAdvisor();
            advisor.setTransactionAttributeSource(transactionAttributeSource);
            advisor.setAdvice(dataSourceTransactionInterceptor(transactionAttributeSource));
            advisor.setOrder(Ordered.LOWEST_PRECEDENCE);
            return advisor;
        }
    }
}
