package org.daijie.shiro.configure;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.daijie.shiro.ShiroSecurity;
import org.daijie.shiro.TokenCredentialsMatcher;
import org.daijie.shiro.UserAuthorizingRealm;
import org.daijie.shiro.filter.CredentialFilter;
import org.daijie.shiro.redis.BaseRedisManagerFactory;
import org.daijie.shiro.redis.RedisCacheManager;
import org.daijie.shiro.redis.RedisManager;
import org.daijie.shiro.redis.RedisOperator;
import org.daijie.shiro.session.ClusterRedisSession;
import org.daijie.shiro.session.RedisSession;
import org.daijie.shiro.session.RedisSessionFactory;
import org.daijie.shiro.session.bean.ShiroRedisSessionBean;
import org.daijie.shiro.session.quartz.QuartzSessionValidationScheduler2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import javax.servlet.Filter;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * shiro相关bean配置
 * @author daijie
 * @since 2017年6月5日
 */
@Configuration
@EnableConfigurationProperties({ShiroProperties.class, ShiroRedisProperties.class})
@Import(ShiroRedisSessionBean.class)
public class ShiroConfigure {

	@Bean(name = "shiroFilter")
	@Primary
	public ShiroFilterFactoryBean initShiroFilterFactoryBean(@Qualifier("securityManager") SecurityManager securityManager,
			ShiroProperties shiroProperties){
		Map<String, Filter> filterMap = new HashMap<String, Filter>();
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		try {
			shiroFilterFactoryBean.setSecurityManager(securityManager);
			if(shiroProperties.getFilterClasses() != null){
				for (Class<AdviceFilter> cls : shiroProperties.getFilterClasses()) {
					String filterClassName = cls.getTypeName();
					String name = filterClassName.substring(filterClassName.lastIndexOf(".")+1).replace("Filter", "");
					filterMap.put(name.substring(0,1).toLowerCase()+name.substring(1), cls.newInstance());
				}
			}else if(!StringUtils.isEmpty(shiroProperties.getFilterClassNames())){
				for (String filterClassName : shiroProperties.getFilterClassNames().split(",")) {
					if(filterClassName.trim().length() > 0){
						@SuppressWarnings("unchecked")
						Class<? extends Filter> cls = (Class<? extends Filter>) Class.forName(filterClassName);
						String name = filterClassName.substring(filterClassName.lastIndexOf(".")+1).replace("Filter", "");
						filterMap.put(name.substring(0,1).toLowerCase()+name.substring(1), cls.newInstance());
					}
				}
			}
			filterMap.put("credential", new CredentialFilter());
			shiroFilterFactoryBean.setFilters(filterMap);
			shiroFilterFactoryBean.setLoginUrl(shiroProperties.getLoginUrl());
			shiroFilterFactoryBean.setSuccessUrl(shiroProperties.getSuccessUrl());
			shiroFilterFactoryBean.setUnauthorizedUrl(shiroProperties.getUnauthorizedUrl());
			Map<String, String> filterChainDefinitionMap = new HashMap<String, String>();
			if(!shiroProperties.getMatcher().isEmpty()){
				Iterator<Entry<String, String>> iterator = shiroProperties.getMatcher().entrySet().iterator();
	    		while (iterator.hasNext()) {
	    			Entry<String, String> next = iterator.next();
	    			String matcher = next.getKey().charAt(0) != '_' ? 
	    					("/" + next.getKey()).replaceAll("_", "/") : next.getKey().replaceAll("_", "/");
	    			matcher = next.getKey().charAt(next.getKey().length() - 1) == '_' ?
	    					matcher + "**" : matcher;
	    			filterChainDefinitionMap.put(matcher, next.getValue());
	    		}
			}else if(!StringUtils.isEmpty(shiroProperties.getFilterChainDefinitions())){
				for (String definition : shiroProperties.getFilterChainDefinitions().split(",")) {
					if(definition.contains("=")){
						filterChainDefinitionMap.put(definition.split("=")[0], definition.split("=")[1]);
					}
				}
			}else if(!StringUtils.isEmpty(shiroProperties.getFilterChainDefinitionMap())){
				ObjectMapper mapper = new ObjectMapper();
				@SuppressWarnings("unchecked")
				Map<String, String> map = mapper.readValue(shiroProperties.getFilterChainDefinitionMap(), Map.class);
				filterChainDefinitionMap = map;
			}
			if(shiroProperties.getInitCredentialUrl() != null && shiroProperties.getInitCredentialUrl().length > 0){
				for (String initCredentialUrl : shiroProperties.getInitCredentialUrl()) {
					filterChainDefinitionMap.put(initCredentialUrl, "credential");
				}
			}else{
				filterChainDefinitionMap.put("/**", "credential");
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
	
	/**
	 * @param credentialsMatcher
	 * @return
	 */
	@Bean(name = "authorizingRealm")
	@Primary
	public AuthorizingRealm initAuthorizingRealm(@Qualifier("credentialsMatcher") CredentialsMatcher credentialsMatcher,
			ShiroSecurity shiroSecurity){
		UserAuthorizingRealm authorizingRealm = new UserAuthorizingRealm();
		authorizingRealm.setCredentialsMatcher(credentialsMatcher);
		authorizingRealm.setShiroSecurity(shiroSecurity);
		return authorizingRealm;
	}
	
	@Bean(name = "credentialsMatcher")
	@Primary
	public CredentialsMatcher initCredentialsMatcher(@Qualifier("redisSession") RedisSessionFactory redisSession,
			ShiroProperties shiroProperties){
		TokenCredentialsMatcher tokenCredentialsMatcher = new TokenCredentialsMatcher();
		tokenCredentialsMatcher.setRedisSession(redisSession);
		tokenCredentialsMatcher.setValidation(shiroProperties.getIsValidation());
		return tokenCredentialsMatcher;
	}
	
	@Bean(name = "lifecycleBeanPostProcessor")
	@Primary
	public LifecycleBeanPostProcessor initLifecycleBeanPostProcessor(){
		return new LifecycleBeanPostProcessor();
	}
	
	@Bean(name = "redisSession")
	@Primary
	public RedisSessionFactory initRedisSession(@Qualifier("redisManagerFactory") BaseRedisManagerFactory redisManagerFactory,
			@Qualifier("sessionIdGenerator") SessionIdGenerator sessionIdGenerator,
			ShiroRedisProperties shiroRedisProperties){
		if(shiroRedisProperties.getCluster()){
			return new ClusterRedisSession(redisManagerFactory.getClusterRedisManager(), sessionIdGenerator);
		}else{
			RedisSession redisSession = new RedisSession();
			redisSession.setRedisManager(redisManagerFactory.getSingleRedisManager());
			redisSession.setSessionIdGenerator(sessionIdGenerator);
			return redisSession;
		}
	}
	
	@Bean(name = "sessionIdGenerator")
	@Primary
	public SessionIdGenerator initSessionIdGenerator(){
		return new JavaUuidSessionIdGenerator();
	}
	
	@Bean(name = "sessionManager")
	@Primary
	public SessionManager initSessionManager(@Qualifier("redisSession") SessionDAO redisSession, @Qualifier("simpleCookie") SimpleCookie simpleCookie,
                                             @Qualifier("sessionValidationScheduler") SessionValidationScheduler sessionValidationScheduler,
                                             ShiroProperties shiroProperties){
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionDAO(redisSession);
		sessionManager.setSessionIdCookie(simpleCookie);
		sessionManager.setSessionValidationScheduler(sessionValidationScheduler);
		sessionManager.setSessionValidationSchedulerEnabled(true);
		sessionManager.setSessionIdCookieEnabled(!shiroProperties.getKissoEnable());
		sessionManager.setGlobalSessionTimeout(360000);
		return sessionManager;
	}
	
	@Bean(name = "sessionManagerScheduler")
	@Primary
	public SessionManager initSessionManager(@Qualifier("redisSession") SessionDAO redisSession,
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
	public SimpleCookie initSimpleCookie(ShiroProperties shiroProperties){
		return new SimpleCookie(shiroProperties.getSessionid());
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
	

	@Bean("redisManager")
	public Object redisManager(ShiroRedisProperties shiroRedisProperties){
		if(shiroRedisProperties.getCluster()){
			GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
			genericObjectPoolConfig.setMaxWaitMillis(-1);
			genericObjectPoolConfig.setMaxTotal(1000);
			genericObjectPoolConfig.setMinIdle(8);
			genericObjectPoolConfig.setMaxIdle(100);
			String[] serverArray = shiroRedisProperties.getAddress().split(",");
			Set<HostAndPort> nodes = new HashSet<>();
			for (String ipPort : serverArray) {
				String[] ipPortPair = ipPort.split(":");
				nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
			}
			JedisCluster jedisCluster = new JedisCluster(nodes,
					shiroRedisProperties.getConnectionTimeout(), 
					shiroRedisProperties.getTimeout(), 
					shiroRedisProperties.getMaxAttempts(), 
					shiroRedisProperties.getPassword(), 
					genericObjectPoolConfig);
			RedisOperator redisOperator = new RedisOperator();
			redisOperator.setJedisCluster(jedisCluster);
			RedisManager redisManager = new RedisManager();
			redisManager.setJedisCluster(jedisCluster);
			redisManager.setRedisOperator(redisOperator);
			return redisManager;
		}else{
			String[] hosts = shiroRedisProperties.getAddress().split(",")[0].split(":");
			org.crazycake.shiro.RedisManager redisManager = new org.crazycake.shiro.RedisManager();
			redisManager.setExpire(shiroRedisProperties.getExpire());
			redisManager.setTimeout(shiroRedisProperties.getConnectionTimeout());
			redisManager.setHost(hosts[0]);
			redisManager.setPort(Integer.parseInt(hosts[1]));
			redisManager.setPassword(shiroRedisProperties.getPassword());
			return redisManager;
		}
	}
	
	@Bean("redisManagerFactory")
	public BaseRedisManagerFactory redisManagerFactory(@Qualifier("redisManager") Object redisManager){
		BaseRedisManagerFactory redisManagerFactory = new BaseRedisManagerFactory();
		if(redisManager instanceof RedisManager){
			redisManagerFactory.setClusterRedisManager((RedisManager) redisManager);
		}else{
			redisManagerFactory.setSingleRedisManager((org.crazycake.shiro.RedisManager) redisManager);
		}
		return redisManagerFactory;
	}
	
	@Bean("cacheManager")
	public CacheManager cacheManager(@Qualifier("redisManager") Object redisManager){
		if(redisManager instanceof RedisManager){
			RedisCacheManager cacheManager = new RedisCacheManager();
			cacheManager.setRedisManager((RedisManager) redisManager);
			return cacheManager;
		}else{
			org.crazycake.shiro.RedisCacheManager cacheManager = new org.crazycake.shiro.RedisCacheManager();
			cacheManager.setRedisManager((org.crazycake.shiro.RedisManager) redisManager);
			return cacheManager;
		}
	}
	
	@Bean
	public ShiroSecurity shiroSecurity(ShiroProperties shiroProperties) {
		return new ShiroSecurity(shiroProperties);
	}
}
