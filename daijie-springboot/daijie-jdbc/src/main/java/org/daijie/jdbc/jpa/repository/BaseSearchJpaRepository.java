package org.daijie.jdbc.jpa.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * repository基础接口类
 * @author daijie_jay
 * @since 2018年5月28日
 * @param <E> 实体类
 * @param <ID> 实体ID
 */
@NoRepositoryBean
public interface BaseSearchJpaRepository<E,ID extends Serializable> extends JpaRepository<E,ID> {

}
