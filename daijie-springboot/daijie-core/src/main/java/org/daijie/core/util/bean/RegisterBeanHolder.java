package org.daijie.core.util.bean;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class RegisterBeanHolder implements ImportBeanDefinitionRegistrar {
	
	private static BeanDefinitionRegistry registry;
	
	public static BeanDefinitionRegistry getRegistry(){
		return registry;
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		RegisterBeanHolder.registry = registry;
	}
	
	public static void registryBean(String beanName, BeanDefinitionBuilder builder){
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
		BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
	}

	public static BeanDefinition getBeanDefinition(String beanName) {
		return registry.getBeanDefinition(beanName);
	}

}
