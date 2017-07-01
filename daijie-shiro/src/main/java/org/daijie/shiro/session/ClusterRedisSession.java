package org.daijie.shiro.session;

import java.io.Serializable;

import org.apache.shiro.session.Session;

/**
 * 集群redis服务
 * @author daijie
 * @date 2017年6月22日
 */
public class ClusterRedisSession extends RedisSessionDAO implements RedisSessionFactory {

	@Override
	public Session getSession(Serializable sessionId){
		return doReadSession(sessionId);
	}

	@Override
	public Session saveSession(Serializable sessionId) {
		return saveSession(sessionId);
	}

	@Override
	public Session removeSession(Serializable sessionId) {
		return removeSession(sessionId);
	}

	@Override
	public void updateSession(Session session) {
		update(session);
	}

}
