package org.daijie.shiro.session;

import java.io.Serializable;

import org.apache.shiro.session.Session;

/**
 * shiro redis工厂类
 * @author daijie
 * @date 2017年6月22日
 */
public interface RedisSessionFactory {

	/**
	 * 获取session
	 * @param sessionId
	 * @return
	 */
	public Session getSession(Serializable sessionId);
	
	/**
	 * 保存session
	 * @param sessionId
	 * @return
	 */
	public Session saveSession(Serializable sessionId);
	
	/**
	 * 删除session
	 * @param sessionId
	 * @return
	 */
	public Session removeSession(Serializable sessionId);
	
	/**
	 * 更新Session
	 * @param session
	 */
	public void updateSession(Session session);
}
