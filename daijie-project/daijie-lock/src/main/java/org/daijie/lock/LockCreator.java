package org.daijie.lock;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理构建具体类执行方法锁
 * @author daijie
 * @since 2019/5/9
 */
public class LockCreator {

    private LockHandler lockHandler;

    public LockCreator(DistributedLockTemplate template) {
        this.lockHandler = new LockCreator.LockHandler(template);;
    }

    /**
     * 代理执行方法
     * @param lockId 锁id(对应业务唯一ID)
     * @param timeout 设置需要锁住的时间（算法需要执行的时间，执行完成后自动放开锁），单位毫秒
     * @param callback 回调函数
     * @return Object 回调函数方法中定义的返回类型
     */
    public Object invoke(String lockId, int timeout, Callback callback) {
        DistributedLockTemplate proxy= (DistributedLockTemplate) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), this.lockHandler.getProxyClass().getInterfaces(), this.lockHandler);
        return proxy.execute(lockId, timeout, callback);
    }

    /**
     * 动态代理到具体的锁实现类
     * @author daijie
     * @since 2019/5/9
     */
    public static class LockHandler implements InvocationHandler {

        private DistributedLockTemplate template;

        public LockHandler(DistributedLockTemplate template) {
            this.template = template;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(template,args);
        }

        /**
         * 获取具体实现类名
         * @return Class 具体实现类的class
         */
        public Class<DistributedLockTemplate> getProxyClass() {
            return (Class<DistributedLockTemplate>) this.template.getClass();
        }
    }
}
