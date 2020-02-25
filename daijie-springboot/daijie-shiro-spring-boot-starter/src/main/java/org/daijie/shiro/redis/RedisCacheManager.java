package org.daijie.shiro.redis;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 重写
 * @author daijie
 * @since 2017年6月22日
 */
public class RedisCacheManager implements CacheManager {

	private static final Logger logger = LoggerFactory
			.getLogger(RedisCacheManager.class);

	@SuppressWarnings("rawtypes")
	private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();

	private ClusterRedisManager redisManager;

	private String keyPrefix = "shiro_redis_cache:";
	
	/**
	 * @return String
	 */
	public String getKeyPrefix() {
		return keyPrefix;
	}

	/**
	 * @param keyPrefix 前缀名
	 */
	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		logger.debug("获取名称为: " + name + " 的RedisCache实例");
		Cache<K, V> c = caches.get(name);
		if (c == null) {
			redisManager.init();
			c = new RedisCache<K, V>(redisManager, keyPrefix);
			caches.put(name, c);
		}
		return c;
	}

	public ClusterRedisManager getRedisManager() {
		return redisManager;
	}

	public void setRedisManager(ClusterRedisManager redisManager) {
		this.redisManager = redisManager;
	}

}
