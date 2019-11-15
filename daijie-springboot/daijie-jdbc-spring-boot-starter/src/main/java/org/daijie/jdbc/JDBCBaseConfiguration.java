package org.daijie.jdbc;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;

/**
 * JDBC相关基础类自动配置加载
 * @author daijie_jay
 * @since 2019年11月10日
 */
@Configuration
public class JDBCBaseConfiguration {

    @ConditionalOnMissingBean
    public MapperClassPathBeanDefinitionScanner mapperClassPathBeanDefinitionScanner() {
        return new MapperClassPathBeanDefinitionScanner();
    }
}
