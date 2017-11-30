package org.daijie.social.login.config;

import org.daijie.social.login.baidu.BaiduLoignProperties;
import org.daijie.social.login.baidu.callback.BaiduLoginCallbackController;
import org.daijie.social.login.baidu.service.BaiduLoginService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 百度登录自动装置
 * @author daijie_jay
 * @date 2017年11月28日
 */
@Configuration
@EnableConfigurationProperties({BaiduLoignProperties.class})
public class BaiduLoginBeanAutoConfiguration {

	@Bean
    public BaiduLoginService loginService() {
		return new BaiduLoginService();
	}
	
	@Bean
	public BaiduLoginCallbackController loginCallbackController() {
		return new BaiduLoginCallbackController();
	}
}
