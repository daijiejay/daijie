package org.daijie.shiro.oauth2;

import org.daijie.shiro.oauth2.AuthenticationMatch;

/**
 * 用户权限匹配器实例工厂
 * @author daijie_jay
 * @date 2017年12月27日
 */
public interface AuthenticationMatchFactory {

	/**
	 * 设置实现了org.daijie.shiro.oauth2.AuthenticationMatch匹配器
	 * @param authenticationMatch 权限匹配器
	 */
	public void setAuthenticationMatch(AuthenticationMatch authenticationMatch);
}
