package org.daijie.shiro.session;

import java.io.Serializable;

import org.apache.shiro.session.Session;

public class ClusterRedisSession extends RedisSessionDAO implements RedisSessionFactory {

	@Override
	public Session getSession(Serializable sessionId){
		return doReadSession(sessionId);
	}

}
