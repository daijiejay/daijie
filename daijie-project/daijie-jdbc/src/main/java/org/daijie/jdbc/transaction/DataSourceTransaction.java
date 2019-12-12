package org.daijie.jdbc.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据源获取连接处理事务信息
 * @author daijie
 * @since 2019/6/3
 */
public class DataSourceTransaction implements Transaction {

    private final Logger log = LoggerFactory.getLogger(DataSourceTransaction.class);

    /**
     * 数据源具体实现类
     */
    private final DataSource dataSource ;

    /**
     * 数据库连接
     */
    private Connection connection;

    /**
     * 是否自动提交
     */
    private boolean autoCommit;

    public DataSourceTransaction(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (TransactionManager.isTransaction()) {
            return this.getConnection(false);
        }
        return this.getConnection(true);
    }

    /**
     * 获取数据库连接
     * @param autoCommit 是否自动提交
     * @return 返回数据库连接
     * @throws SQLException sql异常
     */
    public Connection getConnection(boolean autoCommit) throws SQLException {
        return this.getConnection(autoCommit, TransactionIsolationLevel.NONE);
    }

    /**
     * 获取数据库连接
     * @param autoCommit 是否自动提交
     * @param level 事务隔离级别
     * @return 返回数据库连接
     * @throws SQLException sql异常
     */
    public Connection getConnection(boolean autoCommit, TransactionIsolationLevel level) throws SQLException {
        if (this.connection == null || this.connection.isClosed()) {
            this.connection = this.dataSource.getConnection();
            this.connection.setAutoCommit(autoCommit);
            if (TransactionIsolationLevel.NONE != level) {
                this.connection.setTransactionIsolation(level.getLevel());
            }
            this.setAutoCommit(autoCommit);
        }
        return this.connection;
    }

    @Override
    public void commit() throws SQLException {
        if (!isClosed() && !this.isAutoCommit()) {
            if (log.isDebugEnabled()) {
                log.debug("Committing JDBC Connection [" + this.connection + "]");
            }
            this.connection.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (!isClosed() && !this.isAutoCommit()) {
            if (log.isDebugEnabled()) {
                log.debug("Rolling back JDBC Connection [" + this.connection + "]");
            }
            this.connection.rollback();
        }
    }

    @Override
    public void close() throws SQLException {
        if (!isClosed()) {
            if (!this.connection.getAutoCommit()) {
                this.connection.setAutoCommit(true);
            }
            if (log.isDebugEnabled()) {
                log.debug("Closing JDBC Connection [" + this.connection + "]");
            }
            this.connection.close();
        }
    }

    @Override
    public Integer getTimeout() throws SQLException {
        return null;
    }

    /**
     * 连接是否已关闭
     * @return 返回布尔值
     * @throws SQLException SQL异常
     */
    private boolean isClosed() throws SQLException {
        return this.connection == null || this.connection.isClosed();
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }
}
