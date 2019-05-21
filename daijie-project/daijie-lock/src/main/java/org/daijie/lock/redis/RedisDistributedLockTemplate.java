package org.daijie.lock.redis;

import org.daijie.lock.Callback;
import org.daijie.lock.DistributedLockTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁执行实现类
 * 锁底层是evel命令实现，redis服务需要是3.0以上版本
 * @author daijie_jay
 * @since 2017年11月24日
 */
public class RedisDistributedLockTemplate implements DistributedLockTemplate {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisDistributedLockTemplate.class);

    private Object jedisLock;

    /**
     * 可使用JedisPool来实例单机的redis服务，也可以使用JedisCluster够实例集群的redis服务
     * @param jedisLock redis客户端
     */
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
        }catch (Exception e) {
        	if(e instanceof InterruptedException){
        		Thread.currentThread().interrupt();
        	}
        	logger.debug(e.getMessage(), e);
            return callback.onError(e);
        }finally {
            if(getLock) {
                distributedReentrantLock.unlock();
            }
        }
    }
}
