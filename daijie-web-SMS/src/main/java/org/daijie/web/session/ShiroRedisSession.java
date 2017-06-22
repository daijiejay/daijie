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

/**
 * shiro redis管理类
 * 提供基于shiro session的redis增删改查
 * 兼容redis单机和集群配置
 * @author daijie
 * @date 2017年6月22日
 */
public class ShiroRedisSession {

	private static Logger logger = LoggerFactory.getLogger(ShiroRedisSession.class);
	
	private static Session session;  

	private static RedisSessionFactory redisSession;  

	@Autowired
	public void setRedisSession(RedisSessionFactory redisSession) {
		ShiroRedisSession.redisSession = redisSession;
	}

	/**
	 * 基于shiro session管理
	 * @author daijie
	 * @date 2017年6月22日
	 */
	private static class ShiroSession {
		
		/**
		 * 初始化当前会话中的session
		 */
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

		/**
		 * 设置session值，并更新到redis中
		 * @param key
		 * @param value
		 */
		public static void setAttribute(Object key, Object value){
			initSession();
			session.setAttribute(key, value);
			((SessionDAO) redisSession).update(session);
		}
		
		/**
		 * 设置session值及过期时间，并更新到redis中
		 * @param key
		 * @param value
		 */
		public static void setAttribute(Object key, Object value, long maxIdleTimeInMillis){
			initSession();
			session.setAttribute(key, value);
			session.setTimeout(maxIdleTimeInMillis);
			((SessionDAO) redisSession).update(session);
		}

		/**
		 * 获取session中的值
		 * @param key
		 * @return
		 */
		public static Object getAttribute(Object key){
			initSession();
			return session.getAttribute(key);
		}

		/**
		 * 删除session值，并更新到redis中
		 * @param key
		 * @param value
		 */
		public static void removeAttribute(Object key){
			initSession();
			session.removeAttribute(key);
			((SessionDAO) redisSession).update(session);
		}
		
		/**
		 * 获取当前会话中的session
		 * @return
		 */
		public static Session getSession(){
			initSession();
			return session;
		}
		
		/**
		 * 清除当前会话中的session
		 * @param sessionId
		 */
		public static void deleteSession(Serializable sessionId){
			((SessionDAO) redisSession).delete(redisSession.getSession(sessionId));
		}

		/**
		 * 获取当前会话中的凭证
		 * @return
		 */
		public static String getToken(){
			return HttpRequestUtil.getToken();
		}
	}
	
	/**
	 * redis管理
	 * @author daijie
	 * @date 2017年6月22日
	 */
	public static class Redis extends ShiroSession {
		
		/**
		 * 设置redis的值
		 * @param key
		 * @param value
		 */
		public static void set(String key, String value){
			set(key, value, 360000);
		}
		
		/**
		 * 设置redis的值及过期时间
		 * @param key
		 * @param value
		 * @param expire
		 */
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
		}
		
		/**
		 * 获取redis中的值
		 * @param key
		 * @return
		 */
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
