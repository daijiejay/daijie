package org.daijie.social.login.config;

import org.daijie.social.login.weixin.WeixinLoignProperties;
import org.daijie.social.login.weixin.callback.WeixinLoginCallbackController;
import org.daijie.social.login.weixin.service.WeixinLoginService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信登录自动装置
 * @author daijie_jay
 * @date 2017年11月28日
 */
@Configuration
@EnableConfigurationProperties({WeixinLoignProperties.class})
public class WeixinLoginBeanAutoConfiguration {

	@Bean
    public WeixinLoginService loginService() {
		return new WeixinLoginService();
	}
	
	@Bean
	public WeixinLoginCallbackController loginCallbackController() {
		return new WeixinLoginCallbackController();
	}
}
