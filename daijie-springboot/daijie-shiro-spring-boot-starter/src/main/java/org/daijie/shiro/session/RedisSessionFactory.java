package org.daijie.shiro.session;

import org.apache.shiro.session.Session;

import java.io.Serializable;

/**
 * shiro redis工厂类
 * @author daijie
 * @since 2017年6月22日
 */
public interface RedisSessionFactory {

	/**
	 * 获取session
	 * @param sessionId token
	 * @return Session
	 */
	public Session getSession(Serializable sessionId);
	
	/**
	 * 保存session
	 * @param session session
	 */
	public void saveSession(Session session);
	
	/**
	 * 删除session
	 * @param session session
	 */
	public void removeSession(Session session);
	
	/**
	 * 更新Session
	 * @param session session
	 */
	public void updateSession(Session session);
}
