package org.daijie.jdbc.transaction;

import java.sql.SQLException;

/**
 * 事务拦截器
 * @author daijie
 * @since 2019/7/14
 */
public interface TransactionInterceptor {

    /**
     * 关闭事务
     * @param transactionInfo 当前事务信息
     * @throws SQLException SQL异常
     */
    void doFinally(TransactionInfo transactionInfo) throws SQLException;

    /**
     * 提交事务
     * @param transactionInfo 当前事务信息
     * @throws SQLException SQL异常
     */
    void doCommitTransactionAfterReturning(TransactionInfo transactionInfo) throws SQLException;

    /**
     * 回滚事务
     * @param transactionInfo 当前事务信息
     * @throws SQLException SQL异常
     */
    void doRollbackTransactionAfterThrowing(TransactionInfo transactionInfo, Throwable e) throws SQLException;
}
