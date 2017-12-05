package org.daijie.social.captcha;

import org.daijie.social.captcha.tx.TXCaptchaProperties;
import org.daijie.social.captcha.tx.TXCaptchaService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 第三方验证码自动装置
 * @author daijie_jay
 * @date 2017年11月28日
 */
@Configuration
@EnableConfigurationProperties({
	TXCaptchaProperties.class
	})
@Import({
	TXCaptchaService.class,
	})
public class SocialCaptchaBeanAutoConfiguration {

}
