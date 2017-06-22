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
}
