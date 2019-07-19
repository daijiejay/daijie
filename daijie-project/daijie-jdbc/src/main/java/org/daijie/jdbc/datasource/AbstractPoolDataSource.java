package org.daijie.jdbc.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author daijie
 * @since 2019/5/23
 */
public abstract class AbstractPoolDataSource extends AbstractDataSource {

    private final int initialSize;

    private final int maxActive;

    private boolean poolPreparedStatements = false;

    private boolean defaultAutoCommit = false;

    private LinkedBlockingDeque<Connection> holderPool;

    public AbstractPoolDataSource() {
        this(10, 20);
    }

    public AbstractPoolDataSource(int initialSize, int maxActive) {
        this.initialSize = initialSize;
        this.maxActive = maxActive;
        this.holderPool  = new LinkedBlockingDeque<Connection>(maxActive);
        init();
    }

    public int getInitialSize() {
        return initialSize;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public boolean isPoolPreparedStatements() {
        return poolPreparedStatements;
    }

    public void setPoolPreparedStatements(boolean poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
    }

    public boolean isDefaultAutoCommit() {
        return defaultAutoCommit;
    }

    public void setDefaultAutoCommit(boolean defaultAutoCommit) {
        this.defaultAutoCommit = defaultAutoCommit;
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = this.holderPool.poll();
        if (connection == null) {
            connection = createConnection();
        }
        return connection;
    }

    public void init() {
        for (int i = 0; i < this.initialSize; ++i) {
            Connection connection = createConnection();
            this.holderPool.offer(connection);
        }
    }

    public void releaseConnection(Connection connection) {
        this.holderPool.offer(connection);
    }

    public abstract Connection createConnection();
}
