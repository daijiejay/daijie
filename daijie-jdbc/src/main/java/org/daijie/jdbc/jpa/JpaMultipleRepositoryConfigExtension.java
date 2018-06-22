package org.daijie.jdbc.jpa;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.daijie.jdbc.jpa.repository.BaseSearchJpaRepository;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.data.jpa.repository.support.DefaultJpaContext;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;
import org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport;
import org.springframework.data.repository.config.RepositoryConfigurationSource;
import org.springframework.data.repository.config.XmlRepositoryConfigurationSource;
import org.springframework.lang.Nullable;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.util.StringUtils;

/**
 * 重写JpaRepositoryConfigExtension类
 * @author daijie_jay
 * @since 2018年5月28日
 */
public class JpaMultipleRepositoryConfigExtension extends RepositoryConfigurationExtensionSupport {
	
	private static final Class<?> PAB_POST_PROCESSOR = PersistenceAnnotationBeanPostProcessor.class;
	public static final String JPA_MAPPING_CONTEXT_BEAN_NAME = "jpaMappingContext";
	public static final String JPA_CONTEXT_BEAN_NAME = "jpaContext";
	public static final String EM_BEAN_DEFINITION_REGISTRAR_POST_PROCESSOR_BEAN_NAME = "emBeanDefinitionRegistrarPostProcessor";
	private static final String DEFAULT_TRANSACTION_MANAGER_BEAN_NAME = "transactionManager";
	private static final String ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE = "enableDefaultTransactions";

	@Override
	public String getModuleName() {
		return "JPA";
	}

	@Override
	public String getRepositoryFactoryBeanClassName() {
		return JpaRepositoryFactoryBean.class.getName();
	}

	@Override
	protected String getModulePrefix() {
		return getModuleName().toLowerCase(Locale.US);
	}

	@Override
	protected Collection<Class<? extends Annotation>> getIdentifyingAnnotations() {
		return Arrays.asList(Entity.class, MappedSuperclass.class);
	}

	@Override
	protected Collection<Class<?>> getIdentifyingTypes() {
		return Collections.<Class<?>> singleton(BaseSearchJpaRepository.class);
	}

	@Override
	public void postProcess(BeanDefinitionBuilder builder, RepositoryConfigurationSource source) {
		Optional<String> transactionManagerRef = source.getAttribute("transactionManagerRef");
		builder.addPropertyValue("transactionManager", transactionManagerRef.orElse(DEFAULT_TRANSACTION_MANAGER_BEAN_NAME));
		builder.addPropertyValue("entityManager", getEntityManagerBeanDefinitionFor(source, source.getSource()));
		builder.addPropertyReference("mappingContext", JPA_MAPPING_CONTEXT_BEAN_NAME);
	}

	@Override
	public void postProcess(BeanDefinitionBuilder builder, AnnotationRepositoryConfigurationSource config) {
		AnnotationAttributes attributes = config.getAttributes();
		builder.addPropertyValue(ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE,
				attributes.getBoolean(ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE));
	}

	@Override
	public void postProcess(BeanDefinitionBuilder builder, XmlRepositoryConfigurationSource config) {
		Optional<String> enableDefaultTransactions = config.getAttribute(ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE);
		if (enableDefaultTransactions.isPresent() && StringUtils.hasText(enableDefaultTransactions.get())) {
			builder.addPropertyValue(ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE, enableDefaultTransactions.get());
		}
	}
	
	@Override
	public void registerBeansForRoot(BeanDefinitionRegistry registry, RepositoryConfigurationSource config) {
		super.registerBeansForRoot(registry, config);
		Object source = config.getSource();
		registerIfNotAlreadyRegistered(new RootBeanDefinition(MultipleEntityManagerBeanDefinitionRegistrarPostProcessor.class),
				registry, EM_BEAN_DEFINITION_REGISTRAR_POST_PROCESSOR_BEAN_NAME, source);
		registerIfNotAlreadyRegistered(new RootBeanDefinition(JpaMultipleMetamodelMappingContextFactoryBean.class), registry,
				JPA_MAPPING_CONTEXT_BEAN_NAME, source);
		registerIfNotAlreadyRegistered(new RootBeanDefinition(PAB_POST_PROCESSOR), registry,
				AnnotationConfigUtils.PERSISTENCE_ANNOTATION_PROCESSOR_BEAN_NAME, source);
		RootBeanDefinition contextDefinition = new RootBeanDefinition(DefaultJpaContext.class);
		contextDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
		registerIfNotAlreadyRegistered(contextDefinition, registry, JPA_CONTEXT_BEAN_NAME, source);
	}
	
	private static AbstractBeanDefinition getEntityManagerBeanDefinitionFor(RepositoryConfigurationSource config,
			@Nullable Object source) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder
				.rootBeanDefinition("org.daijie.jdbc.jpa.MultipleSharedEntityManagerCreator");
		builder.setFactoryMethod("createSharedEntityManager");
		builder.addConstructorArgReference(getEntityManagerBeanRef(config));
		AbstractBeanDefinition bean = builder.getRawBeanDefinition();
		bean.setSource(source);
		return bean;
	}

	private static String getEntityManagerBeanRef(RepositoryConfigurationSource config) {
		Optional<String> entityManagerFactoryRef = config.getAttribute("entityManagerFactoryRef");
		return entityManagerFactoryRef.orElse("entityManagerFactory");
	}
}
