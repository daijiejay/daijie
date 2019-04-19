package org.daijie.jdbc.jpa;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.lang.Nullable;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 重写私有类JpaMetamodelMappingContextFactoryBean
 * @author daijie_jay
 * @since 2018年5月25日
 */
public class JpaMultipleMetamodelMappingContextFactoryBean extends AbstractFactoryBean<JpaMetamodelMappingContext>
	implements ApplicationContextAware {

	private @Nullable ListableBeanFactory beanFactory;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.beanFactory = applicationContext;
	}

	@Override
	public Class<?> getObjectType() {
		return JpaMetamodelMappingContext.class;
	}

	@Override
	protected JpaMetamodelMappingContext createInstance() throws Exception {
		Set<Metamodel> models = getMetamodels();
		Set<Class<?>> entitySources = new HashSet<Class<?>>();
		for (Metamodel metamodel : models) {
			for (ManagedType<?> type : metamodel.getManagedTypes()) {
				Class<?> javaType = type.getJavaType();
				if (javaType != null) {
					entitySources.add(javaType);
				}
			}
		}
		JpaMetamodelMappingContext context = new JpaMetamodelMappingContext(models);
		context.setInitialEntitySet(entitySources);
		context.initialize();
		return context;
	}

	private Set<Metamodel> getMetamodels() {
		if (beanFactory == null) {
			throw new IllegalStateException("BeanFactory must not be null!");
		}
		Collection<EntityManagerFactory> factories = BeanFactoryUtils
				.beansOfTypeIncludingAncestors(beanFactory, EntityManagerFactory.class).values();
		Set<Metamodel> metamodels = new HashSet<Metamodel>(factories.size());
		for (EntityManagerFactory emf : factories) {
			metamodels.add(emf.getMetamodel());
		}
		return metamodels;
	}
}
