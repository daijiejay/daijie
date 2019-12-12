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
     * @throws SQLException SQL异常
     */
    void doFinally() throws SQLException;

    /**
     * 提交事务
     * @throws SQLException SQL异常
     */
    void doCommitTransactionAfterReturning() throws SQLException;

    /**
     * 回滚事务
     * @param throwable 异常栈信息
     * @throws SQLException SQL异常
     */
    void doRollbackTransactionAfterThrowing(Throwable throwable) throws SQLException;
}
