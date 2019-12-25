package org.daijie.jdbc.transaction;

import org.daijie.jdbc.cache.CacheManage;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * 事务处理
 * @author daijie_jay
 * @since 2019年11月10日
 */
public abstract class AbstractTransactionInterceptor implements TransactionInterceptor {

    private TransactionInfo transactionInfo;

    /**
     * 如果方法和类上声明org.daijie.jdbc.transaction.@Transactional将创建事务
     * @param method 事务前的方法
     */
    public void createTransaction(Method method) {
        if (method.isAnnotationPresent(Transactional.class) || method.getDeclaringClass().isAnnotationPresent(Transactional.class)) {
            this.transactionInfo = TransactionManager.createTransactionInfo(DataSourceTransaction.class);
        }
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
