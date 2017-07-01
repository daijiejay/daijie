package org.daijie.shiro.configure;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

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
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.daijie.core.util.PropertiesLoader;
import org.daijie.shiro.TokenCredentialsMatcher;
import org.daijie.shiro.UserAuthorizingRealm;
import org.daijie.shiro.filter.CredentialFilter;
import org.daijie.shiro.session.RedisSession;
import org.daijie.shiro.session.quartz.QuartzSessionValidationScheduler2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author daijie
 * @date 2017年6月5日
 * shiro session集群+redis配置
 * 
 */
public class ShiroConfigure {
	
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
						filterMap.put(filterClassName.substring(filterClassName.lastIndexOf(".")), cls.newInstance());
					}
				}
			}
			if(loader.getBoolean("shiro.isValidation") != null && loader.getBoolean("shiro.isValidation")){
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
			if(loader.getBoolean("shiro.isValidation") != null && loader.getBoolean("shiro.isValidation")){
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
	
	@Bean(name = "redisManager")
	@Primary
	public RedisManager initRedisManager(){
		RedisManager redisManager = new RedisManager();
		redisManager.setHost(loader.getProperty("shiro.redis.host", "127.0.0.1"));
		redisManager.setPort(loader.getInteger("shiro.redis.port", 6379));
		redisManager.setPassword(loader.getProperty("shiro.redis.password", ""));
		redisManager.setTimeout(loader.getInteger("shiro.redis.timeout", 360000));
		redisManager.setExpire(loader.getInteger("shiro.redis.expire", 1800));
		return redisManager;
	}
	
	@Bean(name = "lifecycleBeanPostProcessor")
	@Primary
	public LifecycleBeanPostProcessor initLifecycleBeanPostProcessor(){
		return new LifecycleBeanPostProcessor();
	}
	
	@Bean(name = "redisSession")
	@Primary
	public RedisSession initRedisSession(@Qualifier("redisManager") RedisManager redisManager, @Qualifier("sessionIdGenerator") SessionIdGenerator sessionIdGenerator){
		RedisSession redisSession = new RedisSession();
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
	public SessionManager initSessionManager(@Qualifier("redisSession") RedisSession redisSession, @Qualifier("simpleCookie") SimpleCookie simpleCookie,
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
	public SessionManager initSessionManager(@Qualifier("redisSession") RedisSession redisSession, 
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
