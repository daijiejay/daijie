package org.daijie.social.login;


public interface LoginCallback<T, E> {

	/**
	 * 登录成功后逻辑处理
	 * @param userInfo 第三方用户实体
	 */
	public void handle(T userInfo);
	
	/**
	 * 登录失败后逻辑处理 
	 * @param error 第三方错误实体
	 */
	public void errer(E error);
}
