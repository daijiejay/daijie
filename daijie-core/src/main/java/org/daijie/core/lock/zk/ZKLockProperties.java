package org.daijie.core.lock.zk;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * zookeeper锁相关属性配置
 * @author daijie_jay
 * @since 2017年11月24日
 */
@ConfigurationProperties("lock.zk")
public class ZKLockProperties {
	
	private Boolean lockZkEnable = false;
	
	private String addresses = "127.0.0.1:2181";
	
	private Integer baseSleepTimeMs = 1000;
	
	private Integer maxRetries = 3;

	public Boolean getLockZkEnable() {
		return lockZkEnable;
	}

	public void setLockZkEnable(Boolean lockZkEnable) {
		this.lockZkEnable = lockZkEnable;
	}

	public String getAddresses() {
		return addresses;
	}

	public void setAddresses(String addresses) {
		this.addresses = addresses;
	}

	public Integer getBaseSleepTimeMs() {
		return baseSleepTimeMs;
	}

	public void setBaseSleepTimeMs(Integer baseSleepTimeMs) {
		this.baseSleepTimeMs = baseSleepTimeMs;
	}

	public Integer getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(Integer maxRetries) {
		this.maxRetries = maxRetries;
	}
}
