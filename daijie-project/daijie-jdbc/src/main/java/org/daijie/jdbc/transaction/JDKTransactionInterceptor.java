package org.daijie.jdbc.transaction;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JDK代理事务拦截器
 * @author daijie
 * @since 2019/6/3
 */
public class JDKTransactionInterceptor extends AbstractTransactionInterceptor implements MethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JDKTransactionInterceptor.class);

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        log.debug("{}方法开启事务", methodInvocation.getMethod());
        createTransaction(methodInvocation.getMethod());
        Object result = null;
        try {
            result = methodInvocation.proceed();
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
}
