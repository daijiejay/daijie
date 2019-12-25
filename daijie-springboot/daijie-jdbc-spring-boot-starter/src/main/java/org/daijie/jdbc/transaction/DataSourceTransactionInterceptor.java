package org.daijie.jdbc.transaction;

import org.aopalliance.intercept.MethodInvocation;
import org.daijie.jdbc.cache.CacheManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * 重写spring的事务拦截器
 * @author daijie_jay
 * @since 2019年11月10日
 */
public class DataSourceTransactionInterceptor extends TransactionInterceptor implements SpringManagedTransactionInterceptor {

    private static final Logger log = LoggerFactory.getLogger(DataSourceTransactionInterceptor.class);

    private org.daijie.jdbc.transaction.TransactionInfo transactionInfo;

    @Override
    public Object invoke(final MethodInvocation methodInvocation) throws Throwable {
        log.debug("{}方法开启事务", methodInvocation.getMethod());
        createTransaction(methodInvocation.getMethod());
        Object result = null;
        try {
            result = super.invoke(methodInvocation);
            log.debug("{}方法事务结束，提交事务", methodInvocation.getMethod());
            doCommitTransactionAfterReturning();
        }catch (Throwable e) {
            log.debug("{}方法事务异常，回滚事务", methodInvocation.getMethod());
            log.error("执行事务异常", e);
            doRollbackTransactionAfterThrowing(e);
            throw e;
        } finally {
            log.debug("{}方法事务结束，关闭连接", methodInvocation.getMethod());
            doFinally();
            TransactionManager.removeTransactionInfo();
        }
        return result;
    }

    @Override
    public void createTransaction(Method method) {
        this.transactionInfo = TransactionManager.createTransactionInfo(SpringManagedTransaction.class);
    }

    @Override
    public void doFinally() throws SQLException {
        if (this.transactionInfo != null) {
            this.transactionInfo.setSuccessStatus(StatusType.COMMIT);
            Iterator<Transaction> it = this.transactionInfo.getTransactions().iterator();
            while (it.hasNext()) {
                it.next().close();
            }
        }
    }

    @Override
    public void doCommitTransactionAfterReturning() throws SQLException {
        if (this.transactionInfo != null) {
            Iterator<Transaction> it = this.transactionInfo.getTransactions().iterator();
            while (it.hasNext()) {
                it.next().commit();
            }
            CacheManage.commit();
        }
    }

    @Override
    public void doRollbackTransactionAfterThrowing(Throwable e) throws SQLException  {
        if (this.transactionInfo != null) {
            Iterator<Transaction> it = this.transactionInfo.getTransactions().iterator();
            while (it.hasNext()) {
                it.next().rollback();
            }
            CacheManage.rollback();
        }
    }
}
