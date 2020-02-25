package org.daijie.jdbc;

import org.daijie.core.util.ClassLoaderUtil;
import org.daijie.jdbc.annotation.Mapper;
import org.daijie.jdbc.session.SessionMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

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
     * @return bean实例集
     */
    public Set<Class<?>> doScan() {
        Set<Class<?>> classes = null;
        if (ClassLoaderUtil.isRunTest()) {
            classes = ClassLoaderUtil.getClassesInPackage("");
        } else {
            classes = ClassLoaderUtil.getClassesInMianMethodClass();
        }
        return classes;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        doScan().forEach(cls -> {
            if (isMapperClass(cls)) {
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(cls);
                GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
                definition.getPropertyValues().add("interfaceClass", cls);
                definition.setBeanClass(MapperFactoryBean.class);
                definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
                beanDefinitionRegistry.registerBeanDefinition(cls.getSimpleName(), definition);
            }
        });
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    /**
     * 判断类是不是声明为mapper类
     * @param cls 指定类
     * @return 是不是声明为mapper类
     */
    private boolean isMapperClass(Class<?> cls) {
        Class<?>[] parentClass = cls.getInterfaces();
        if (parentClass.length > 0 && parentClass[0].isAssignableFrom(SessionMapper.class)) {
            return true;
        }
        if (cls.getAnnotation(Mapper.class) != null) {
            return true;
        }
        return false;
    }
}
