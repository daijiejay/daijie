package org.daijie.shiro.redis;

public class BaseRedisManagerFactory {

	private RedisManager clusterRedisManager;
	
	private org.crazycake.shiro.RedisManager SingleRedisManager;

	public RedisManager getClusterRedisManager() {
		return clusterRedisManager;
	}

	public void setClusterRedisManager(RedisManager clusterRedisManager) {
		this.clusterRedisManager = clusterRedisManager;
	}

	public org.crazycake.shiro.RedisManager getSingleRedisManager() {
		return SingleRedisManager;
	}

	public void setSingleRedisManager(
			org.crazycake.shiro.RedisManager singleRedisManager) {
		SingleRedisManager = singleRedisManager;
	}
	
}
