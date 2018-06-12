package org.daijie.shiro.configure;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
import org.daijie.shiro.TokenCredentialsMatcher;
import org.daijie.shiro.UserAuthorizingRealm;
import org.daijie.shiro.filter.CredentialFilter;
import org.daijie.shiro.redis.RedisCacheManager;
import org.daijie.shiro.redis.RedisManager;
import org.daijie.shiro.redis.RedisOperator;
import org.daijie.shiro.session.ClusterRedisSession;
import org.daijie.shiro.session.quartz.QuartzSessionValidationScheduler2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * shiro session集群+redis集群bean配置
 * 1.1.0版本后已弃用，org.daijie.shiro.configure.ShiroConfigure支持单机和集群配置
 * @author daijie
 * @since 2017年6月5日
 */
@Configuration
public class ClusterShiroConfigure {
	
	@Value("${shiro.filterClassNames:}")
	private String filterClassNames;
	
	@Value("${shiro.isValidation:true}")
	private Boolean isValidation;
	
	@Value("${shiro.kissoEnable:true}")
	private Boolean kissoEnable;
	
	@Value("${shiro.loginUrl:/login}")
	private String loginUrl;
	
	@Value("${shiro.successUrl:/}")
	private String successUrl;
	
	@Value("${shiro.unauthorizedUrl:/403}")
	private String unauthorizedUrl;
	
	@Value("${shiro.filterChainDefinitions:}")
	private String filterChainDefinitions;
	
	@Value("${shiro.filterChainDefinitionMap:}")
	private String filterChainDefinitionMap;
	
	@Value("${shiro.redis.timeout:360000}")
	private Integer redisTimeout;
	
	@Value("${shiro.redis.connectionTimeout:1800}")
	private Integer connectionTimeout;
	
	@Value("${shiro.redis.maxAttempts:1}")
	private Integer maxAttempts;
	
	@Value("${shiro.redis.cluster.address:}")
	private String redisAddress;
	
	@Value("${shiro.redis.cluster.password:}")
	private String redisPassword;
	
	@Value("${shiro.sessionid:mysessionid}")
	private String sessionid;
	
	@Bean(name = "shiroFilter")
	@Primary
	public ShiroFilterFactoryBean initShiroFilterFactoryBean(@Qualifier("securityManager") SecurityManager securityManager){
		Map<String, Filter> filterMap = new HashMap<String, Filter>();
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		try {
			shiroFilterFactoryBean.setSecurityManager(securityManager);
			if(!StringUtils.isEmpty(this.filterClassNames)){
				for (String filterClassName : this.filterClassNames.split(",")) {
					if(filterClassName.trim().length() > 0){
						@SuppressWarnings("unchecked")
						Class<? extends Filter> cls = (Class<? extends Filter>) Class.forName(filterClassName);
						String name = filterClassName.substring(filterClassName.lastIndexOf(".")+1).replace("Filter", "");
						filterMap.put(name.substring(0,1).toLowerCase()+name.substring(1), cls.newInstance());
					}
				}
			}
			if(this.isValidation){
				filterMap.put("credential", new CredentialFilter());
			}
			shiroFilterFactoryBean.setFilters(filterMap);
			shiroFilterFactoryBean.setLoginUrl(this.loginUrl);
			shiroFilterFactoryBean.setSuccessUrl(this.successUrl);
			shiroFilterFactoryBean.setUnauthorizedUrl(this.unauthorizedUrl);
			Map<String, String> filterChainDefinitionMap = new HashMap<String, String>();
			if(!StringUtils.isEmpty(this.filterChainDefinitions)){
				for (String definition : this.filterChainDefinitions.split(",")) {
					if(definition.contains("=")){
						filterChainDefinitionMap.put(definition.split("=")[0], definition.split("=")[1]);
					}
				}
			}else if(!StringUtils.isEmpty(this.filterChainDefinitionMap)){
				ObjectMapper mapper = new ObjectMapper();
				@SuppressWarnings("unchecked")
				Map<String, String> map = mapper.readValue(this.filterChainDefinitionMap, Map.class);
				filterChainDefinitionMap = map;
			}else{
				filterChainDefinitionMap.put("*/**", "anon");
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
		authorizingRealm.setKissoEnable(this.kissoEnable);
		return authorizingRealm;
	}
	
	@Bean(name = "credentialsMatcher")
	@Primary
	public CredentialsMatcher initCredentialsMatcher(@Qualifier("redisSession") ClusterRedisSession redisSession){
		TokenCredentialsMatcher tokenCredentialsMatcher = new TokenCredentialsMatcher();
		tokenCredentialsMatcher.setRedisSession(redisSession);
		tokenCredentialsMatcher.setValidation(this.isValidation);
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
	public JedisCluster initJedisCluster(@Qualifier("genericObjectPoolConfig") GenericObjectPoolConfig genericObjectPoolConfig){
		String[] serverArray = this.redisAddress.split(",");
		Set<HostAndPort> nodes = new HashSet<>();
		for (String ipPort : serverArray) {
			String[] ipPortPair = ipPort.split(":");
			nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
		}
		return new JedisCluster(nodes, this.connectionTimeout, this.redisTimeout, this.maxAttempts, this.redisPassword, genericObjectPoolConfig);
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
		return new SimpleCookie(this.sessionid);
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
