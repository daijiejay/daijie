package org.daijie.social.login.sina.model;

import org.daijie.social.login.LoginResult;

/**
 * 新浪微博用户统一ID
 * @author daijie_jay
 * @since 2017年11月30日
 */
public class SinaUser implements LoginResult {

	/**
	 * 	授权用户的uid。
	 */
	private String uid;

	/**
	 * access_token所属的应用appkey。
	 */
	private String appkey;

	/**
	 * 用户授权的scope权限。
	 */
	private String scope;

	/**
	 * access_token的创建时间，从1970年到创建时间的秒数。
	 */
	private String create_at;
	
	/**
	 * access_token的剩余时间，单位是秒数
	 */
	private String expire_in;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getCreate_at() {
		return create_at;
	}

	public void setCreate_at(String create_at) {
		this.create_at = create_at;
	}

	public String getExpire_in() {
		return expire_in;
	}

	public void setExpire_in(String expire_in) {
		this.expire_in = expire_in;
	}

	@Override
	public Boolean getResult() {
		return true;
	}
}
