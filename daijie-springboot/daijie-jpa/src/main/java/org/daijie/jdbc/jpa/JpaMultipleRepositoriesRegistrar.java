package org.daijie.jdbc.jpa;

import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import java.lang.annotation.Annotation;

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
