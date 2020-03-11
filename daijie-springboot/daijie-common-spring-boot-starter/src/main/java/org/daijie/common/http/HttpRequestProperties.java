package org.daijie.common.http;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * swagger属性配置
 * @author daijie_jay
 * @since 2017年12月13日
 */
@ConfigurationProperties("http")
public class HttpRequestProperties {
	
	private Boolean remoteAjaxEanble = false;
	
	private String accessControlAllowOrigin[] = new String[]{"*"};
	
	private String accessControlAllowMethods = "GET,POST,PUT,DELETE,OPTIONS";
	
	private String accessControlAllowHeaders = "token,Content-Type,Access-Control-Allow-Headers,Access-Control-Allow-Origin,Authorization,X-Requested-With";
	
	private Boolean bodyByParamEanble = false;
	
	private String bodyByParamMethods = "GET,POST,PUT,DELETE";

	public Boolean getRemoteAjaxEanble() {
		return remoteAjaxEanble;
	}

	public void setRemoteAjaxEanble(Boolean remoteAjaxEanble) {
		this.remoteAjaxEanble = remoteAjaxEanble;
	}

	public String[] getAccessControlAllowOrigin() {
		return accessControlAllowOrigin;
	}

	public void setAccessControlAllowOrigin(String[] accessControlAllowOrigin) {
		this.accessControlAllowOrigin = accessControlAllowOrigin;
	}

	public String getAccessControlAllowMethods() {
		return accessControlAllowMethods;
	}

	public void setAccessControlAllowMethods(String accessControlAllowMethods) {
		this.accessControlAllowMethods = accessControlAllowMethods;
	}

	public String getAccessControlAllowHeaders() {
		return accessControlAllowHeaders;
	}

	public void setAccessControlAllowHeaders(String accessControlAllowHeaders) {
		this.accessControlAllowHeaders = accessControlAllowHeaders;
	}

	public Boolean getBodyByParamEanble() {
		return bodyByParamEanble;
	}

	public void setBodyByParamEanble(Boolean bodyByParamEanble) {
		this.bodyByParamEanble = bodyByParamEanble;
	}

	public String getBodyByParamMethods() {
		return bodyByParamMethods;
	}

	public void setBodyByParamMethods(String bodyByParamMethods) {
		this.bodyByParamMethods = bodyByParamMethods.toUpperCase();
	}
	
}
