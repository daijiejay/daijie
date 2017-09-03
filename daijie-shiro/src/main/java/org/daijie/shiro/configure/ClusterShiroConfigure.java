package org.daijie.shiro.configure;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.daijie.core.util.PropertiesLoader;
import org.daijie.shiro.TokenCredentialsMatcher;
import org.daijie.shiro.UserAuthorizingRealm;
import org.daijie.shiro.filter.CredentialFilter;
import org.daijie.shiro.redis.JedisClusterFactory;
import org.daijie.shiro.redis.RedisCacheManager;
import org.daijie.shiro.redis.RedisManager;
import org.daijie.shiro.redis.RedisOperator;
import org.daijie.shiro.session.ClusterRedisSession;
import org.daijie.shiro.session.RedisSession;
import org.daijie.shiro.session.quartz.QuartzSessionValidationScheduler2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * 
 * @author daijie
 * @date 2017年6月5日
 * shiro session集群+redis集群配置
 * 
 */
public class ClusterShiroConfigure {
	
	private PropertiesLoader loader = new PropertiesLoader("bootstrap.properties");
	
	@Bean(name = "shiroFilter")
	@Primary
	public ShiroFilterFactoryBean initShiroFilterFactoryBean(@Qualifier("securityManager") SecurityManager securityManager){
		Map<String, Filter> filterMap = new HashMap<String, Filter>();
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		try {
			shiroFilterFactoryBean.setSecurityManager(securityManager);
			if(!StringUtils.isEmpty(loader.getProperty("shiro.filterClassNames"))){
				for (String filterClassName : loader.getProperty("shiro.filterClassNames").split(",")) {
					if(filterClassName.trim().length() > 0){
						@SuppressWarnings("unchecked")
						Class<? extends Filter> cls = (Class<? extends Filter>) Class.forName(filterClassName);
						String name = filterClassName.substring(filterClassName.lastIndexOf(".")+1).replace("Filter", "");
						filterMap.put(name.substring(0,1).toLowerCase()+name.substring(1), cls.newInstance());
					}
				}
			}
			if(loader.getBoolean("shiro.isCredential") != null && loader.getBoolean("shiro.isCredential")){
				filterMap.put("credential", new CredentialFilter());
			}
			shiroFilterFactoryBean.setFilters(filterMap);
			shiroFilterFactoryBean.setLoginUrl(loader.getProperty("shiro.loginUrl", "/login"));
			shiroFilterFactoryBean.setSuccessUrl(loader.getProperty("shiro.successUrl", "/"));
			shiroFilterFactoryBean.setUnauthorizedUrl(loader.getProperty("shiro.unauthorizedUrl", "/403"));
			Map<String, String> filterChainDefinitionMap = new HashMap<String, String>();
			if(!StringUtils.isEmpty(loader.getProperty("shiro.filterChainDefinitions"))){
				for (String definition : loader.getProperty("shiro.filterChainDefinitions").split(",")) {
					if(definition.contains("=")){
						filterChainDefinitionMap.put(definition.split("=")[0], definition.split("=")[1]);
					}
				}
			}else if(!StringUtils.isEmpty(loader.getProperty("shiro.filterChainDefinitionMap"))){
				ObjectMapper mapper = new ObjectMapper();
				@SuppressWarnings("unchecked")
				Map<String, String> map = mapper.readValue(loader.getProperty("shiro.filterChainDefinitionMap"), Map.class);
				filterChainDefinitionMap = map;
			}else{
				filterChainDefinitionMap.put("*/**", "anon");
			}
			if(loader.getBoolean("shiro.isCredential") != null && loader.getBoolean("shiro.isCredential")){
				filterChainDefinitionMap.put("/login", "credential");
			}
			shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return shiroFilterFactoryBean;
	}
	
	@Bean(name = "securityManager")
	@Primary
	public SecurityManager initSecurityManager(@Qualifier("authorizingRealm") AuthorizingRealm authorizingRealm, @Qualifier("sessionManager") SessionManager sessionManager, 
			@Qualifier("cacheManager") CacheManager cacheManager){
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(authorizingRealm);
		securityManager.setSessionManager(sessionManager);
		securityManager.setCacheManager(cacheManager);
		return securityManager;
	}
	
	@Bean(name = "authorizingRealm")
	@Primary
	public AuthorizingRealm initAuthorizingRealm(@Qualifier("credentialsMatcher") CredentialsMatcher credentialsMatcher){
		UserAuthorizingRealm authorizingRealm = new UserAuthorizingRealm();
		authorizingRealm.setCredentialsMatcher(credentialsMatcher);
		return authorizingRealm;
	}
	
	@Bean(name = "credentialsMatcher")
	@Primary
	public CredentialsMatcher initCredentialsMatcher(@Qualifier("redisSession") RedisSession redisSession){
		TokenCredentialsMatcher tokenCredentialsMatcher = new TokenCredentialsMatcher();
		tokenCredentialsMatcher.setRedisSession(redisSession);
		tokenCredentialsMatcher.setValidation(loader.getBoolean("shiro.isValidation", false));
		return tokenCredentialsMatcher;
	}
	
	@Bean(name = "cacheManager")
	@Primary
	public CacheManager initCacheManager(@Qualifier("redisManager") RedisManager redisManager){
		RedisCacheManager cacheManager = new RedisCacheManager();
		cacheManager.setRedisManager(redisManager);
		return cacheManager;
	}
	
	@Bean(name = "genericObjectPoolConfig")
	@Primary
	public GenericObjectPoolConfig initGenericObjectPoolConfig(){
		GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
		genericObjectPoolConfig.setMaxWaitMillis(-1);
		genericObjectPoolConfig.setMaxTotal(1000);
		genericObjectPoolConfig.setMinIdle(8);
		genericObjectPoolConfig.setMaxIdle(100);
		return genericObjectPoolConfig;
	}
	
	@Bean(name = "jedisCluster")
	@Primary
	public JedisCluster initJedisCluster(){
		JedisClusterFactory jedisCluster = new JedisClusterFactory(
				new HostAndPort(loader.getProperty("shiro.redis.host", "127.0.0.1"), 
						loader.getInteger("shiro.redis.port", 6379)
						));
		jedisCluster.setPassword(loader.getProperty("shiro.redis.cluster.password", ""));
		jedisCluster.setAddressConfig(new DefaultResourceLoader().getResource("classpath:bootstrap.properties"));
		jedisCluster.setAddressKeyPrefix("shiro.redis.cluster.address");
		jedisCluster.setTimeout(loader.getInteger("shiro.redis.timeout", 360000));
		jedisCluster.setConnectionTimeout(loader.getInteger("shiro.redis.connectionTimeout", 10000));
		jedisCluster.setGenericObjectPoolConfig(initGenericObjectPoolConfig());
		return jedisCluster;
	}
	
	@Bean(name = "redisOperator")
	@Primary
	public RedisOperator initRedisOperator(@Qualifier("jedisCluster") JedisCluster jedisCluster){
		RedisOperator redisOperator = new RedisOperator();
		redisOperator.setJedisCluster(jedisCluster);
		return redisOperator;
	}
	
	@Bean(name = "redisManager")
	@Primary
	public RedisManager initRedisManager(@Qualifier("jedisCluster") JedisCluster jedisCluster,
			@Qualifier("redisOperator") RedisOperator redisOperator){
		RedisManager redisManager = new RedisManager();
		redisManager.setJedisCluster(jedisCluster);
		redisManager.setRedisOperator(redisOperator);
		return redisManager;
	}
	
	@Bean(name = "lifecycleBeanPostProcessor")
	@Primary
	public LifecycleBeanPostProcessor initLifecycleBeanPostProcessor(){
		return new LifecycleBeanPostProcessor();
	}
	
	@Bean(name = "redisSession")
	@Primary
	public ClusterRedisSession initRedisSession(@Qualifier("redisManager") RedisManager redisManager, @Qualifier("sessionIdGenerator") SessionIdGenerator sessionIdGenerator){
		ClusterRedisSession redisSession = new ClusterRedisSession();
		redisSession.setRedisManager(redisManager);
		redisSession.setSessionIdGenerator(sessionIdGenerator);
		return redisSession;
	}
	
	@Bean(name = "sessionIdGenerator")
	@Primary
	public SessionIdGenerator initSessionIdGenerator(){
		return new JavaUuidSessionIdGenerator();
	}
	
	@Bean(name = "sessionManager")
	@Primary
	public SessionManager initSessionManager(@Qualifier("redisSession") ClusterRedisSession redisSession, @Qualifier("simpleCookie") SimpleCookie simpleCookie,
			@Qualifier("sessionValidationScheduler") SessionValidationScheduler sessionValidationScheduler){
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionDAO(redisSession);
		sessionManager.setSessionIdCookie(simpleCookie);
		sessionManager.setSessionValidationScheduler(sessionValidationScheduler);
		sessionManager.setSessionValidationSchedulerEnabled(true);
		sessionManager.setSessionIdCookieEnabled(true);
		sessionManager.setGlobalSessionTimeout(360000);
		return sessionManager;
	}
	
	@Bean(name = "sessionManagerScheduler")
	@Primary
	public SessionManager initSessionManager(@Qualifier("redisSession") ClusterRedisSession redisSession, 
			@Qualifier("simpleCookie") SimpleCookie simpleCookie){
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionDAO(redisSession);
		sessionManager.setSessionIdCookie(simpleCookie);
		sessionManager.setSessionValidationSchedulerEnabled(true);
		sessionManager.setSessionIdCookieEnabled(true);
		sessionManager.setGlobalSessionTimeout(360000);
		return sessionManager;
	}
	
	@Bean(name = "simpleCookie")
	@Primary
	public SimpleCookie initSimpleCookie(){
		return new SimpleCookie(loader.getProperty("shiro.sessionid", "mysessionid"));
	}
	
	@Bean(name = "sessionValidationScheduler")
	@Primary
	public SessionValidationScheduler initSessionValidationScheduler(@Qualifier("sessionManagerScheduler") SessionManager sessionManager){
		QuartzSessionValidationScheduler2 sessionValidationScheduler = new QuartzSessionValidationScheduler2();
		sessionValidationScheduler.setSessionManager((ValidatingSessionManager) sessionManager);
		return sessionValidationScheduler;
	}
	
	@Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return aasa;
    }
}
