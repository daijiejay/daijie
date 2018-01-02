package org.daijie.social.login.config;

import org.daijie.social.login.ali.AliLoignProperties;
import org.daijie.social.login.ali.callback.AliLoginCallbackController;
import org.daijie.social.login.ali.service.AliLoginService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝登录自动装置
 * @author daijie_jay
 * @since 2017年11月28日
 */
@Configuration
@EnableConfigurationProperties({AliLoignProperties.class})
public class AliLoginBeanAutoConfiguration {

	@Bean("aliLoginService")
    public AliLoginService loginService() {
		return new AliLoginService();
	}
	
	@Bean
	public AliLoginCallbackController loginCallbackController() {
		return new AliLoginCallbackController();
	}
}
