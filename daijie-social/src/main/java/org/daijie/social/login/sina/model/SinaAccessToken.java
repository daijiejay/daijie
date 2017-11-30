package org.daijie.social.login.sina.model;

import org.daijie.social.login.LoginResult;

/**
 * 新浪微博认证权限的实体
 * @author daijie_jay
 * @date 2017年11月28日
 */
public class SinaAccessToken implements LoginResult {

	private String access_token;
	
	private Long expires_in;
	
	private String remind_in;
	
	private String uid;

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

	public String getRemind_in() {
		return remind_in;
	}

	public void setRemind_in(String remind_in) {
		this.remind_in = remind_in;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Override
	public Boolean getResult() {
		return true;
	}
}
