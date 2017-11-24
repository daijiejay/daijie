package org.daijie.core.lock.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.daijie.core.lock.DistributedLockTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ZKLockProperties.class})
public class ZKLockAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public CuratorFramework curatorFramework(ZKLockProperties lockZKProperties) {
		CuratorFramework CuratorFramework = CuratorFrameworkFactory.newClient(lockZKProperties.getAddresses(), 
				new ExponentialBackoffRetry(lockZKProperties.getBaseSleepTimeMs(), lockZKProperties.getMaxRetries()));
		return CuratorFramework;
	}
	
	@Bean
	@ConditionalOnMissingBean
	public DistributedLockTemplate distributedLockTemplate(CuratorFramework curatorFramework){
		curatorFramework.start();
		return new ZkDistributedLockTemplate(curatorFramework);
	}
}
