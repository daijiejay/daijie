package org.daijie.jdbc.cache;

import java.util.List;

/**
 * 查询结果的一些缓存操作
 * @author daijie
 * @since 2019/7/14
 */
public interface Cache {

    /**
     * 缓存查询结果
     * @param tableNames 缓存对应的表名
     * @param sql sql语句
     * @param resultData 缓存数据
     */
    void set(List<String> tableNames, String sql, Object resultData);

    /**
     * 获取缓存中的查询结果
     * @param tableNames 缓存对应的表名
     * @param sql sql语句
     * @return 查询结果集
     */
    Object get(List<String> tableNames, String sql);

    /**
     * 删除缓存数据
     * @param tableName 缓存对应的表名
     */
    void remove(String tableName);

    /**
     * 记录当前事务已变更的表名
     * @param tableName 表名称
     */
    public void recodeChangedTable(String tableName);

    /**
     * 提交事务后清除对应表缓存
     */
    public void commit();

    /**
     * 回滚事务后不对对应表缓存清除
     */
    public void rollback();

    /**
     * 是否在当前事物中更新过这个表
     * @param tableName 表名称
     * @return 返回布尔值
     */
    boolean isChangeTable(String tableName);
}
