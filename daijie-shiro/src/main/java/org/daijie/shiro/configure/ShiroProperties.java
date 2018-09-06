package org.daijie.shiro.configure;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.shiro.web.servlet.AdviceFilter;
import org.daijie.shiro.session.ShiroRedisSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * shiro相关属性配置
 * @author daijie_jay
 * @since 2018年1月2日
 */
@ConfigurationProperties(prefix = "shiro")
public class ShiroProperties {

	private String filterClassNames;
	
	private Boolean isValidation = true;
	
	private Boolean kissoEnable = true;
	
	private String loginUrl = "/login";
	
	private String successUrl = "/";
	
	private String unauthorizedUrl = "/403";
	
	private String filterChainDefinitions;
	
	private String filterChainDefinitionMap;
	
	private long sesseionTimeOut = 360000;
	
	/**
	 * session名称
	 */
	private String sessionid = ShiroRedisSession.TOKEN_NAME;
	
	@Value("${kisso.config.cookieName:mysessionid}")
	private String cookieName;
	
	/**
	 * 过滤器对应的路径
	 */
	private Map<String, String> matcher = new ConcurrentHashMap<String, String>();
	
	/**
	 * 过滤器加载配置
	 */
	private Class<AdviceFilter>[] filterClasses;
	
	/**
	 * 初始化凭证的请求路径
	 * 目前用于初始化一对非对象密钥
	 */
	private String[] initCredentialUrl;

	public String getFilterClassNames() {
		return filterClassNames;
	}

	public void setFilterClassNames(String filterClassNames) {
		this.filterClassNames = filterClassNames;
	}

	public Boolean getIsValidation() {
		return isValidation;
	}

	public void setIsValidation(Boolean isValidation) {
		this.isValidation = isValidation;
	}

	public Boolean getKissoEnable() {
		return kissoEnable;
	}

	public void setKissoEnable(Boolean kissoEnable) {
		this.kissoEnable = kissoEnable;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getUnauthorizedUrl() {
		return unauthorizedUrl;
	}

	public void setUnauthorizedUrl(String unauthorizedUrl) {
		this.unauthorizedUrl = unauthorizedUrl;
	}

	public String getFilterChainDefinitions() {
		return filterChainDefinitions;
	}

	public void setFilterChainDefinitions(String filterChainDefinitions) {
		this.filterChainDefinitions = filterChainDefinitions;
	}

	public String getFilterChainDefinitionMap() {
		return filterChainDefinitionMap;
	}

	public void setFilterChainDefinitionMap(String filterChainDefinitionMap) {
		this.filterChainDefinitionMap = filterChainDefinitionMap;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public Map<String, String> getMatcher() {
		return matcher;
	}

	public void setMatcher(Map<String, String> matcher) {
		this.matcher = matcher;
	}

	public Class<AdviceFilter>[] getFilterClasses() {
		return filterClasses;
	}

	public void setFilterClasses(Class<AdviceFilter>[] filterClasses) {
		this.filterClasses = filterClasses;
	}

	public String[] getInitCredentialUrl() {
		return initCredentialUrl;
	}

	public void setInitCredentialUrl(String[] initCredentialUrl) {
		this.initCredentialUrl = initCredentialUrl;
	}

	public String getCookieName() {
		return cookieName;
	}

	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}

	public long getSesseionTimeOut() {
		return sesseionTimeOut;
	}

	public void setSesseionTimeOut(long sesseionTimeOut) {
		this.sesseionTimeOut = sesseionTimeOut;
	}
}
