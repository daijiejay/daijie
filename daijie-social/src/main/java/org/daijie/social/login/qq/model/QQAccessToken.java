package org.daijie.social.login.qq.model;

import org.daijie.social.login.LoginResult;

/**
 * 百度认证权限的实体
 * @author daijie_jay
 * @since 2017年11月28日
 */
public class QQAccessToken implements LoginResult {

	private String access_token;
	
	private Long expires_in;
	
	private String refresh_token;
	
	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public Long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(Long expires_in) {
		this.expires_in = expires_in;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	@Override
	public Boolean getResult() {
		return true;
	}
}
