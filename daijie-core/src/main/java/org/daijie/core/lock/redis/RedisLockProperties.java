package org.daijie.core.lock.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redis锁相关配置
 * @author daijie_jay
 * @date 2017年11月24日
 */
@ConfigurationProperties("lock.redis")
public class RedisLockProperties {
	
	private Boolean redisZkEnable = false;
	
	private String addresses = "127.0.0.1:6379";
	
	private String password = "";

	public Boolean getRedisZkEnable() {
		return redisZkEnable;
	}

	public void setRedisZkEnable(Boolean redisZkEnable) {
		this.redisZkEnable = redisZkEnable;
	}

	public String getAddresses() {
		return addresses;
	}

	public void setAddresses(String addresses) {
		this.addresses = addresses;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
