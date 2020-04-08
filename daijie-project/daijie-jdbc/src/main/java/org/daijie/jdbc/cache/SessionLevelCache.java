package org.daijie.jdbc.cache;

import com.google.common.collect.Sets;

import java.util.Iterator;
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

    /**
     * 检查当前线程记录的已变更的表名对象是否有初始化
     */
    private void check() {
        if (this.changedTable.get() == null) {
            this.changedTable.set(Sets.newConcurrentHashSet());
        }
    }

    @Override
    public void recodeChangedTable(String tableName) {
        check();
        this.changedTable.get().add(tableName);
    }

    @Override
    public void commit() {
        check();
        for (String tableName : this.changedTable.get()) {
            super.remove(tableName);
        }
        this.clear();
    }

    @Override
    public void rollback() {
        check();
        this.clear();
    }

    /**
     * 清除已记录当前事务变更的表名
     */
    private void clear() {
        this.changedTable.get().clear();
    }

    @Override
    public boolean isChangeTable(String tableName) {
        check();
        Iterator<String> iterator = this.changedTable.get().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(tableName)) {
                return true;
            }
        }
        return false;
    }
}
