package org.daijie.social.login.qq;

import org.daijie.social.login.LoginProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * QQ登录属性配置初始化
 * @author daijie_jay
 * @since 2017年11月28日
 */
@ConfigurationProperties("qq.login")
public class QQLoignProperties extends LoginProperties {
	
}
