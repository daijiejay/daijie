package org.daijie.core.lock;

import java.util.concurrent.TimeUnit;

/**
 * 对锁的查询添加删除操作
 * @author daijie_jay
 * @since 2017年11月24日
 */
public interface DistributedReentrantLock {
	
	/**
	 * 验证锁是否存在
	 * @param timeout 超时时间
	 * @param unit unit
	 * @return boolean 是否存在
	 * @throws InterruptedException 抛出异常
	 */
    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException;

    /**
     * 清除业务锁
     */
    public void unlock();
}
