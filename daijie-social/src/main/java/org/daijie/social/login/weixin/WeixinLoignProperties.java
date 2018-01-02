package org.daijie.social.login.weixin;

import org.daijie.social.login.LoginProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信登录属性配置初始化
 * @author daijie_jay
 * @since 2017年11月28日
 */
@ConfigurationProperties("weixin.login")
public class WeixinLoignProperties extends LoginProperties {

    private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
