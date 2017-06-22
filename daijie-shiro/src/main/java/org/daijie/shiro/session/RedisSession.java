package org.daijie.shiro.session;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.crazycake.shiro.RedisSessionDAO;

/**
 * redis服务
 * @author daijie
 * @date 2017年6月22日
 */
public class RedisSession extends RedisSessionDAO implements RedisSessionFactory {

	/**
	 * 获取session
	 */
	@Override
	public Session getSession(Serializable sessionId){
		return doReadSession(sessionId);
	}
}
