package org.daijie.jdbc.mybatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import org.daijie.jdbc.interceptor.DefaultRoutingDataSource;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.JdbcTransactionObjectSupport;
import org.springframework.lang.Nullable;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionSynchronizationUtils;

@Deprecated
@SuppressWarnings("serial")
public class MultipleTransactionManager extends DataSourceTransactionManager {
	
	public MultipleTransactionManager(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	protected Object doGetTransaction() {
		MultipleTransactionObject txObject = new MultipleTransactionObject();
		txObject.setSavepointAllowed(isNestedTransactionAllowed());
		ConnectionHolder conHolder =
				(ConnectionHolder) TransactionSynchronizationManager.getResource(obtainDataSource());
		txObject.setConnectionHolder(conHolder, false);
		return txObject;
	}

	@Override
	protected boolean isExistingTransaction(Object transaction) {
		MultipleTransactionObject txObject = (MultipleTransactionObject) transaction;
		return txObject.hasConnectionHolder();
	}
	
	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition) {
		MultipleTransactionObject txObject = (MultipleTransactionObject) transaction;
		try {
			if (!txObject.hasConnectionHolder() ||
					txObject.getConnectionHolder().isSynchronizedWithTransaction()) {
				Connection newCon = obtainDataSource().getConnection();
				if (logger.isDebugEnabled()) {
					logger.debug("Acquired Connection [" + newCon + "] for JDBC transaction");
				}
				txObject.setConnectionHolder(new MultipleConnectionHolder(newCon), true);
			}
			Map<Object, DataSource> targetDataSources = ((DefaultRoutingDataSource) obtainDataSource()).getTargetDataSources();
			Iterator<Entry<Object, DataSource>> iterator = targetDataSources.entrySet().iterator();
			while (iterator.hasNext()) {
				DataSource dataSource = iterator.next().getValue();
				ConnectionHolder connectionHolder = new ConnectionHolder(dataSource.getConnection());
				connectionHolder.setSynchronizedWithTransaction(true);
				Integer previousIsolationLevel = DataSourceUtils.prepareConnectionForTransaction(connectionHolder.getConnection(), definition);
				txObject.setPreviousIsolationLevel(previousIsolationLevel);
				if (connectionHolder.getConnection().getAutoCommit()) {
					txObject.setMustRestoreAutoCommit(true);
					if (logger.isDebugEnabled()) {
						logger.debug("Switching JDBC Connection [" + connectionHolder.getConnection() + "] to manual commit");
					}
					connectionHolder.getConnection().setAutoCommit(false);
				}
				prepareTransactionalConnection(connectionHolder.getConnection(), definition);
				int timeout = determineTimeout(definition);
				if (timeout != TransactionDefinition.TIMEOUT_DEFAULT) {
					connectionHolder.setTimeoutInSeconds(timeout);
				}
				TransactionSynchronizationManager.bindResource(dataSource, connectionHolder);
				((MultipleConnectionHolder) txObject.getConnectionHolder()).getConnectionHolders().add(connectionHolder);
			}
		} catch (Throwable ex) {
			if (txObject.isNewConnectionHolder()) {
				txObject.setConnectionHolder(null, false);
			}
			throw new CannotCreateTransactionException("Could not open JDBC Connection for transaction", ex);
		}
	}

	@Override
	protected Object doSuspend(Object transaction) {
		MultipleTransactionObject txObject = (MultipleTransactionObject) transaction;
		txObject.setConnectionHolder(null);
		return TransactionSynchronizationManager.unbindResource(obtainDataSource());
	}

	@Override
	protected void doResume(@Nullable Object transaction, Object suspendedResources) {
		TransactionSynchronizationManager.bindResource(obtainDataSource(), suspendedResources);
	}
	
	@Override
	protected void doCommit(DefaultTransactionStatus status) {
		status.getTransaction();
		MultipleTransactionObject txObject = (MultipleTransactionObject) status.getTransaction();
		Set<ConnectionHolder> holders = ((MultipleConnectionHolder) txObject.getConnectionHolder()).getConnectionHolders();
//		try {
//			txObject.getConnectionHolder().getConnection().commit();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		holders.forEach(holder -> {
			try {
				holder.getConnection().commit();
			} catch (SQLException ex) {
				throw new TransactionSystemException("Could not commit JDBC transaction", ex);
			}
		});
	}

	@Override
	protected void doRollback(DefaultTransactionStatus status) {
		MultipleTransactionObject txObject = (MultipleTransactionObject) status.getTransaction();
		Set<ConnectionHolder> holders = ((MultipleConnectionHolder) txObject.getConnectionHolder()).getConnectionHolders();
		holders.forEach(holder -> {
			try {
				holder.getConnection().rollback();
			} catch (SQLException ex) {
				throw new TransactionSystemException("Could not roll back JDBC transaction", ex);
			}
		});
	}

	@Override
	protected void doSetRollbackOnly(DefaultTransactionStatus status) {
		MultipleTransactionObject txObject = (MultipleTransactionObject) status.getTransaction();
		if (status.isDebug()) {
			logger.debug("Setting JDBC transaction [" + txObject.getConnectionHolder().getConnection() +
					"] rollback-only");
		}
		txObject.setRollbackOnly();
	}

	@Override
	protected void doCleanupAfterCompletion(Object transaction) {
		MultipleTransactionObject txObject = (MultipleTransactionObject) transaction;
//		if (txObject.isNewConnectionHolder()) {
//			TransactionSynchronizationManager.unbindResource(obtainDataSource());
//		}
		Set<ConnectionHolder> holders = ((MultipleConnectionHolder) txObject.getConnectionHolder()).getConnectionHolders();
		holders.forEach(holder -> {
			try {
				holder.getConnection().setAutoCommit(true);
			} catch (SQLException ex) {
				throw new TransactionSystemException("Could not reset JDBC Connection after transaction", ex);
			}
		});
		txObject.getConnectionHolder().clear();
	}

	private static class MultipleTransactionObject extends JdbcTransactionObjectSupport {

		private boolean newConnectionHolder;

		private boolean mustRestoreAutoCommit;

		public void setConnectionHolder(@Nullable ConnectionHolder conHolder, boolean newConnectionHolder) {
			super.setConnectionHolder(conHolder);
			this.newConnectionHolder = newConnectionHolder;
		}

		public boolean isNewConnectionHolder() {
			return this.newConnectionHolder;
		}

		public void setMustRestoreAutoCommit(boolean mustRestoreAutoCommit) {
			this.mustRestoreAutoCommit = mustRestoreAutoCommit;
		}

		@SuppressWarnings("unused")
		public boolean isMustRestoreAutoCommit() {
			return this.mustRestoreAutoCommit;
		}

		public void setRollbackOnly() {
			getConnectionHolder().setRollbackOnly();
		}

		@Override
		public boolean isRollbackOnly() {
			return getConnectionHolder().isRollbackOnly();
		}

		@Override
		public void flush() {
			if (TransactionSynchronizationManager.isSynchronizationActive()) {
				TransactionSynchronizationUtils.triggerFlush();
			}
		}
	}
}
