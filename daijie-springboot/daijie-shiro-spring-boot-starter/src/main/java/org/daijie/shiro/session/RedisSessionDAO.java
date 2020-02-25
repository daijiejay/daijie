package org.daijie.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.crazycake.shiro.exception.SerializationException;
import org.crazycake.shiro.serializer.ObjectSerializer;
import org.crazycake.shiro.serializer.RedisSerializer;
import org.crazycake.shiro.serializer.StringSerializer;
import org.daijie.shiro.redis.ClusterRedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

/**
 * 重写org.crazycake.shiro.RedisSessionDAO
 * 实现redis集群
 * @author daijie
 * @since 2017年6月22日
 */
public class RedisSessionDAO extends AbstractSessionDAO {

	private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);
	/**
	 * shiro-redis的session对象前缀
	 */
	private ClusterRedisManager redisManager;
	private RedisSerializer keySerializer = new StringSerializer();
	private RedisSerializer valueSerializer = new ObjectSerializer();
	
	/**
	 * The Redis key prefix for the sessions 
	 */
	private String keyPrefix = "shiro_redis_session:";
	
	@Override
	public void update(Session session) throws UnknownSessionException {
		this.saveSession(session);
	}
	
	/**
	 * save session
	 * @param session
	 * @throws UnknownSessionException
	 */
	private void saveSession(Session session) throws UnknownSessionException {
		if(session == null || session.getId() == null){
			logger.error("session or session id is null");
			return;
		}
		
		byte[] key = null;
		byte[] value = null;
		try {
			key = this.keySerializer.serialize(getByteKey(session.getId()));
			value = this.valueSerializer.serialize(session);
		} catch (SerializationException e) {
			throw new UnknownSessionException(e);
		}
		session.setTimeout(redisManager.getExpire()*1000);		
		this.redisManager.set(key, value, redisManager.getExpire());
	}

	@Override
	public void delete(Session session) {
		if(session == null || session.getId() == null){
			logger.error("session or session id is null");
			return;
		}
		redisManager.del(this.getByteKey(session.getId()));

	}

	@Override
	public Collection<Session> getActiveSessions() {
		Set<Session> sessions = new HashSet<Session>();
		TreeSet<String> treeSet = new TreeSet<String>();
		try {
			treeSet = redisManager.keys(this.keyPrefix + "*");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(treeSet != null && treeSet.size()>0){
			Iterator<?> it = treeSet.iterator();
			while (it.hasNext()) {
				Session s = null;
				try {
					s = (Session) this.valueSerializer.deserialize(
						redisManager.get(this.keySerializer.serialize(it.next())));
				} catch (SerializationException e) {
					logger.error("获取本地Session失败！");
				}
				sessions.add(s);
			}
		}
		
		return sessions;
	}

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = this.generateSessionId(session);  
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		if(sessionId == null){
			logger.error("session id is null");
			return null;
		}

		Session s = null;
		try {
			s = (Session)this.valueSerializer.deserialize(redisManager.get(this.getByteKey(sessionId)));
		} catch (SerializationException e) {
			logger.error("获取session失败. settionId=" + sessionId);
		}
		return s;
	}
	
	/**
	 * 获得byte[]型的key
	 * @param sessionId
	 * @return
	 */
	private byte[] getByteKey(Serializable sessionId){
		String preKey = this.keyPrefix + sessionId;
		return preKey.getBytes();
	}

	public ClusterRedisManager getRedisManager() {
		return redisManager;
	}

	public void setRedisManager(ClusterRedisManager redisManager) {
		this.redisManager = redisManager;
		
		/**
		 * 初始化redisManager
		 */
		this.redisManager.init();
	}

	/**
	 * Returns the Redis session keys
	 * prefix.
	 * @return The prefix
	 */
	public String getKeyPrefix() {
		return keyPrefix;
	}

	/**
	 * Sets the Redis sessions key 
	 * prefix.
	 * @param keyPrefix The prefix
	 */
	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}
	
	
}
