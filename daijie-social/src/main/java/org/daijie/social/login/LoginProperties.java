package org.daijie.social.login;

/**
 * 登录属性配置初始化
 * @author daijie_jay
 * @since 2017年11月28日
 */
public class LoginProperties {

	protected String appid;

	protected String appsecret;
    
	protected String redirectUri;
    
	protected String errorUri;
    
	protected String callbackUri;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAppsecret() {
		return appsecret;
	}

	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public String getErrorUri() {
		return errorUri;
	}

	public void setErrorUri(String errorUri) {
		this.errorUri = errorUri;
	}

	public String getCallbackUri() {
		return callbackUri;
	}

	public void setCallbackUri(String callbackUri) {
		this.callbackUri = callbackUri;
	}

}
