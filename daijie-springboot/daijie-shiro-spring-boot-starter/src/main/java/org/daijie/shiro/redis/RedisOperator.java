package org.daijie.shiro.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * redis操作具体实现类
 * @author daijie
 * @since 2017年6月22日
 */
public class RedisOperator implements IRedisOperator {
	public final static Logger logger = LoggerFactory.getLogger(RedisOperator.class);

	private JedisCluster jedisCluster;

	@Override
	public TreeSet<String> keys(String pattern) throws Exception {
		logger.debug("Start getting keys...");
		TreeSet<String> keys = new TreeSet<String>();
		Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
		for(String k : clusterNodes.keySet()){
			logger.debug("Getting keys from: {}", k);
			JedisPool jp = clusterNodes.get(k);
			Jedis connection = jp.getResource();
			try {
				Set<String> set = connection.keys(pattern);
				Iterator<?> it = set.iterator();
				while (it.hasNext()) {
					keys.add((String) it.next());
				}
			} catch(Exception e){
				logger.error("Getting keys error: {}", e);
			} finally{
				logger.debug("Connection closed.");
				connection.close();//用完一定要close这个链接！！！
			}
		}
		logger.debug("Keys gotten!");
		return keys;
	}

	@Override
	public void flushDB() throws Exception {
		logger.debug("Start flushDb keys...");
		Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
		for(String k : clusterNodes.keySet()){
			logger.debug("delete keys from: {}", k);
			JedisPool jp = clusterNodes.get(k);
			Jedis connection = jp.getResource();
			try {
				connection.flushDB();
			} catch(Exception e){
				logger.error("Getting keys error: {}", e);
			} finally{
				logger.debug("Connection closed.");
				connection.close();//用完一定要close这个链接！！！
			}
		}
		logger.debug("flushDB");
	}

	public JedisCluster getJedisCluster() {
		return jedisCluster;
	}

	public void setJedisCluster(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}
}
