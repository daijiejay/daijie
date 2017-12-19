package org.daijie.shiro.redis;

public interface RedisManagerFactory {

	public RedisManager getClusterRedisManager();
	
	public org.crazycake.shiro.RedisManager getSingleRedisManager();
}
