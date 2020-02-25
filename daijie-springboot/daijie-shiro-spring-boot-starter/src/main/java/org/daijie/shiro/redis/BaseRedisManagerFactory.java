package org.daijie.shiro.redis;

public class BaseRedisManagerFactory implements RedisManagerFactory {

	private ClusterRedisManager clusterRedisManager;
	
	private SingleRedisManager SingleRedisManager;

	@Override
	public ClusterRedisManager getClusterRedisManager() {
		return clusterRedisManager;
	}

	public void setClusterRedisManager(ClusterRedisManager clusterRedisManager) {
		this.clusterRedisManager = clusterRedisManager;
	}

	@Override
	public SingleRedisManager getSingleRedisManager() {
		return SingleRedisManager;
	}

	public void setSingleRedisManager(SingleRedisManager singleRedisManager) {
		SingleRedisManager = singleRedisManager;
	}
	
}
