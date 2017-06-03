package org.daijie.web.service;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.crazycake.shiro.RedisSessionDAO;
import org.daijie.core.service.IBasicService;
import org.daijie.core.util.HttpRequestUtil;
import org.daijie.shiro.session.ClusterRedisSession;
import org.daijie.shiro.session.RedisSession;
import org.daijie.shiro.session.RedisSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * 
 * @author daijie
 *
 * @param <MAPPER>
 */
public abstract class BasicService<DAO> implements IBasicService {

	private static Logger logger = LoggerFactory.getLogger(BasicService.class);
	
	public static Session session;  

	@Autowired
	protected DAO dao;

	protected static RedisSessionFactory redisSession;  

	@Autowired
	public void setRedisSession(RedisSessionFactory redisSession) {
		BasicService.redisSession = redisSession;
	}

	public static class MySession extends RedisSessionDAO {
		
		public static void initSession(){
			if(!StringUtils.isEmpty(getToken())){
				if(session != null && session.getId().equals(getToken())){
					return;
				}
				if(redisSession == null){
					logger.error("redisSession is null!");
				}else{
					try {
						session = ((AbstractSessionDAO) redisSession).readSession(getToken());
					} catch (UnknownSessionException e) {
						logger.error(e.getMessage());
						session = null;
					}
				}
			}else{
				logger.error("token is null!");
				session = null;
			}
		}

		public static void setAttribute(Object key, Object value){
			initSession();
			session.setAttribute(key, value);
			((SessionDAO) redisSession).update(session);
		}

		public static Object getAttribute(Object key){
			initSession();
			return session.getAttribute(key);
		}
		
		public static void removeAttribute(Object key){
			initSession();
			session.removeAttribute(key);
			((SessionDAO) redisSession).update(session);
		}
		
		public static Session getSession(){
			initSession();
			return session;
		}
		
		public static void deleteSession(Serializable sessionId){
			((SessionDAO) redisSession).delete(redisSession.getSession(sessionId));
		}

		public static String getToken(){
			return HttpRequestUtil.getToken();
		}
	}
	
	public static class Redis extends MySession {
		
		public static void set(String key, String value){
			if(redisSession instanceof RedisSession)
				((RedisSession) redisSession).getRedisManager().set((key+getToken()).getBytes(), value.getBytes());
			else if(redisSession instanceof ClusterRedisSession)
				((ClusterRedisSession) redisSession).getRedisManager().set((key+getToken()).getBytes(), value.getBytes());
		}
		
		public static String get(String key){
			byte[] value = {};
			if(redisSession instanceof RedisSession)
				value = ((RedisSession) redisSession).getRedisManager().get((key+getToken()).getBytes());
			else if(redisSession instanceof ClusterRedisSession)
				value = ((ClusterRedisSession) redisSession).getRedisManager().get((key+getToken()).getBytes());
			return new String(value);
		}
	}
}
