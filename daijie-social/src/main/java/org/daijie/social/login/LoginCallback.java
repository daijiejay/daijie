package org.daijie.social.login;


public interface LoginCallback<T, E> {

	/**
	 * 登录成功后逻辑处理
	 * @param result
	 */
	public void handle(T userInfo);
	
	/**
	 * 登录失败后逻辑处理 
	 * @param error
	 */
	public void errer(E error);
}
