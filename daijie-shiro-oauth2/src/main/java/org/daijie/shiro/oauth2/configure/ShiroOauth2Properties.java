package org.daijie.shiro.oauth2.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 请求shiro登录接口服务的配置类
 * loginUrl：请求路径
 * loginMethod：请求方式，只支持get与post
 * @author daijie_jay
 * @date 2017年12月27日
 */
@ConfigurationProperties(prefix = "shiro.oauth2")
public class ShiroOauth2Properties {

	private String loginUrl;
	
	private String loginMethod;

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getLoginMethod() {
		return loginMethod;
	}

	public void setLoginMethod(String loginMethod) {
		this.loginMethod = loginMethod;
	}
	
}
