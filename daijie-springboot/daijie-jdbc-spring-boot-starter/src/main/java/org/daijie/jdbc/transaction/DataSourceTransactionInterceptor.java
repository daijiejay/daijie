package org.daijie.jdbc.transaction;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * 重写spring的事务拦截器
 * @author daijie_jay
 * @since 2019年11月10日
 */
public class DataSourceTransactionInterceptor extends TransactionInterceptor {

    private static final Logger log = LoggerFactory.getLogger(DataSourceTransactionInterceptor.class);

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        Object result = null;
        try {
            Object object = super.invoke(invocation);
        }catch (Throwable e) {
            throw e;
        } finally {
        }
        return result;
    }
}
