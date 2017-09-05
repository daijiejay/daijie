package org.daijie.shiro.session;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.daijie.core.util.SerializeUtil;
import org.daijie.core.util.http.HttpConversationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
		}

		/**
		 * 设置session值，并更新到redis中
		 * @param key
		 * @param value
		 */
		public static void setAttribute(Object key, Object value){
			initSession();
			session.setAttribute(key, value);
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
			agentRedisSession().updateSession(session);
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
		public static void deleteSession(){
			initSession();
			SecurityUtils.getSubject().logout();
		}
		
		/**
		 * 保存当前会话中的session
		 * @param sessionId
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
		 * @param string 
		 * @return
		 */
		public static String getToken(){
			String token = HttpConversationUtil.getToken();
			if(token == null){
				token = SecurityUtils.getSubject().getSession().getId().toString();
			}
			return token;
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
		public static void set(String key, Object value){
			if(redisSession instanceof RedisSession){
				RedisSession redis = (RedisSession) redisSession;
				redis.getRedisManager().set((key+getToken()).getBytes(), SerializeUtil.serialize(value));
			}else if(redisSession instanceof ClusterRedisSession){
				ClusterRedisSession redis = (ClusterRedisSession) redisSession;
				redis.getRedisManager().set((key+getToken()).getBytes(), SerializeUtil.serialize(value));
			}
		}
		
		/**
		 * 设置redis的值及过期时间
		 * @param key
		 * @param value
		 * @param expire
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
		 * @param key
		 * @return
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
