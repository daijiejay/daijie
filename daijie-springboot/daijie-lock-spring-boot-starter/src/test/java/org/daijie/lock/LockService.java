package org.daijie.lock;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试注解锁添加的类
 * @author daijie
 * @since 2019/5/21
 */
@Service
public class LockService {

    @Lock(argName = "id",
            timeout = 5000,
            errorMethodName = "org.daijie.lock.RedisLockAnontationSpringBootTest.lockError(java.lang.String)",
            timeOutMethodName = "org.daijie.lock.RedisLockAnontationSpringBootTest.lockTimeOut(java.lang.String)")
    public void anontationLockAdd(int increment, AtomicInteger runFrequency) {
        System.out.println(Thread.currentThread().getName() + ":进入获取锁成功处理方法.....");
        runFrequency.incrementAndGet();
        increment ++;
    }

    public void lockError(String id) {
        System.out.println(Thread.currentThread().getName() + ":进入获取锁异常处理方法.....");
    }

    public void lockTimeOut(String id) {
        System.out.println(Thread.currentThread().getName() + ":进入获取锁超时处理方法.....");
    }

}
