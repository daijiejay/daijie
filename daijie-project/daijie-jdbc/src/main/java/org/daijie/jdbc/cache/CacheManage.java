package org.daijie.jdbc.cache;

import org.daijie.jdbc.transaction.TransactionManager;

import java.util.List;

/**
 * 缓存管理
 * @author daijie
 * @since 2019/7/18
 */
public class CacheManage {

    private static Cache cache;

    static {
        if (CacheManage.cache == null) {
            synchronized (CacheManage.class) {
                if (CacheManage.cache == null) {
                    CacheManage.cache = new SessionLevelCache();
                }
            }
        }
    }

    /**
     * 缓存查询结果
     * @param tableNames 缓存对应的表名
     * @param sql sql语句
     * @param resultData 缓存数据
     */
    public static void set(List<String> tableNames, String sql, Object resultData) {
        CacheManage.cache.set(tableNames, sql, resultData);
    }

    /**
     * 获取缓存中的查询结果
     * @param tableNames 缓存对应的表名
     * @param sql sql语句
     * @return 查询结果集
     */
    public static Object get(List<String> tableNames, String sql) {
        return CacheManage.cache.get(tableNames, sql);
    }

    /**
     * 删除缓存，如果当前线程已开启事务且还没有提交事务先记录变更表
     * @param tableName 表名称
     */
    public static void remove(String tableName) {
        if (TransactionManager.isTransaction()) {
            CacheManage.cache.recodeChangedTable(tableName);
        } else {
            CacheManage.cache.remove(tableName);
        }
    }

    /**
     * 是否在当前事物中更新过这个表
     * @param tableNames 表名称
     * @return 返回布尔值
     */
    public static boolean isChangeTable(List<String> tableNames) {
        for (String tableName : tableNames) {
            if (CacheManage.cache.isChangeTable(tableName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 缓存变更提交
     */
    public static void commit() {
        CacheManage.cache.commit();
    }

    /**
     * 缓存变更回滚
     */
    public static void rollback() {
        CacheManage.cache.rollback();
    }
}
