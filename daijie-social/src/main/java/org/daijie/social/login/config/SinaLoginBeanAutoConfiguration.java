package org.daijie.social.login.config;

import org.daijie.social.login.sina.SinaLoignProperties;
import org.daijie.social.login.sina.callback.SinaLoginCallbackController;
import org.daijie.social.login.sina.service.SinaLoginService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 新浪微博登录自动装置
 * @author daijie_jay
 * @date 2017年11月28日
 */
@Configuration
@EnableConfigurationProperties({SinaLoignProperties.class})
public class SinaLoginBeanAutoConfiguration {

	@Bean("sinaLoginService")
    public SinaLoginService loginService() {
		return new SinaLoginService();
	}
	
	@Bean
	public SinaLoginCallbackController loginCallbackController() {
		return new SinaLoginCallbackController();
	}
}
