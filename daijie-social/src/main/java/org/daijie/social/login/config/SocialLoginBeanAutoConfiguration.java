package org.daijie.social.login.config;

import org.daijie.social.login.ali.AliLoginTool;
import org.daijie.social.login.baidu.BaiduLoginTool;
import org.daijie.social.login.qq.QQLoginTool;
import org.daijie.social.login.sina.SinaLoginTool;
import org.daijie.social.login.weixin.WeixinLoginTool;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝登录自动装置
 * @author daijie_jay
 * @since 2017年11月28日
 */
@Configuration
@ImportAutoConfiguration({
	AliLoginBeanAutoConfiguration.class, 
	BaiduLoginBeanAutoConfiguration.class,
	QQLoginBeanAutoConfiguration.class,
	SinaLoginBeanAutoConfiguration.class,
	WeixinLoginBeanAutoConfiguration.class
	})
@AutoConfigureAfter({
	AliLoginTool.class,
	BaiduLoginTool.class,
	QQLoginTool.class,
	SinaLoginTool.class,
	WeixinLoginTool.class,
	})
public class SocialLoginBeanAutoConfiguration {

}
