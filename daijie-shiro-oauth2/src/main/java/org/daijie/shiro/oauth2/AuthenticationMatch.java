package org.daijie.shiro.oauth2;

/**
 * 通过用户密码匹配用户角色
 * @author daijie_jay
 * @since 2017年12月26日
 */
public interface AuthenticationMatch {

	/**
	 * 匹配具体实现
	 * @param username 用户名
	 * @param password 密码
	 * @return Boolean
	 */
	public Boolean match(String username, String password);
}
