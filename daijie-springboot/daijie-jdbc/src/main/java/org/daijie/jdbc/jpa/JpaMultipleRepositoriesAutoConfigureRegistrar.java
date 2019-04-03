package org.daijie.jdbc.jpa;

import java.lang.annotation.Annotation;

import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

/**
 * 重写JpaRepositoriesAutoConfigureRegistrar类
 * @author daijie_jay
 * @since 2018年5月28日
 */
public class JpaMultipleRepositoriesAutoConfigureRegistrar extends AbstractRepositoryConfigurationSourceSupport {

	@Override
	protected Class<? extends Annotation> getAnnotation() {
		return EnableJpaMultipleRepositories.class;
	}

	@Override
	protected Class<?> getConfiguration() {
		return EnableJpaRepositoriesConfiguration.class;
	}

	@Override
	protected RepositoryConfigurationExtension getRepositoryConfigurationExtension() {
		return new JpaMultipleRepositoryConfigExtension();
	}

	@EnableJpaMultipleRepositories
	private static class EnableJpaRepositoriesConfiguration {

	}
}
