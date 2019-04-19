package org.daijie.jdbc.jpa;

import org.daijie.jdbc.jpa.repository.JpaMultipleRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * 构建repositoryFactory Bean
 * @author daijie_jay
 * @since 2018年5月28日
 * @param <E> repository接口类
 * @param <R> 实体类
 * @param <ID> 实体ID
 */
public class JpaMultipleRepositoryFactoryBean<E extends PagingAndSortingRepository<R, ID>, R extends JpaMultipleRepository<E,ID>, ID extends Serializable> 
	extends JpaRepositoryFactoryBean<E, R, ID> {

	public JpaMultipleRepositoryFactoryBean(Class<? extends E> repositoryInterface) {
		super(repositoryInterface);
	}

	@Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new JpaMultipleRepositoryFactory<E, ID>(entityManager);
    }
}
