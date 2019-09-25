package org.daijie.jdbc.cache;

/**
 * @author daijie
 * @since 2019/7/16
 */
public class TransactionLevelCache extends AbstractCache {


    @Override
    public void recodeChangedTable(String tableName) {

    }

    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

    }

    @Override
    public boolean isChangeTable(String tableName) {
        return false;
    }
}
