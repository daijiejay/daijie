package org.daijie.social.login.sina;

import org.daijie.social.login.LoginProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 新浪微博登录属性配置初始化
 * @author daijie_jay
 * @since 2017年11月28日
 */
@ConfigurationProperties("sina.login")
public class SinaLoignProperties extends LoginProperties {
	
}
