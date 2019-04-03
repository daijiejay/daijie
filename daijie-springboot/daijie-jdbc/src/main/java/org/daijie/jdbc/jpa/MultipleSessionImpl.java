package org.daijie.jdbc.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;

import org.daijie.jdbc.DbContextHolder;
import org.daijie.jdbc.transaction.MultipleTransactionSynchronizationEntityManager;

/**
 * 多个SessionImpl代理类
 * @author daijie_jay
 * @since 2018年5月28日
 */
@SuppressWarnings("rawtypes")
public class MultipleSessionImpl implements EntityManager {
	
	private Map<String, MultipleTransactionSynchronizationEntityManager> entityManagers = new HashMap<String, MultipleTransactionSynchronizationEntityManager>();

	public Map<String, MultipleTransactionSynchronizationEntityManager> getEntityManagers() {
		return entityManagers;
	}

	public void setEntityManagers(Map<String, MultipleTransactionSynchronizationEntityManager> entityManagers) {
		this.entityManagers = entityManagers;
	}
	
	public EntityManager getDefaultEntityManager() {
		if (entityManagers.get(DbContextHolder.getDataSourceName()) != null) {
			EntityManager entityManager = entityManagers.get(DbContextHolder.getDataSourceName()).getEntityManager();
			if (entityManager != null) {
				return entityManager;
			}
		}
		return entityManagers.entrySet().iterator().next().getValue().getEntityManager();
	}

	@Override
	public void persist(Object entity) {
		getDefaultEntityManager().persist(entity);
	}

	@Override
	public <T> T merge(T entity) {
		return getDefaultEntityManager().merge(entity);
	}

	@Override
	public void remove(Object entity) {
		getDefaultEntityManager().remove(entity);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey) {
		return getDefaultEntityManager().find(entityClass, primaryKey);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
		return getDefaultEntityManager().find(entityClass, primaryKey, properties);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
		return getDefaultEntityManager().find(entityClass, primaryKey, lockMode);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
		return getDefaultEntityManager().find(entityClass, primaryKey, lockMode, properties);
	}

	@Override
	public <T> T getReference(Class<T> entityClass, Object primaryKey) {
		return getDefaultEntityManager().getReference(entityClass, primaryKey);
	}

	@Override
	public void flush() {
		getDefaultEntityManager().flush();
	}

	@Override
	public void setFlushMode(FlushModeType flushMode) {
		getDefaultEntityManager().setFlushMode(flushMode);
	}

	@Override
	public FlushModeType getFlushMode() {
		return getDefaultEntityManager().getFlushMode();
	}

	@Override
	public void lock(Object entity, LockModeType lockMode) {
		getDefaultEntityManager().lock(entity, lockMode);
	}

	@Override
	public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
		getDefaultEntityManager().lock(entity, lockMode, properties);
	}

	@Override
	public void refresh(Object entity) {
		getDefaultEntityManager().refresh(entity);
	}

	@Override
	public void refresh(Object entity, Map<String, Object> properties) {
		getDefaultEntityManager().refresh(entity, properties);
	}

	@Override
	public void refresh(Object entity, LockModeType lockMode) {
		getDefaultEntityManager().refresh(entity, lockMode);
	}

	@Override
	public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
		getDefaultEntityManager().refresh(entity, lockMode, properties);
	}

	@Override
	public void clear() {
		getDefaultEntityManager().clear();
	}

	@Override
	public void detach(Object entity) {
		getDefaultEntityManager().detach(entity);
	}

	@Override
	public boolean contains(Object entity) {
		return getDefaultEntityManager().contains(entity);
	}

	@Override
	public LockModeType getLockMode(Object entity) {
		return getDefaultEntityManager().getLockMode(entity);
	}

	@Override
	public void setProperty(String propertyName, Object value) {
		getDefaultEntityManager().setProperty(propertyName, value);
	}

	@Override
	public Map<String, Object> getProperties() {
		return getDefaultEntityManager().getProperties();
	}

	@Override
	public Query createQuery(String qlString) {
		return getDefaultEntityManager().createQuery(qlString);
	}

	@Override
	public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
		return getDefaultEntityManager().createQuery(criteriaQuery);
	}

	@Override
	public Query createQuery(CriteriaUpdate updateQuery) {
		return getDefaultEntityManager().createQuery(updateQuery);
	}

	@Override
	public Query createQuery(CriteriaDelete deleteQuery) {
		return getDefaultEntityManager().createQuery(deleteQuery);
	}

	@Override
	public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
		return getDefaultEntityManager().createQuery(qlString, resultClass);
	}

	@Override
	public Query createNamedQuery(String name) {
		return getDefaultEntityManager().createNamedQuery(name);
	}

	@Override
	public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
		return getDefaultEntityManager().createNamedQuery(name, resultClass);
	}

	@Override
	public Query createNativeQuery(String sqlString) {
		return getDefaultEntityManager().createNativeQuery(sqlString);
	}

	@Override
	public Query createNativeQuery(String sqlString, Class resultClass) {
		return getDefaultEntityManager().createNativeQuery(sqlString, resultClass);
	}

	@Override
	public Query createNativeQuery(String sqlString, String resultSetMapping) {
		return getDefaultEntityManager().createNativeQuery(sqlString, resultSetMapping);
	}

	@Override
	public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
		return getDefaultEntityManager().createNamedStoredProcedureQuery(name);
	}

	@Override
	public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
		return getDefaultEntityManager().createStoredProcedureQuery(procedureName);
	}

	@Override
	public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
		return getDefaultEntityManager().createStoredProcedureQuery(procedureName, resultClasses);
	}

	@Override
	public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
		return getDefaultEntityManager().createStoredProcedureQuery(procedureName, resultSetMappings);
	}

	@Override
	public void joinTransaction() {
		getDefaultEntityManager().joinTransaction();
	}

	@Override
	public boolean isJoinedToTransaction() {
		return getDefaultEntityManager().isJoinedToTransaction();
	}

	@Override
	public <T> T unwrap(Class<T> cls) {
		return getDefaultEntityManager().unwrap(cls);
	}

	@Override
	public Object getDelegate() {
		return getDefaultEntityManager().getDelegate();
	}

	@Override
	public void close() {
		getDefaultEntityManager().close();
	}

	@Override
	public boolean isOpen() {
		return getDefaultEntityManager().isOpen();
	}

	@Override
	public EntityTransaction getTransaction() {
		return getDefaultEntityManager().getTransaction();
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return getDefaultEntityManager().getEntityManagerFactory();
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return getDefaultEntityManager().getCriteriaBuilder();
	}

	@Override
	public Metamodel getMetamodel() {
		return getDefaultEntityManager().getMetamodel();
	}

	@Override
	public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
		return getDefaultEntityManager().createEntityGraph(rootType);
	}

	@Override
	public EntityGraph<?> createEntityGraph(String graphName) {
		return getDefaultEntityManager().createEntityGraph(graphName);
	}

	@Override
	public EntityGraph<?> getEntityGraph(String graphName) {
		return getDefaultEntityManager().getEntityGraph(graphName);
	}

	@Override
	public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
		return getDefaultEntityManager().getEntityGraphs(entityClass);
	}

}
