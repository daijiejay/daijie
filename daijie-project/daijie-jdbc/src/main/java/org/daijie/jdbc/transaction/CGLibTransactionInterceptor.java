package org.daijie.jdbc.transaction;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.daijie.jdbc.cache.CacheManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * GCLib事务拦截器
 * @author daijie
 * @since 2019/6/9
 */
public class CGLibTransactionInterceptor implements MethodInterceptor, TransactionInterceptor {

    private static final Logger log = LoggerFactory.getLogger(CGLibTransactionInterceptor.class);

    public CGLibTransactionInterceptor() {}

    /**
     * 实例代理对象
     * @param target 代理对象Class
     * @param <T> 代理对象
     * @return 返回代理对象实例
     */
    public <T> T newProxyInstance(Class<T> target) {
        Enhancer enhancer  = new Enhancer();
        enhancer.setSuperclass(target);
        enhancer.setCallback(this);
        return (T) enhancer.create();
    }

    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        log.debug("{}方法开启事务", method);
        TransactionInfo transactionInfo = TransactionManage.createTransactionInfo();
        Object result = null;
        try {
            result = methodProxy.invokeSuper(object, args);
            doCommitTransactionAfterReturning(transactionInfo);
            if (TransactionManage.isTransaction()) {
                log.debug("{}方法事务结束，提交事务", method);
            }
        }catch (Throwable e) {
            doRollbackTransactionAfterThrowing(transactionInfo, e);
            if (TransactionManage.isTransaction()) {
                log.debug("{}方法事务异常，回滚事务", method);
                log.error("事务异常", e);
            }
            throw e;
        } finally {
            transactionInfo.setSuccessStatus(StatusType.COMMIT);
            doFinally(transactionInfo);
            TransactionManage.removeTransactionInfo();
            log.debug("{}方法事务结束，关闭连接", method);
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
