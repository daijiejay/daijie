package org.daijie.jdbc.session;

import org.daijie.core.util.ClassInfoUtil;
import org.daijie.jdbc.executor.Executor;
import org.daijie.jdbc.executor.SqlExecutor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 操作数据库映射对象创建会话的管理类
 * @author daijie
 * @since 2019/5/31
 */
public class SessionMapperManage {

    /**
     * 操作数据库映射对象创建会话
     * @param sessionMapperClass 操作数据库映射对象的继承类类型
     * @param <T> 具体映射对象
     * @return Object 具体映射对象的继承类的实例
     */
    public static <T> T createSessionMapper(Class<T> sessionMapperClass) {
        return (T) Proxy.newProxyInstance(sessionMapperClass.getClassLoader(), new Class[]{sessionMapperClass}, new SessionMapperInvocationHandler(sessionMapperClass));
    }

    /**
     * 操作数据库映射对象动态代理创建实例
     */
    public static class SessionMapperInvocationHandler implements InvocationHandler {

        /**
         * 操作数据库映射对象的继承类类型
         */
        private final Class sessionMapperClass;

        /**
         * SQL执行器
         */
        private Executor executor;

        public SessionMapperInvocationHandler(Class<?> sessionMapperClass) {
            this.sessionMapperClass = sessionMapperClass;
        }

        /**
         * 获取mapper类泛型对象类型
         * @return 具体映射对象类型
         */
        private Class getEntityType() {
            return ClassInfoUtil.getSuperClassGenricType(sessionMapperClass);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            this.executor = new SqlExecutor(getEntityType(), method, args);
            return this.executor.execute();
        }
    }
}
