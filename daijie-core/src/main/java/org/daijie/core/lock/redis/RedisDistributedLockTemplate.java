package org.daijie.core.lock.redis;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.daijie.core.lock.Callback;
import org.daijie.core.lock.DistributedLockTemplate;

/**
 * redis分布式锁执行实现类
 * 启用redis分布式锁时，调用锁工具类执行
 * @author daijie_jay
 * @since 2017年11月24日
 */
public class RedisDistributedLockTemplate implements DistributedLockTemplate {
	
	private static final Logger logger = Logger.getLogger(RedisDistributedLockTemplate.class);

    private Object jedisLock;

    public RedisDistributedLockTemplate(Object jedisLock) {
        this.jedisLock = jedisLock;
    }

    public Object execute(String lockId, int timeout, Callback callback) {
        RedisReentrantLock distributedReentrantLock = null;
        boolean getLock=false;
        try {
            distributedReentrantLock = new RedisReentrantLock(jedisLock,lockId);
            if(distributedReentrantLock.tryLock(new Long(timeout), TimeUnit.MILLISECONDS)){
                getLock=true;
                return callback.onGetLock();
            }else{
                return callback.onTimeout();
            }
        }catch(InterruptedException ex){
        	logger.error(ex.getMessage(), ex);
            Thread.currentThread().interrupt();
        }catch (Exception e) {
        	logger.error(e.getMessage(), e);
        }finally {
            if(getLock) {
                distributedReentrantLock.unlock();
            }
        }
        return null;
    }
}
