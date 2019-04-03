package org.daijie.jdbc.jpa;

import java.lang.annotation.Annotation;

import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

/**
 * 重写JpaRepositoriesRegistrar
 * @author daijie_jay
 * @since 2018年5月25日
 */
public class JpaMultipleRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {

	@Override
	protected Class<? extends Annotation> getAnnotation() {
		return EnableJpaMultipleRepositories.class;
	}

	@Override
	protected RepositoryConfigurationExtension getExtension() {
		return new JpaMultipleRepositoryConfigExtension();
	}
}
