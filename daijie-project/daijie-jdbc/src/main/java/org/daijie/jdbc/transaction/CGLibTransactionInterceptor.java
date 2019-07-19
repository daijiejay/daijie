package org.daijie.jdbc.transaction;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.daijie.jdbc.cache.CacheManage;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * GCLib事务拦截器
 * @author daijie
 * @since 2019/6/9
 */
public class CGLibTransactionInterceptor implements MethodInterceptor, TransactionInterceptor {

    public CGLibTransactionInterceptor() {}

    /**
     * 实例代理对象
     * @param target 代理对象Class
     * @param args 构造参数
     * @param <T> 代理对象
     * @return 返回代理对象实例
     */
    public <T> T newProxyInstance(Class<T> target, Object... args) {
        Enhancer enhancer  = new Enhancer();
        enhancer.setSuperclass(target);
        if (args.length > 0) {
            Class[] argTypes = new Class[args.length];
            int i = 0;
            for (Object object : args) {
                argTypes[i++] = object.getClass();
            }
            enhancer.create(argTypes, args);
        }
        enhancer.setCallback(this);
        return (T) enhancer.create();
    }

    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        TransactionInfo transactionInfo = TransactionManage.createTransactionInfo();
        Object result = null;
        try {
            result = methodProxy.invokeSuper(object, args);
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
