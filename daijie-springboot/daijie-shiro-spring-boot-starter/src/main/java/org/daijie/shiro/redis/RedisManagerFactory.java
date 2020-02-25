package org.daijie.shiro.redis;

/**
 * redis管理工具工厂
 * @author daijie_jay
 * @since 2018年1月2日
 */
public interface RedisManagerFactory {

	/**
	 * 获取集群redis管理工具
	 * @return RedisManager
	 */
	public ClusterRedisManager getClusterRedisManager();
	
	/**
	 * 获取单机redis管理工具
	 * @return RedisManager
	 */
	public SingleRedisManager getSingleRedisManager();
}
