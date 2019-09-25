package org.daijie.jdbc.generator.config;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 文件生成器构建代理类
 * @author daijie
 * @since 2019/9/22
 */
public class FileConfigurationManager {

    /**
     * 创建一个FileConfiguration对象
     * @param target FileConfiguration具体实现对象类名
     * @param args 实例化类的构造参数
     * @param <T> FileConfiguration具体实现对象
     * @return 返回FileConfiguration具体实现对象
     */
    public static <T> T newInstance(Class<T> target, Object... args) {
        return new CGLibFileConfigurationInterceptor().newProxyInstance(target, args);
    }

    private static class CGLibFileConfigurationInterceptor implements MethodInterceptor {

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
            enhancer.setCallback(this);
            if (args.length > 0) {
                Class[] argTypes = new Class[args.length];
                int i = 0;
                for (Object object : args) {
                    argTypes[i++] = object.getClass();
                }
                return (T) enhancer.create(argTypes, args);
            }
            return (T) enhancer.create();
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            return methodProxy.invokeSuper(o, objects);
        }
    }

}
