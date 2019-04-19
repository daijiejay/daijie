package org.daijie.jdbc.jpa;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.data.jpa.util.BeanDefinitionUtils.EntityManagerFactoryBeanDefinition;

import static org.springframework.data.jpa.util.BeanDefinitionUtils.getEntityManagerFactoryBeanDefinitions;

/**
 * 重写EntityManagerBeanDefinitionRegistrarPostProcessor类
 * @author daijie_jay
 * @since 2018年5月28日
 */
public class MultipleEntityManagerBeanDefinitionRegistrarPostProcessor implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		for (EntityManagerFactoryBeanDefinition definition : getEntityManagerFactoryBeanDefinitions(beanFactory)) {
			if (!(definition.getBeanFactory() instanceof BeanDefinitionRegistry)) {
				continue;
			}
			BeanDefinitionBuilder builder = BeanDefinitionBuilder
					.rootBeanDefinition("org.daijie.jdbc.jpa.MultipleSharedEntityManagerCreator");
			builder.setFactoryMethod("createSharedEntityManager");
			builder.addConstructorArgReference(definition.getBeanName());

			AbstractBeanDefinition emBeanDefinition = builder.getRawBeanDefinition();
			emBeanDefinition.addQualifier(new AutowireCandidateQualifier(Qualifier.class, definition.getBeanName()));
			emBeanDefinition.setScope(definition.getBeanDefinition().getScope());
			emBeanDefinition.setSource(definition.getBeanDefinition().getSource());
			BeanDefinitionReaderUtils.registerWithGeneratedName(emBeanDefinition,
					(BeanDefinitionRegistry) definition.getBeanFactory());
		}
	}

}
