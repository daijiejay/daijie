package org.daijie.jdbc.datasource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 多个数据源、默认数据源缓存处理
 * @author daijie
 * @since 2019/5/23
 */
public abstract class AbstractDataSource implements DataSource {

    /**
     * 默认数据源字段键值
     */
    public static final String DATA_SOURCE = "default";

    /**
     * 多个数据源
     */
    private Map<Object, DataSource> targetDataSources;

    /**
     * 默认数据源
     */
    private DataSource defaultTargetDataSource;

    @Override
    public Connection getConnection() throws SQLException {
        return this.defaultTargetDataSource.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return this.defaultTargetDataSource.getConnection(username, password);
    }

    public DataSource getDataSource() {
        return this.defaultTargetDataSource;
    }

    public DataSource getDataSource(Object name) {
        return this.targetDataSources.get(name);
    }

    /**
     * 初始化数据源
     * @param targetDataSources 设置数据源
     */
    public void setTargetDataSources(Map<Object, DataSource> targetDataSources) {
        this.targetDataSources = targetDataSources;
    }

    /**
     * 刷新默认数据源
     * @param defaultDataSource 默认数据源
     * @return DataSource
     */
    public DataSource freshDefaultDataSource(Object defaultDataSource) {
        return this.defaultTargetDataSource = this.targetDataSources.get(defaultDataSource);
    }

    public DataSource getDefaultTargetDataSource() {
        return this.defaultTargetDataSource;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
