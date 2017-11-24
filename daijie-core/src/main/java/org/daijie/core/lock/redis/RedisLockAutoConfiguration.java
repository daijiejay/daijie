package org.daijie.core.lock.redis;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;
import org.daijie.core.lock.DistributedLockTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

@Configuration
@EnableConfigurationProperties({RedisLockProperties.class})
public class RedisLockAutoConfiguration {
	
	private static final Logger logger = Logger.getLogger(RedisLockAutoConfiguration.class);

	@Bean(name = "jedisLock")
	public Object jedisLock(RedisLockProperties lockZKProperties) {
		try {
			if(lockZKProperties.getAddresses().contains(",")){
				String[] serverArray = lockZKProperties.getAddresses().split(",");
				Set<HostAndPort> nodes = new HashSet<>();
				for (String ipPort : serverArray) {
					String[] ipPortPair = ipPort.split(":");
					nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
				}
				return new JedisCluster(nodes, 
						Protocol.DEFAULT_TIMEOUT, 
						Protocol.DEFAULT_TIMEOUT, 
						1, 
						lockZKProperties.getPassword(), 
						new GenericObjectPoolConfig());
			}else{
				JedisPool jedisPool = null;
				String[] local = lockZKProperties.getAddresses().split(":");
				if(StringUtils.isEmpty(lockZKProperties.getPassword())){
					jedisPool = new JedisPool(local[0].trim(), Integer.parseInt(local[1].trim()));
				}else{
					jedisPool = new JedisPool(new GenericObjectPoolConfig(), 
							local[0].trim(), 
							Integer.parseInt(local[1].trim()), 
							Protocol.DEFAULT_TIMEOUT,
							lockZKProperties.getPassword(),
					        Protocol.DEFAULT_DATABASE, 
					        null);
				}
				return jedisPool;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@Bean
	@ConditionalOnMissingBean
	public DistributedLockTemplate distributedLockTemplate(@Qualifier("jedisLock") Object jedisLock){
		return new RedisDistributedLockTemplate(jedisLock);
	}
}
