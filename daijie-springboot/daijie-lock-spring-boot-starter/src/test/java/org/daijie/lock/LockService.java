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
            errorMethodName = "org.daijie.lock.LockService.lockError(java.util.concurrent.atomic.AtomicInteger)",
            timeOutMethodName = "org.daijie.lock.LockService.lockTimeOut(java.util.concurrent.atomic.AtomicInteger)")
    public void anontationLockAdd(String id, int increment, AtomicInteger runFrequency, AtomicInteger successIncrement) {
        System.out.println(Thread.currentThread().getName() + ":进入获取锁成功处理方法.....");
        runFrequency.incrementAndGet();
        successIncrement.incrementAndGet();
        increment ++;
    }

    public void lockError(AtomicInteger errorFrequency) {
        errorFrequency.incrementAndGet();
        System.out.println(Thread.currentThread().getName() + ":进入获取锁异常处理方法.....");
    }

    public void lockTimeOut(AtomicInteger timeOutFrequency) {
        timeOutFrequency.incrementAndGet();
        System.out.println(Thread.currentThread().getName() + ":进入获取锁超时处理方法.....");
    }

}
