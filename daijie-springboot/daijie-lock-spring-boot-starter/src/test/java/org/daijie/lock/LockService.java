package org.daijie.lock;

import org.junit.Assert;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试锁
 * @author daijie
 * @since 2019/5/21
 */
@Service
public class LockService {
    //线程记数器
    private AtomicInteger runFrequency = new AtomicInteger(0);
    //定义一个不安全的公共数据，测试是否安全，只有成功获取锁时累加
    private int unsafeIncrement = 0;
    //获取锁成功的计数器，使用一个原子类的公共数据与之比较，如果用锁结果应该是一致，如果不用锁则反之
    private AtomicInteger successSafeIncrement = new AtomicInteger(0);
    //获取锁超时的计数器
    private AtomicInteger timeOutIncrement = new AtomicInteger(0);
    //获取锁异常的计数器
    private AtomicInteger errorIncrement = new AtomicInteger(0);

    /**
     * 初始化计数器
     */
    public void init() {
        unsafeIncrement = 0;
        runFrequency = new AtomicInteger(0);
        successSafeIncrement = new AtomicInteger(0);
        timeOutIncrement = new AtomicInteger(0);
        errorIncrement = new AtomicInteger(0);
    }

    /**
     * 累加
     */
    public void add() {
        runFrequency.incrementAndGet();
    }

    /**
     * 成功锁累加
     */
    public void addSucess() {
        add();
        unsafeIncrement ++;
        successSafeIncrement.incrementAndGet();
    }

    /**
     * 超时锁累加
     */
    public void addTimeOut() {
        add();
        timeOutIncrement.incrementAndGet();
    }

    /**
     * 异常锁累加
     */
    public void addError() {
        add();
        errorIncrement.incrementAndGet();
    }

    /**
     * 校验数据是不是达到要求
     * @param isLock 是否加锁
     * @param clientTotal 线程数
     */
    public void validate(boolean isLock, int clientTotal) {
        Assert.assertTrue(runFrequency.get() == clientTotal);
        if (!isLock) {
            Assert.assertNotEquals(unsafeIncrement, successSafeIncrement.get());
            Assert.assertTrue(unsafeIncrement < clientTotal);
            Assert.assertTrue(successSafeIncrement.get() == clientTotal);
        } else {
            Assert.assertEquals(unsafeIncrement, successSafeIncrement.get());
            Assert.assertTrue(successSafeIncrement.addAndGet(timeOutIncrement.addAndGet(errorIncrement.get())) == clientTotal);
        }
    }

    @Lock(argName = "id",
            timeout = 5000,
            errorMethodName = "org.daijie.lock.LockService.lockError",
            timeOutMethodName = "org.daijie.lock.LockService.lockTimeOut")
    public void andAnontationLockService(String id) {
        System.out.println(Thread.currentThread().getName() + ":进入获取锁成功处理方法.....");
        addSucess();
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(5)*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void lockError(String id) {
        System.out.println(Thread.currentThread().getName() + ":进入获取锁异常处理方法.....");
        addError();
    }

    public void lockTimeOut(String id) {
        System.out.println(Thread.currentThread().getName() + ":进入获取锁超时处理方法.....");
        addTimeOut();
    }

}
