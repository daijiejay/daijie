package org.daijie.shiro.session;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.crazycake.shiro.RedisSessionDAO;

public class RedisSession extends RedisSessionDAO implements RedisSessionFactory {

	@Override
	public Session getSession(Serializable sessionId){
		return doReadSession(sessionId);
	}
}
