package org.daijie.core.lock;

import java.util.concurrent.TimeUnit;

/**
 * 锁业务类
 * @author daijie_jay
 * @date 2017年11月24日
 */
public interface DistributedReentrantLock {
	
	/**
	 * 验证锁是否存在
	 * @param timeout
	 * @param unit
	 * @return
	 * @throws InterruptedException
	 */
    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException;

    /**
     * 清除业务锁
     */
    public void unlock();
}
