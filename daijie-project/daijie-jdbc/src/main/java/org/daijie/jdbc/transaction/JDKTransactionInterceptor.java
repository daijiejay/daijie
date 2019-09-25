package org.daijie.jdbc.transaction;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.daijie.jdbc.cache.CacheManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Iterator;

/**
 * JDK代理事务拦截器
 * @author daijie
 * @since 2019/6/3
 */
public class JDKTransactionInterceptor implements MethodInterceptor, TransactionInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JDKTransactionInterceptor.class);

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        log.info("{}方法开启事务", methodInvocation.getMethod());
        TransactionInfo transactionInfo = TransactionManage.createTransactionInfo();
        Object result = null;
        try {
            result = methodInvocation.proceed();
            log.info("{}方法事务结束，提交事务", methodInvocation.getMethod());
            doCommitTransactionAfterReturning(transactionInfo);
        }catch (Throwable e) {
            log.info("{}方法事务异常，回滚事务", methodInvocation.getMethod());
            log.error("执行事务异常", e);
            doRollbackTransactionAfterThrowing(transactionInfo, e);
            throw e;
        } finally {
            log.info("{}方法事务结束，关闭连接", methodInvocation.getMethod());
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
            it.next().close();
        }
    }

    @Override
    public void doCommitTransactionAfterReturning(TransactionInfo transactionInfo) throws SQLException {
        Iterator<Transaction> it = transactionInfo.getTransactions().iterator();
        while (it.hasNext()) {
            it.next().commit();
        }
        CacheManage.commit();
    }

    @Override
    public void doRollbackTransactionAfterThrowing(TransactionInfo transactionInfo, Throwable e) throws SQLException  {
        Iterator<Transaction> it = transactionInfo.getTransactions().iterator();
        while (it.hasNext()) {
            it.next().rollback();
        }
        CacheManage.rollback();
    }

}
