package org.daijie.shiro.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * shiro集成redis的属性配置
 * @author daijie_jay
 * @since 2018年1月2日
 */
@ConfigurationProperties(prefix = "shiro.redis")
public class ShiroRedisProperties {

	private Integer timeout = 5000;
	
	private Integer connectionTimeout = 5000;
	
	private Integer maxAttempts = 1;
	
	private Integer expire = 360000;
	
	private String address;
	
	private String password;
	
	private Boolean cluster;

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public Integer getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(Integer connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public Integer getMaxAttempts() {
		return maxAttempts;
	}

	public void setMaxAttempts(Integer maxAttempts) {
		this.maxAttempts = maxAttempts;
	}

	public Integer getExpire() {
		return expire;
	}

	public void setExpire(Integer expire) {
		this.expire = expire;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getCluster() {
		return cluster;
	}

	public void setCluster(Boolean cluster) {
		this.cluster = cluster;
	}
}
