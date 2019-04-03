package org.daijie.jdbc.jpa.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.daijie.jdbc.DbContextHolder;
import org.daijie.jdbc.annotation.SelectDataSource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * repository接口数据库操作实现类
 * @author daijie_jay
 * @since 2018年5月28日
 * @param <E> 实体类
 * @param <ID> 实体ID
 */
@Repository
@Transactional(readOnly = true)
public class JpaMultipleRepository<E,ID extends Serializable> extends SimpleJpaRepository<E, ID> implements BaseSearchJpaRepository<E, ID> {
	
	private final Class<? extends E> repositoryInterface;
	
	public JpaMultipleRepository(Class<E> domainClass, EntityManager entityManager, Class<? extends E> repositoryInterface) {
		super(domainClass, entityManager);
		this.repositoryInterface = repositoryInterface;
	}
	
	/**
	 * 获取repository接口选择数据源
	 */
	public void selectDataSource() {
		if(repositoryInterface.isAnnotationPresent(SelectDataSource.class)){
        	SelectDataSource selectDataSource = (SelectDataSource) repositoryInterface.getAnnotation(SelectDataSource.class);
        	DbContextHolder.setDataSourceName(selectDataSource.name());
        }
	}

	@Override
	public void deleteById(ID id) {
		selectDataSource();
		super.deleteById(id);
	}

	@Override
	public void delete(E entity) {
		selectDataSource();
		super.delete(entity);
	}

	@Override
	public void deleteInBatch(Iterable<E> entities) {
		selectDataSource();
		super.deleteInBatch(entities);
	}

	@Override
	public void deleteAllInBatch() {
		selectDataSource();
		super.deleteAllInBatch();
	}

	@Override
	public Optional<E> findById(ID id) {
		selectDataSource();
		return super.findById(id);
	}

	@Override
	public E getOne(ID id) {
		selectDataSource();
		return super.getOne(id);
	}

	@Override
	public boolean existsById(ID id) {
		selectDataSource();
		return super.existsById(id);
	}

	@Override
	public List<E> findAll() {
		selectDataSource();
		return super.findAll();
	}

	@Override
	public List<E> findAllById(Iterable<ID> ids) {
		selectDataSource();
		return super.findAllById(ids);
	}

	@Override
	public List<E> findAll(Sort sort) {
		selectDataSource();
		return super.findAll(sort);
	}

	@Override
	public Page<E> findAll(Pageable pageable) {
		selectDataSource();
		return super.findAll(pageable);
	}

	@Override
	public Optional<E> findOne(@Nullable Specification<E> spec) {
		selectDataSource();
		return super.findOne(spec);
	}

	@Override
	public List<E> findAll(@Nullable Specification<E> spec) {
		selectDataSource();
		return super.findAll(spec);
	}

	@Override
	public Page<E> findAll(@Nullable Specification<E> spec, Pageable pageable) {
		selectDataSource();
		return super.findAll(spec, pageable);
	}

	@Override
	public List<E> findAll(@Nullable Specification<E> spec, Sort sort) {
		selectDataSource();
		return super.findAll(spec, sort);
	}

	@Override
	public <S extends E> Optional<S> findOne(Example<S> example) {
		selectDataSource();
		return super.findOne(example);
	}

	@Override
	public <S extends E> long count(Example<S> example) {
		selectDataSource();
		return super.count(example);
	}

	@Override
	public <S extends E> boolean exists(Example<S> example) {
		selectDataSource();
		return super.exists(example);
	}

	@Override
	public <S extends E> List<S> findAll(Example<S> example) {
		selectDataSource();
		return super.findAll(example);
	}

	@Override
	public <S extends E> List<S> findAll(Example<S> example, Sort sort) {
		selectDataSource();
		return super.findAll(example, sort);
	}

	@Override
	public <S extends E> Page<S> findAll(Example<S> example, Pageable pageable) {
		selectDataSource();
		return super.findAll(example, pageable);
	}

	@Override
	public long count() {
		selectDataSource();
		return super.count();
	}

	@Override
	public long count(@Nullable Specification<E> spec) {
		selectDataSource();
		return super.count(spec);
	}

	@Override
	public <S extends E> S save(S entity) {
		selectDataSource();
		return super.save(entity);
	}

	@Override
	public <S extends E> S saveAndFlush(S entity) {
		selectDataSource();
		return super.saveAndFlush(entity);
	}

	@Override
	public <S extends E> List<S> saveAll(Iterable<S> entities) {
		selectDataSource();
		return super.saveAll(entities);
	}

	@Override
	public void flush() {
		selectDataSource();
		super.flush();
	}
}
