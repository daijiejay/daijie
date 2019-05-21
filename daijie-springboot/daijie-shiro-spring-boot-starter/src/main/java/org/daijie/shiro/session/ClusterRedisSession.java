package org.daijie.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.daijie.shiro.redis.RedisManager;

import java.io.Serializable;

/**
 * 集群redis服务
 * @author daijie
 * @since 2017年6月22日
 */
public class ClusterRedisSession extends RedisSessionDAO implements RedisSessionFactory {
	
	public ClusterRedisSession(){
		
	}
	
	public ClusterRedisSession(RedisManager redisManager, SessionIdGenerator sessionIdGenerator){
		this.setRedisManager(redisManager);
		this.setSessionIdGenerator(sessionIdGenerator);
	}

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
