package org.daijie.web.session;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.daijie.core.util.ApplicationContextHolder;
import org.daijie.core.util.http.HttpRequestUtil;
import org.daijie.shiro.session.ClusterRedisSession;
import org.daijie.shiro.session.RedisSession;
import org.daijie.shiro.session.RedisSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class ShiroRedisSession {

	private static Logger logger = LoggerFactory.getLogger(ShiroRedisSession.class);
	
	private static Session session;  

	private static RedisSessionFactory redisSession;  

	@Autowired
	public void setRedisSession(RedisSessionFactory redisSession) {
		ShiroRedisSession.redisSession = redisSession;
	}

	private static class ShiroSession {
		
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
	
	public static class Redis extends ShiroSession {
		
		public static void set(String key, String value){
			set(key, value, 360000);
		}
		
		public static void set(String key, String value, int expire){
			if(redisSession instanceof RedisSession){
				RedisSession redis = (RedisSession) redisSession;
				redis.setRedisManager((org.crazycake.shiro.RedisManager)ApplicationContextHolder.getBean("redisManager"));
				redis.getRedisManager().set((key+getToken()).getBytes(), value.getBytes(), expire);
			}else if(redisSession instanceof ClusterRedisSession){
				ClusterRedisSession redis = (ClusterRedisSession) redisSession;
				redis.setRedisManager((org.daijie.shiro.redis.RedisManager)ApplicationContextHolder.getBean("redisManager"));
				redis.getRedisManager().set((key+getToken()).getBytes(), value.getBytes(), expire);
			}
			get(key);
		}
		
		public static String get(String key){
			byte[] value = {};
			if(redisSession instanceof RedisSession){
				RedisSession redis = (RedisSession) redisSession;
				redis.setRedisManager((org.crazycake.shiro.RedisManager)ApplicationContextHolder.getBean("redisManager"));
				value = redis.getRedisManager().get((key+getToken()).getBytes());
			}else if(redisSession instanceof ClusterRedisSession){
				ClusterRedisSession redis = (ClusterRedisSession) redisSession;
				redis.setRedisManager((org.daijie.shiro.redis.RedisManager)ApplicationContextHolder.getBean("redisManager"));
				value = redis.getRedisManager().get((key+getToken()).getBytes());
			}
			return new String(value);
		}
	}
}
