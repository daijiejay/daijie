package org.daijie.jdbc.transaction;

/**
 * 事务隔离级别
 * @author daijie
 * @since 2019/6/22
 */
public enum  TransactionIsolationLevel {
    /**
     * 默认事务
     */
    NONE(0),
    /**
     * 读不提交
     */
    READ_UNCOMMITTED(1),
    /**
     * 读提交
     */
    READ_COMMITTED(2),
    /**
     * 可重复读
     */
    REPEATABLE_READ(4),
    /**
     * 不同事务隔离串行化
     */
    SERIALIZABLE(8);

    private final int level;

    private TransactionIsolationLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }
}
