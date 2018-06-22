package org.daijie.jdbc.jpa;

import static org.springframework.data.jpa.util.BeanDefinitionUtils.getEntityManagerFactoryBeanDefinitions;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.data.jpa.util.BeanDefinitionUtils.EntityManagerFactoryBeanDefinition;

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
