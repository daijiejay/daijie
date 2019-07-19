package org.daijie.jdbc.transaction;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * @author daijie
 * @since 2019/6/3
 */
public class JDKTransactionMethodInvocation implements MethodInvocation {
    protected Object[] arguments;

    protected final Object target;

    protected final Method method;

    Advice interceptorsAndDynamicMethodMatchers;

    private int currentInterceptorIndex = -1;

    public JDKTransactionMethodInvocation(Object[] arguments, Object target, Method method, Advice interceptorsAndDynamicMethodMatchers) {
        this.arguments = arguments;
        this.target = target;
        this.method = method;
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override
    public AccessibleObject getStaticPart() {
        return this.method;
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    @Override
    public Object proceed() throws Throwable {
        if(++currentInterceptorIndex == 1) {
            return this.method.invoke(this.target, this.arguments);
        }
        return ((MethodInterceptor) interceptorsAndDynamicMethodMatchers).invoke(this);
    }

    @Override
    public Method getMethod() {
        return this.method;
    }
}
