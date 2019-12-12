package org.daijie.jdbc;

import org.daijie.core.util.ClassLoaderUtil;
import org.daijie.jdbc.session.SessionMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 动态扫描注册mapper类
 * @author daijie_jay
 * @since 2019年11月10日
 */
public class MapperClassPathBeanDefinitionScanner implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {

    private ApplicationContext context;

    /**
     * Collections.singletonList(new ContextConfigurationAttributes(testClass))
     * 扫描启动类包路径下的Mapper类，并注册到spring容器
     * @return
     */
    public Set<BeanDefinition> doScan() {
        Set<BeanDefinition> beanDefinitions = new LinkedHashSet();
        Set<Class<?>> classes = null;
        if (ClassLoaderUtil.isRunTest()) {
            classes = ClassLoaderUtil.getClassesInPackage("");
        } else {
            classes = ClassLoaderUtil.getClassesInMianMethodClass();
        }
        classes.forEach(cls -> {
            Class<?>[] parentClass = cls.getInterfaces();
            if (parentClass.length > 0 && parentClass[0].isAssignableFrom(SessionMapper.class)) {
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(cls);
                GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
                definition.getPropertyValues().add("interfaceClass", cls);
                definition.setBeanClass(MapperFactoryBean.class);
                definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
                beanDefinitions.add(definition);
            }
        });
        return beanDefinitions;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        doScan().forEach(beanDefinition -> beanDefinitionRegistry.registerBeanDefinition(beanDefinition.getBeanClassName(), beanDefinition));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
