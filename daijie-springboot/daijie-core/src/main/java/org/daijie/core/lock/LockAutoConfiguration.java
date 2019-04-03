package org.daijie.core.lock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 锁相关bean配置
 * @author daijie_jay
 * @since 2018年3月9日
 */
@Configuration
public class LockAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public LockHardler lockHardler(){
		return new LockHardler();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public LockAdvisorAutoProxyCreator lockAdvisorAutoProxyCreator(){
		return new LockAdvisorAutoProxyCreator();
	}
}
