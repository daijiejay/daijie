package org.daijie.jdbc.transaction;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK代理实例对象实现
 * @author daijie
 * @since 2019/7/17
 */
public class JDKTransactionInvocationHandler implements InvocationHandler {
    private final Logger log = LoggerFactory.getLogger(JDKTransactionInvocationHandler.class);
    /**
     * 代理类的具体实现类
     */
    private Object target;
    /**
     * 拦截器
     */
    private Advice advisor;

    public JDKTransactionInvocationHandler() {
        this.advisor = new JDKTransactionInterceptor();
    }

    /**
     * 实例代理对象
     * @param targetClass 代理对象
     * @param <T> 代理对象
     * @return 返回代理对象的接口类
     */
    public <T> T newProxyInstance(Class<T> targetClass) {
        try {
            this.target = targetClass.newInstance();
            return (T) Proxy.newProxyInstance(targetClass.getClassLoader(), getMethodInterceptor(targetClass), this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodInvocation methodInvocation = new JDKTransactionMethodInvocation(args, target, method, this.advisor);
        return methodInvocation.proceed();
    }

    private Class[] getMethodInterceptor(Class<?> targetClass) {
        return targetClass.getInterfaces();
    }
}
