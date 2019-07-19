package org.daijie.jdbc.cache;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 *  会话级别缓存，作用于当前服务的整个环境共享缓存
 * @author daijie
 * @since 2019/7/14
 */
public class SessionLevelCache extends AbstractCache {

    /**
     * 用于记录已变更的表名
     */
    private ThreadLocal<Set<String>> changedTable = new ThreadLocal();

    public SessionLevelCache() {
        this.changedTable.set(Sets.newConcurrentHashSet());
    }

    @Override
    public void recodeChangedTable(String tableName) {
        this.changedTable.get().add(tableName);
    }

    @Override
    public void commit() {
        for (String tableName : this.changedTable.get()) {
            super.remove(tableName);
        }
        this.clear();
    }

    @Override
    public void rollback() {
        this.clear();
    }

    /**
     * 清除已记录当前事务变更的表名
     */
    private void clear() {
        this.changedTable.get().clear();
    }
}
