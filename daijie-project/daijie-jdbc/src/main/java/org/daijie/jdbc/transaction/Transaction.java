package org.daijie.jdbc.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 获取连接处理事务信息
 * @author daijie
 * @since 2019/6/3
 */
public interface Transaction {

    /**
     * 获取数据库连接
     * @return 返回数据库连接
     * @throws SQLException SQL异常
     */
    Connection getConnection() throws SQLException;

    /**
     * 提交事务
     * @throws SQLException SQL异常
     */
    void commit() throws SQLException;

    /**
     * 回滚事务
     * @throws SQLException SQL异常
     */
    void rollback() throws SQLException;

    /**
     * 关闭事务
     * @throws SQLException SQL异常
     */
    void close() throws SQLException;

    /**
     * 连接超时时间
     * @return 返回连接超时时间
     * @throws SQLException SQL异常
     */
    Integer getTimeout() throws SQLException;
}
