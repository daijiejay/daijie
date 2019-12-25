package org.daijie.jdbc.transaction;

import java.lang.reflect.Method;

/**
 * 集成spring事务管理的拦截器
 * @author daijie
 * @since 2019/12/25
 */
public interface SpringManagedTransactionInterceptor extends TransactionInterceptor {

    /**
     * 创建事务信息
     * @param method 被拦截的业务方法
     */
    void createTransaction(Method method);
}
