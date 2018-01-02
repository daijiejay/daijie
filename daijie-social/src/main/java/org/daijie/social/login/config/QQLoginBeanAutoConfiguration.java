package org.daijie.social.login.config;

import org.daijie.social.login.qq.QQLoignProperties;
import org.daijie.social.login.qq.callback.QQLoginCallbackController;
import org.daijie.social.login.qq.service.QQLoginService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 百度登录自动装置
 * @author daijie_jay
 * @since 2017年11月28日
 */
@Configuration
@EnableConfigurationProperties({QQLoignProperties.class})
public class QQLoginBeanAutoConfiguration {

	@Bean("qqLoginService")
    public QQLoginService loginService() {
		return new QQLoginService();
	}
	
	@Bean
	public QQLoginCallbackController loginCallbackController() {
		return new QQLoginCallbackController();
	}
}
