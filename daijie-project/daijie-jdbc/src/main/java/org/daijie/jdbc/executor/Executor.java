package org.daijie.jdbc.executor;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * SQL执行器
 * @author daijie_jay
 * @since 2018年06月22日
 */
public interface Executor {

    /**
     * 获取数据源连接
     * @return Connection 数据源连接
     * @throws SQLException SQL异常
     */
    Connection getConnection() throws SQLException;

    /**
     * SQL执行总线
     * @return Object 返回Mapper方法对应的返回对象
     * @throws SQLException SQL异常
     */
    Object execute() throws SQLException;

    /**
     * 查询SQL执行方法
     * @return Object 返回Mapper方法对应的返回对象
     * @throws SQLException SQL异常
     * @throws IllegalAccessException 对象访问权限异常
     * @throws InstantiationException 对象没有无参构造函数
     */
    Object executeQuery() throws SQLException, IllegalAccessException, InstantiationException;

    /**
     * 变更SQL执行方法
     * @return Object 返回Mapper方法对应的返回对象
     * @throws SQLException SQL异常
     */
    Object executeUpdate() throws SQLException;

    /**
     * 批量执行SQL
     * @return 批量执行结果
     * @throws SQLException SQL异常
     */
    public Object executeBatch() throws SQLException;

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
    void closed() throws SQLException;
}
