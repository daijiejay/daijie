package org.daijie.jdbc.mybatis.transaction;

import static org.springframework.util.Assert.notNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.transaction.Transaction;
import org.daijie.jdbc.DbContextHolder;
import org.daijie.jdbc.interceptor.DefaultRoutingDataSource;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 每次访问mapper接口获取数据源连接
 * 动态切换数据源
 * @author daijie_jay
 * @since 2018年5月28日
 */
public class MybatisMultipleTransaction implements Transaction {

	private static final Log LOGGER = LogFactory.getLog(MybatisMultipleTransaction.class);

	private final DefaultRoutingDataSource dataSource;

	private Connection connection;

	private boolean isConnectionTransactional;

	private boolean autoCommit;
	
	private TrancationsManage trancationsManage;

	public MybatisMultipleTransaction(DataSource dataSource) {
		notNull(dataSource, "没有初始化数据源");
		this.dataSource = (DefaultRoutingDataSource) dataSource;
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (this.trancationsManage == null || this.trancationsManage.get(DbContextHolder.getDataSourceName()) == null) {
			openConnection();
		}
		return this.connection;
	}

	@Override
	public void commit() throws SQLException {
		if (this.trancationsManage != null) {
			Iterator<Entry<String, MybatisMultipleTransaction>> iterator = this.trancationsManage.getIterator();
			while (iterator.hasNext()) {
				Entry<String, MybatisMultipleTransaction> next = iterator.next();
				if (next.getValue().getConnection() != null && !next.getValue().getConnectionTransactional() && !next.getValue().getAutoCommit()) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("提交事务 [" + next.getValue().getConnection() + "]");
					}
					next.getValue().getConnection().commit();
				}
			}
		}
	}

	@Override
	public void rollback() throws SQLException {
		if (this.trancationsManage != null) {
			Iterator<Entry<String, MybatisMultipleTransaction>> iterator = this.trancationsManage.getIterator();
			while (iterator.hasNext()) {
				Entry<String, MybatisMultipleTransaction> next = iterator.next();
				if (next.getValue().getConnection() != null && !next.getValue().getConnectionTransactional() && !next.getValue().getAutoCommit()) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("回滚事务 [" + next.getValue().getConnection() + "]");
					}
					next.getValue().getConnection().rollback();
				}
			}
		}
	}

	@Override
	public void close() throws SQLException {
		if (this.trancationsManage != null) {
			Iterator<Entry<String, MybatisMultipleTransaction>> iterator = this.trancationsManage.getIterator();
			while (iterator.hasNext()) {
				Entry<String, MybatisMultipleTransaction> next = iterator.next();
				DataSourceUtils.releaseConnection(next.getValue().getConnection(), next.getValue().getDataSource());
				this.trancationsManage.remove(next.getKey());
			}
		}
	}

	@Override
	public Integer getTimeout() throws SQLException {
		ConnectionHolder holder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
		if (holder != null && holder.hasTimeout()) {
			return holder.getTimeToLiveInSeconds();
		} 
		return null;
	}

	private void openConnection() throws SQLException {
		DataSource dataSource = (DataSource) this.dataSource.getTargetDataSources().get(DbContextHolder.getDataSourceName());
		if (dataSource == null || this.dataSource.getTargetDataSources().size() == 1) {
			dataSource = this.dataSource;
		}
		this.connection = DataSourceUtils.getConnection(dataSource);
		this.isConnectionTransactional = DataSourceUtils.isConnectionTransactional(this.connection, dataSource);
		this.connection.setAutoCommit(!this.isConnectionTransactional);
		this.autoCommit = this.connection.getAutoCommit();
		
		trancationsManage = new TrancationsManage();
		trancationsManage.put(DbContextHolder.getDataSourceName(), this);
	}

	private boolean getConnectionTransactional() {
		return isConnectionTransactional;
	}

	private boolean getAutoCommit() {
		return autoCommit;
	}
	
	private DataSource getDataSource() {
		return dataSource;
	}
	
	/**
	 * 多数据源管理
	 * 缓存数据源，等待事务处理完毕后进行清除
	 * @author daijie_jay
	 * @since 2018年6月21日
	 */
	private class TrancationsManage {
		
		public Map<String, MybatisMultipleTransaction> transactions = new HashMap<String, MybatisMultipleTransaction>();
		
		protected void put(String dataSource, MybatisMultipleTransaction transaction) {
			transactions.put(dataSource, transaction);
		}
		
		protected MybatisMultipleTransaction get(String dataSource) {
			return transactions.get(dataSource);
		}
		
		protected void remove(String dataSource) {
			transactions.remove(dataSource);
		}
		
		protected Iterator<Entry<String, MybatisMultipleTransaction>> getIterator() {
			return transactions.entrySet().iterator();
		}
	}
}
