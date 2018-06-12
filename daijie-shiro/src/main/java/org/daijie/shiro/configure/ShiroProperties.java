package org.daijie.shiro.configure;

import java.util.Map;

import org.apache.shiro.web.servlet.AdviceFilter;
import org.jboss.netty.util.internal.ConcurrentHashMap;
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
	
	private String sessionid = "mysessionid";
	
	private Map<String, String> matcher = new ConcurrentHashMap<String, String>();
	
	private Class<AdviceFilter>[] filterClasses;

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
}
