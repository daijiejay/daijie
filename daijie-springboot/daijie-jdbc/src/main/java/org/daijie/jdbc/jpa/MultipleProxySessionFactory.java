package org.daijie.jdbc.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Cache;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;
import javax.persistence.SynchronizationType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;

import org.daijie.jdbc.DbContextHolder;

/**
 * 多个seesionFactory代理类
 * @author daijie_jay
 * @since 2018年5月28日
 */
@SuppressWarnings("rawtypes")
public class MultipleProxySessionFactory implements EntityManagerFactory {
	
	private Map<String, EntityManagerFactory> entityManagerFactories = new HashMap<String, EntityManagerFactory>();

	public Map<String, EntityManagerFactory> getEntityManagerFactories() {
		return entityManagerFactories;
	}

	public void setEntityManagerFactories(Map<String, EntityManagerFactory> entityManagerFactories) {
		this.entityManagerFactories = entityManagerFactories;
	}
	
	public EntityManagerFactory getDefaultEntityManagerFactory() {
		EntityManagerFactory entityManagerFactory = entityManagerFactories.get(DbContextHolder.getDataSourceName());
		if (entityManagerFactory == null) {
			return entityManagerFactories.entrySet().iterator().next().getValue();
		}
		return entityManagerFactory;
	}

	@Override
	public EntityManager createEntityManager() {
		return getDefaultEntityManagerFactory().createEntityManager();
	}

	@Override
	public EntityManager createEntityManager(Map map) {
		return getDefaultEntityManagerFactory().createEntityManager(map);
	}

	@Override
	public EntityManager createEntityManager(SynchronizationType synchronizationType) {
		return getDefaultEntityManagerFactory().createEntityManager(synchronizationType);
	}

	@Override
	public EntityManager createEntityManager(SynchronizationType synchronizationType, Map map) {
		return getDefaultEntityManagerFactory().createEntityManager(synchronizationType, map);
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return getDefaultEntityManagerFactory().getCriteriaBuilder();
	}

	@Override
	public Metamodel getMetamodel() {
		return getDefaultEntityManagerFactory().getMetamodel();
	}

	@Override
	public boolean isOpen() {
		return getDefaultEntityManagerFactory().isOpen();
	}

	@Override
	public void close() {
		getDefaultEntityManagerFactory().close();
	}

	@Override
	public Map<String, Object> getProperties() {
		return getDefaultEntityManagerFactory().getProperties();
	}

	@Override
	public Cache getCache() {
		return getDefaultEntityManagerFactory().getCache();
	}

	@Override
	public PersistenceUnitUtil getPersistenceUnitUtil() {
		return getDefaultEntityManagerFactory().getPersistenceUnitUtil();
	}

	@Override
	public void addNamedQuery(String name, Query query) {
		getDefaultEntityManagerFactory().addNamedQuery(name, query);
	}

	@Override
	public <T> T unwrap(Class<T> cls) {
		return getDefaultEntityManagerFactory().unwrap(cls);
	}

	@Override
	public <T> void addNamedEntityGraph(String graphName, EntityGraph<T> entityGraph) {
		getDefaultEntityManagerFactory().addNamedEntityGraph(graphName, entityGraph);
	}

}
