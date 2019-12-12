package org.daijie.test.listener;

import org.daijie.core.util.ClassLoaderUtil;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 注册单元测试监听器的管理类
 * @author daijie_jay
 * @since 2019年11月10日
 */
public class JunitRunListenerManager {

    private static Map<TestMethod, TestRunListener> listenerMap = new HashMap<>();

    public static void initRegisterRunListener() {
        Set<Class<?>> classes = ClassLoaderUtil.getClassesInPackage("");
        for (Class<?> klass : classes) {
            if (klass.isAssignableFrom(TestRunListener.class)) {

            }
        }
    }

    public static <T> T newProxyInstance(Class<T> target) {
        return new JunitRunListenerMethodInterceptor().newProxyInstance(target);
    }

    private static class JunitRunListenerMethodInterceptor implements MethodInterceptor {

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
            Object result = null;
            try {
                result = methodProxy.invokeSuper(object, args);
            } catch (Exception e) {

            }
            return result;
        }
    }

    enum TestMethod {
        testRunStarted,
        testRunFinished,
        testStarted,
        testFinished,
        testFailure,
        testAssumptionFailure,
        testIgnored,
        testSuiteStarted,
        testSuiteFinished
    }
}
