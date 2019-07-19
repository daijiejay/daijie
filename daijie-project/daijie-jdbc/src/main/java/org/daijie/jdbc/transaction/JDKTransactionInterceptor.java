package org.daijie.jdbc.transaction;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.sql.SQLException;
import java.util.Iterator;

/**
 * JDK代理事务拦截器
 * @author daijie
 * @since 2019/6/3
 */
public class JDKTransactionInterceptor implements MethodInterceptor, TransactionInterceptor {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        TransactionInfo transactionInfo = TransactionManage.createTransactionInfo();
        Object result = null;
        try {
            result = methodInvocation.proceed();
            doCommitTransactionAfterReturning(transactionInfo);
        }catch (Throwable e) {
            doRollbackTransactionAfterThrowing(transactionInfo, e);
            throw e;
        } finally {
            transactionInfo.setSuccessStatus(StatusType.COMMIT);
            doFinally(transactionInfo);
            TransactionManage.removeTransactionInfo();
        }
        return result;
    }

    @Override
    public void doFinally(TransactionInfo transactionInfo) throws SQLException {
        Iterator<Transaction> it = transactionInfo.getTransactions().iterator();
        while (it.hasNext()) {
            it.next().rollback();
        }
    }

    @Override
    public void doCommitTransactionAfterReturning(TransactionInfo transactionInfo) throws SQLException {
        Iterator<Transaction> it = transactionInfo.getTransactions().iterator();
        while (it.hasNext()) {
            it.next().commit();
        }
    }

    @Override
    public void doRollbackTransactionAfterThrowing(TransactionInfo transactionInfo, Throwable e) throws SQLException  {
        Iterator<Transaction> it = transactionInfo.getTransactions().iterator();
        while (it.hasNext()) {
            it.next().close();
        }
    }

}
