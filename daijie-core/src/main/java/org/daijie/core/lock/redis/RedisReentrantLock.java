package org.daijie.core.lock.redis;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.daijie.core.lock.DistributedReentrantLock;

import com.google.common.collect.Maps;

/**
 * redis锁操作查询添加删除的实现类
 * @author daijie_jay
 * @since 2017年11月24日
 */
public class RedisReentrantLock implements DistributedReentrantLock {

    private final ConcurrentMap<Thread, LockData> threadData = Maps.newConcurrentMap();

    private RedisLockInternals internals;

    private String lockId;


    public RedisReentrantLock(Object jedisLock,String lockId) {
        this.lockId=lockId;
        this.internals=new RedisLockInternals(jedisLock);
    }

    private static class LockData {
        final String lockVal;
        final AtomicInteger lockCount = new AtomicInteger(1);

        private LockData(Thread owningThread, String lockVal) {
            this.lockVal = lockVal;
        }
    }

    @Override
    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException{
        Thread currentThread = Thread.currentThread();
        LockData lockData = threadData.get(currentThread);
        if ( lockData != null ) {
            lockData.lockCount.incrementAndGet();
            return true;
        }
        String lockVal = internals.tryRedisLock(lockId,timeout,unit);
        if ( lockVal != null ) {
            LockData newLockData = new LockData(currentThread, lockVal);
            threadData.put(currentThread, newLockData);
            return true;
        }
        return false;
    }

    @Override
    public void unlock() {
        Thread currentThread = Thread.currentThread();
        LockData lockData = threadData.get(currentThread);
        if ( lockData == null ) {
            throw new IllegalMonitorStateException("You do not own the lock: " + lockId);
        }
        int newLockCount = lockData.lockCount.decrementAndGet();
        if ( newLockCount > 0 ) {
            return;
        }
        if ( newLockCount < 0 ) {
            throw new IllegalMonitorStateException("Lock count has gone negative for lock: " + lockId);
        }
        try {
            internals.unlockRedisLock(lockId,lockData.lockVal);
        } finally {
            threadData.remove(currentThread);
        }
    }
}
