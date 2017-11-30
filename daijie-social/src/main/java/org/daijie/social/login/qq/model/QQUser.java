package org.daijie.social.login.qq.model;

import org.daijie.social.login.LoginResult;

/**
 * QQ用户统一ID
 * @author daijie_jay
 * @date 2017年11月30日
 */
public class QQUser implements LoginResult {
	
	private String appid;
	
	private String openid;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	@Override
	public Boolean getResult() {
		return true;
	}
}
