package org.daijie.jdbc.transaction;

/**
 * 事务隔离级别，参考spring
 * @author daijie_jay
 * @since 2019年11月10日
 */
public enum Isolation {

    DEFAULT(TransactionDefinition.ISOLATION_DEFAULT),

    READ_UNCOMMITTED(TransactionDefinition.ISOLATION_READ_UNCOMMITTED),

    READ_COMMITTED(TransactionDefinition.ISOLATION_READ_COMMITTED),

    REPEATABLE_READ(TransactionDefinition.ISOLATION_REPEATABLE_READ),

    SERIALIZABLE(TransactionDefinition.ISOLATION_SERIALIZABLE);


    private final int value;


    Isolation(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
