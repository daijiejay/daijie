package org.daijie.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @author daijie
 * @since 2019/5/14
 */
public class LockInterceptor implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LockInterceptor.class);

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        logger.debug("进入分布式锁，执行方法{}", method.toGenericString());
        Class<?>[] parameterNames = method.getParameterTypes();
        Lock lock = method.getAnnotation(Lock.class);
        if(lock == null){
            lock = o.getClass().getAnnotation(Lock.class);
        }
        String lockId = lock.lockId();
        if(StringUtils.isEmpty(lockId)){
            if(!StringUtils.isEmpty(lock.argName())){
                for (int i = 0; i < objects.length; i++) {
                    if(parameterNames[i].getName().contains(lock.argName())){
                        lockId = (String) objects[i];
                    }
                }
            }else{
                lockId = method.getName();
            }
        }
        tryLock(lockId, lock);
        return methodProxy.invokeSuper(o, objects);
    }

    /**
     * 获取锁
     * @param lockId 锁业务ID
     * @param lock @Lock注解
     */
    private void tryLock(String lockId, Lock lock){
        LockTool.execute(lockId, lock.timeout(), new Callback() {
            @Override
            public Object onTimeout() throws InterruptedException {
                String timeOutMethodName = lock.timeOutMethodName();
                if(timeOutMethodName != null){
                    execute(timeOutMethodName);
                }
                return false;
            }
            @Override
            public Object onGetLock() throws InterruptedException {
                return true;
            }
            @Override
            public Object onError(Exception exception){
                String errorMethodName = lock.errorMethodName();
                if(errorMethodName != null){
                    execute(errorMethodName);
                }
                return false;
            }
        });
    }

    /**
     * 获取锁异常时执行的方法
     * @param serviceName 方法名
     */
    private void execute(String serviceName){
        String str1 = serviceName.substring(0, serviceName.indexOf("("));
        String className = str1.substring(0, str1.lastIndexOf("."));
        String methodName = str1.substring(str1.lastIndexOf(".")+1);
        String args = serviceName.substring(serviceName.lastIndexOf("(")+1, serviceName.indexOf(")"));
        try {
            Object result = null;
            Class<?> clz = Class.forName(className);
            if(StringUtils.isEmpty(args)){
                Method method = clz.getDeclaredMethod(methodName);
                result = method.invoke(clz.newInstance());
            }else{
                String[] argNames = args.split(",");
                Object[] argObjects = new Object[argNames.length];
                Class<?>[] argClasses = new Class[argNames.length];
                for (int i = 0; i < argClasses.length; i++) {
                    if(argNames[i].trim().length() > 0){
                        argClasses[i] = Class.forName(argNames[i].trim());
                        argObjects[i] = argClasses[i].newInstance();
                    }
                }
                Method method = clz.getDeclaredMethod(methodName, argClasses);
                result = method.invoke(clz.newInstance(), argObjects);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
