package org.daijie.jdbc;

import org.daijie.common.bean.RegisterBeanFactory;
import org.daijie.core.util.ClassLoaderUtil;
import org.daijie.jdbc.session.SessionMapper;
import org.daijie.jdbc.session.SessionMapperManage;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.type.AnnotationMetadata;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 动态扫描注册mapper类
 * @author daijie_jay
 * @since 2019年11月10日
 */
public class MapperClassPathBeanDefinitionScanner implements RegisterBeanFactory {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        doScan().forEach(beanDefinitionHolder -> BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, beanDefinitionRegistry));
    }

    public Set<BeanDefinitionHolder> doScan() {
        Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet();
        Set<Class<?>> classes = ClassLoaderUtil.getClassesInPackage("");
        classes.forEach(cls -> {
            if (cls.isAssignableFrom(SessionMapper.class)) {
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(SessionMapperManage.createSessionMapper(cls).getClass());
                beanDefinitions.add(new BeanDefinitionHolder(builder.getBeanDefinition(), cls.getSimpleName()));
            }
        });
        return beanDefinitions;
    }
}
