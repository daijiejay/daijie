package org.daijie.core.factory;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;

/**
 * 注册Bean工厂
 * @author daijie_jay
 * @date 2017年12月26日
 */
public interface RegisterBeanFactory extends ImportBeanDefinitionRegistrar {

	default public void registerBean(String beanName, BeanDefinitionBuilder builder, BeanDefinitionRegistry registry){
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
		BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
	}
}
