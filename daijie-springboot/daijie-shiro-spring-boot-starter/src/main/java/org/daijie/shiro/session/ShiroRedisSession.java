package org.daijie.shiro.session;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.daijie.core.util.SerializeUtil;
import org.daijie.shiro.util.HttpConversationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * shiro redis管理类
 * 提供基于shiro session的redis增删改查
 * 兼容redis单机和集群配置
 * @author daijie
 * @since 2017年6月22日
 */
public class ShiroRedisSession {

	private static Logger logger = LoggerFactory.getLogger(ShiroRedisSession.class);
	
	private static final String SESSION_EXPIRE_KEY = "SESSION_EXPIRE_KEY";

	public static final String TOKEN_NAME = "token";
	
	private static Session session;  

	private static RedisSessionFactory redisSession;  
	
	private static long sessionTimeOut = 360000;

	private static Integer expire = 360000;

	public static String token = ShiroRedisSession.TOKEN_NAME;
	
	public ShiroRedisSession(String token) {
		if (!StringUtils.isEmpty(token)) {
			ShiroRedisSession.token = token;
		}
	}
	
	public ShiroRedisSession(String token, long sessionTimeOut, Integer expire) {
		if (!StringUtils.isEmpty(token)) {
			ShiroRedisSession.token = token;
		}
		ShiroRedisSession.sessionTimeOut = sessionTimeOut;
		ShiroRedisSession.expire = expire;
	}

	@Autowired
	public void setRedisSession(RedisSessionFactory redisSession) {
		ShiroRedisSession.redisSession = redisSession;
	}

	/**
	 * 基于shiro session管理
	 * @author daijie
	 * @since 2017年6月22日
	 */
	private static class ShiroSession {
		
		/**
		 * 初始化当前会话中的session
		 */
		public static void initSession(){
			if(redisSession == null){
				logger.error("redisSession is null!");
			}else{
				try {
					String token = null;
					try {
						token = getToken();
					} catch (Exception e) {

					}
					if (token == null) {
						token = SecurityUtils.getSubject().getSession().getId().toString();
						isExpire(true);
					}
					session = ((AbstractSessionDAO) redisSession).readSession(token);
				} catch (UnknownSessionException e) {
					session = null;
				}
			}
		}
		
		/**
		 * 设置session过期	
		 */
		public static void isExpire(boolean expire){
			if (session != null) {
				session.setAttribute(SESSION_EXPIRE_KEY, expire);
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
			session.setTimeout(sessionTimeOut);
			agentRedisSession().updateSession(session);
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
			agentRedisSession().updateSession(session);
		}

		/**
		 * 获取session中的值
		 * @param key
		 * @return Object
		 */
		public static Object getAttribute(Object key){
			initSession();
			return session.getAttribute(key);
		}

		/**
		 * 删除session值，并更新到redis中
		 * @param key
		 */
		public static void removeAttribute(Object key){
			initSession();
			session.removeAttribute(key);
			agentRedisSession().updateSession(session);
		}
		
		/**
		 * 获取当前会话中的session
		 * @return Session
		 */
		public static Session getSession(){
			initSession();
			return session;
		}
		
		/**
		 * 清除当前会话中的session
		 */
		public static void deleteSession(){
			initSession();
			SecurityUtils.getSubject().logout();
		}
		
		/**
		 * 保存当前会话中的session
		 */
		public static void saveSession(){
			initSession();
			agentRedisSession().saveSession(session);
		}
		
		/**
		 * 代理获取RedisSessionFactory实现类
		 * @return
		 */
		private static <T extends RedisSessionFactory> RedisSessionFactory agentRedisSession(){
			if(redisSession instanceof RedisSession){
				return (RedisSession) redisSession;
			}else if(redisSession instanceof ClusterRedisSession){
				return (ClusterRedisSession) redisSession;
			}
			return null;
		}

		/**
		 * 获取当前会话中的凭证
		 * @return String
		 */
		public static String getToken(){
			return HttpConversationUtil.getToken(token);
		}
	}
	
	/**
	 * redis管理
	 * @author daijie
	 * @since 2017年6月22日
	 */
	public static class Redis extends ShiroSession {
		
		/**
		 * 设置redis的值
		 * @param key 键
		 * @param value 值
		 */
		public static void set(String key, Object value){
			if(redisSession instanceof RedisSession){
				RedisSession redis = (RedisSession) redisSession;
				redis.getRedisManager().set((key+getToken()).getBytes(), SerializeUtil.serialize(value), ShiroRedisSession.expire);
			}else if(redisSession instanceof ClusterRedisSession){
				ClusterRedisSession redis = (ClusterRedisSession) redisSession;
				redis.getRedisManager().set((key+getToken()).getBytes(), SerializeUtil.serialize(value));
			}
		}
		
		/**
		 * 设置redis的值及过期时间
		 * @param key 键
		 * @param value 值
		 * @param expire 超时时间
		 */
		public static void set(String key, Object value, int expire){
			if(redisSession instanceof RedisSession){
				RedisSession redis = (RedisSession) redisSession;
				redis.getRedisManager().set((key+getToken()).getBytes(), SerializeUtil.serialize(value), expire);
			}else if(redisSession instanceof ClusterRedisSession){
				ClusterRedisSession redis = (ClusterRedisSession) redisSession;
				redis.getRedisManager().set((key+getToken()).getBytes(), SerializeUtil.serialize(value), expire);
			}
		}
		
		/**
		 * 获取redis中的值
		 * @param key 键
		 * @return Object
		 */
		public static Object get(String key){
			byte[] value = {};
			if(redisSession instanceof RedisSession){
				RedisSession redis = (RedisSession) redisSession;
				value = redis.getRedisManager().get((key+getToken()).getBytes());
			}else if(redisSession instanceof ClusterRedisSession){
				ClusterRedisSession redis = (ClusterRedisSession) redisSession;
				value = redis.getRedisManager().get((key+getToken()).getBytes());
			}
			if(value != null){
				return SerializeUtil.deserialize(value);
			}
			return value;
		}
	}
}
