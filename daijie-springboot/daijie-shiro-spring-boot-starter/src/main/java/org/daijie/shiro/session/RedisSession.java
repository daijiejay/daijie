package org.daijie.shiro.session;

import org.apache.shiro.session.Session;
import org.crazycake.shiro.RedisSessionDAO;

import java.io.Serializable;

/**
 * redis服务
 * @author daijie
 * @since 2017年6月22日
 */
public class RedisSession extends RedisSessionDAO implements RedisSessionFactory {

	@Override
	public Session getSession(Serializable sessionId){
		return doReadSession(sessionId);
	}

	@Override
	public void saveSession(Session session) {
		update(session);
	}

	@Override
	public void removeSession(Session session) {
		delete(session);
	}

	@Override
	public void updateSession(Session session) {
		update(session);
	}
}
