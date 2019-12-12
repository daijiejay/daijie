package org.daijie.jdbc.transaction;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * GCLib事务拦截器
 * @author daijie
 * @since 2019/6/9
 */
public class CGLibTransactionInterceptor extends AbstractTransactionInterceptor implements MethodInterceptor {

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
        createTransaction(method);
        Object result = null;
        try {
            result = methodProxy.invokeSuper(object, args);
            doCommitTransactionAfterReturning();
            if (TransactionManager.isTransaction()) {
                log.debug("{}方法事务结束，提交事务", method);
            }
        }catch (Throwable e) {
            doRollbackTransactionAfterThrowing(e);
            if (TransactionManager.isTransaction()) {
                log.debug("{}方法事务异常，回滚事务", method);
                log.error("事务异常", e);
            }
            throw e;
        } finally {
            doFinally();
            TransactionManager.removeTransactionInfo();
            log.debug("{}方法事务结束，关闭连接", method);
        }
        return result;
    }
}
