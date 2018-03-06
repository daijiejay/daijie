package org.daijie.core.lock.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * zookeeper存储序列
 * @author daijie_jay
 * @since 2017年11月24日
 */
public class ZkDistributedSequence implements DistributedSequence {

	private static final Logger logger = LoggerFactory.getLogger(ZkReentrantLockCleanerTask.class);

	private CuratorFramework client;
	
	private int maxRetries=3;
	
	private final int baseSleepTimeMs=1000;

	public ZkDistributedSequence(String zookeeperAddress){
		try{
			RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
			client = CuratorFrameworkFactory.newClient(zookeeperAddress, retryPolicy);
			client.start();
		}catch (Exception e){
			logger.error(e.getMessage(),e);
		}catch (Throwable ex){
			ex.printStackTrace();
			logger.error(ex.getMessage(),ex);
		}
	}

	public int getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	public int getBaseSleepTimeMs() {
		return baseSleepTimeMs;
	}

	@Override
	public Long sequence(String sequenceName) {
		try {
			int value=client.setData().withVersion(-1).forPath("/"+sequenceName,"".getBytes()).getVersion();
			return new Long(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
