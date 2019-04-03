package org.daijie.jdbc.transaction;

import javax.persistence.EntityManager;

import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.core.Ordered;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.lang.Nullable;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.ResourceHolderSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 多数据源处理管理器
 * @author daijie_jay
 * @since 2018年5月28日
 */
public class MultipleTransactionSynchronizationEntityManager extends ResourceHolderSynchronization<EntityManagerHolder, EntityManager>
	implements Ordered {

	@Nullable
	private final PersistenceExceptionTranslator exceptionTranslator;

	private TransactionStatus status = TransactionStatus.NOT_ACTIVE;
	
	public volatile boolean closeOnCompletion = false;
	
	private final EntityManager entityManager;

	public MultipleTransactionSynchronizationEntityManager(EntityManager entityManager, @Nullable PersistenceExceptionTranslator exceptionTranslator) {
		super(new EntityManagerHolder(entityManager), entityManager);
		this.entityManager = entityManager;
		this.exceptionTranslator = exceptionTranslator;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	/**
	 * 通过TransactionSynchronizationManager开启事务
	 */
	public void begin() {
		if (status != TransactionStatus.ACTIVE) {
			status = TransactionStatus.ACTIVE;
			this.entityManager.getTransaction().begin();
			TransactionSynchronizationManager.bindResource(this.entityManager, this);
			TransactionSynchronizationManager.registerSynchronization(this);
		}
	}
	
	@Override
	public int getOrder() {
		return EntityManagerFactoryUtils.ENTITY_MANAGER_SYNCHRONIZATION_ORDER - 1;
	}

	@Override
	protected void flushResource(EntityManagerHolder resourceHolder) {
		try {
			this.entityManager.flush();
		}
		catch (RuntimeException ex) {
			throw convertException(ex);
		}
	}

	@Override
	protected boolean shouldReleaseBeforeCompletion() {
		return false;
	}

	@Override
	public void afterCommit() {
		super.afterCommit();
		try {
			this.entityManager.getTransaction().commit();
		}
		catch (RuntimeException ex) {
			throw convertException(ex);
		}
	}

	@Override
	public void afterCompletion(int status) {
		try {
			super.afterCompletion(status);
			if (status != STATUS_COMMITTED) {
				try {
					this.entityManager.getTransaction().rollback();
				}
				catch (RuntimeException ex) {
					throw convertException(ex);
				}
			}
		}
		finally {
			if (this.closeOnCompletion) {
				EntityManagerFactoryUtils.closeEntityManager(this.entityManager);
			}
		}
	}

	private RuntimeException convertException(RuntimeException ex) {
		DataAccessException daex = (this.exceptionTranslator != null) ?
				this.exceptionTranslator.translateExceptionIfPossible(ex) :
				EntityManagerFactoryUtils.convertJpaAccessExceptionIfPossible(ex);
		return (daex != null ? daex : ex);
	}
}
