package org.daijie.social.captcha.tx;

import org.daijie.social.login.ali.AliLoignProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝登录自动装置
 * @author daijie_jay
 * @date 2017年11月28日
 */
@Configuration
@EnableConfigurationProperties({AliLoignProperties.class})
public class TXCaptchaBeanAutoConfiguration {

}
