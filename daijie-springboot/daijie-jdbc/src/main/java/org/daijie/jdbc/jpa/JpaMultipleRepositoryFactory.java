package org.daijie.jdbc.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.daijie.jdbc.jpa.repository.JpaMultipleRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;

/**
 * 定义一个多数据源的repositoryFactory
 * @author daijie_jay
 * @since 2018年5月28日
 * @param <E> 实体类
 * @param <ID> 实体ID
 */
public class JpaMultipleRepositoryFactory<E, ID extends Serializable> extends JpaRepositoryFactory {

	public JpaMultipleRepositoryFactory(EntityManager entityManager) {
		super(entityManager);
	}

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return JpaMultipleRepository.class;
    }
}
