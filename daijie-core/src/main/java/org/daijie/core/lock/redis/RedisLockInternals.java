package org.daijie.core.lock.redis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * redis锁服务操作
 * @author daijie_jay
 * @since 2017年11月24日
 */
class RedisLockInternals {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisLockInternals.class);
	
    private JedisPool jedisPool;
    
    private JedisCluster jedisCluster;

    /**
     * 重试等待时间
     */
    private int retryAwait=300;

    private int lockTimeout=2000;

    /**
     * 实例redis客服端工具，通过获取bean时判断是单机还是集群配置
     * @param jedisLock
     */
    RedisLockInternals(Object jedisLock) {
    	if(jedisLock instanceof JedisPool){
    		this.jedisPool = (JedisPool) jedisLock;
    	}else if(jedisLock instanceof JedisCluster){
    		this.jedisCluster = (JedisCluster) jedisLock;
    	}
    }

    /**
     * 加入锁
     * @param lockId 业务锁ID
     * @param time 业务执行时间
     * @param unit 锁规则配置
     * @return String
     */
    String tryRedisLock(String lockId,long time, TimeUnit unit) {
        final long startMillis = System.currentTimeMillis();
        final Long millisToWait = (unit != null) ? unit.toMillis(time) : null;
        String lockValue=null;
        while (lockValue==null){
            lockValue=createRedisKey(lockId);
            if(lockValue!=null){
                break;
            }
            if(System.currentTimeMillis()-startMillis-retryAwait>millisToWait){
                break;
            }
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(retryAwait));
        }
        return lockValue;
    }

    /**
     * 通过eval命令创建redis锁
     * @param lockId 业务锁ID
     * @return String
     */
    private String createRedisKey(String lockId) {
        String value=lockId+randomId(1);
        String luaScript = ""
                + "\nlocal r = tonumber(redis.call('SETNX', KEYS[1],ARGV[1]));"
                + "\nredis.call('PEXPIRE',KEYS[1],ARGV[2]);"
                + "\nreturn r";
        List<String> keys = new ArrayList<String>();
        keys.add(lockId);
        List<String> args = new ArrayList<String>();
        args.add(value);
        args.add(lockTimeout+"");
        Long ret = eval(luaScript, keys, args);
        if( new Long(1).equals(ret)){
            return value;
        }
        return null;
    }

    /**
     * 销毁锁
     * @param key
     * @param value
     */
    void unlockRedisLock(String key,String value) {
        String luaScript=""
                +"\nlocal v = redis.call('GET', KEYS[1]);"
                +"\nlocal r= 0;"
                +"\nif v == ARGV[1] then"
                +"\nr =redis.call('DEL',KEYS[1]);"
                +"\nend"
                +"\nreturn r";
        List<String> keys = new ArrayList<String>();
        keys.add(key);
        List<String> args = new ArrayList<String>();
        args.add(value);
        eval(luaScript, keys, args);
    }

    private final static char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
            'Z'};

    private String randomId(int size) {
        char[] cs = new char[size];
        for (int i = 0; i < cs.length; i++) {
            cs[i] = digits[ThreadLocalRandom.current().nextInt(digits.length)];
        }
        return new String(cs);
    }
    
    /**
     * 执行eval脚本
     * @param script 命令脚本
     * @param keys 键集
     * @param args 值集
     * @return
     */
	private Long eval(String script, List<String> keys, List<String> args){
    	if(jedisPool != null){
    		Jedis jedis = null;
    		try {
    			jedis = jedisPool.getResource();
    			return (Long) jedis.eval(script, keys, args);
    		}finally {
    			if(jedis!=null) jedis.close();
    		}
    	}
    	if(jedisCluster != null){
    		try {
    			return (Long) jedisCluster.eval(script, keys, args);
    		}finally {
    			if(jedisCluster!=null){
					try {
						jedisCluster.close();
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
    			}
    		}
    	}
    	return (long) 0;
    }
}
