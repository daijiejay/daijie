package org.daijie.shiro.oauth2.configure;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;

/**
 * oauth2 shiro登录授权、角色权限控制的配置类
 * @author daijie_jay
 * @since 2017年12月27日
 */
@ConfigurationProperties(prefix = "shiro.oauth2")
public class ShiroOauth2Properties {

	/**
	 * 接口请求路径
	 * 必须配置，在授权时或者是跳转登录页面登录时需要调用的接口路径
	 * 默认是参数名是username和password，如需自定义http://daijie.org/login?user={username}&password={password}
	 */
	private String loginUrl;
	
	/**
	 * 请求方式
	 * 默认POST请求
	 */
	private String loginMethod = HttpMethod.POST.name();
	
	/**
	 * 自定义授权跳转的登录页面
	 * form表单action必须是/login
	 */
	private String loginPage;
	
	/**
	 * 需要过滤的路径集合与角色集合配置
	 */
	private Map<String, List<String>> matcher = new ConcurrentHashMap<String, List<String>>();
	
	/**
	 * access_token有效时间（单位分，默认1分钟）
	 */
	private int accessTokenValiditySeconds = (int) TimeUnit.DAYS.toSeconds(1);

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

	public String getLoginPage() {
		return loginPage;
	}

	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}

	public Map<String, List<String>> getMatcher() {
		return matcher;
	}

	public void setMatcher(Map<String, List<String>> matcher) {
		this.matcher = matcher;
	}

	public int getAccessTokenValiditySeconds() {
		return accessTokenValiditySeconds;
	}

	public void setAccessTokenValiditySeconds(int accessTokenValiditySeconds) {
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
	}
}
