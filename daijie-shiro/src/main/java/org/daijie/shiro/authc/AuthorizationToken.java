package org.daijie.shiro.authc;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 用于shiro用户登录的实体类
 * @author daijie
 * @date 2017年6月22日
 */
@SuppressWarnings("serial")
public class AuthorizationToken extends UsernamePasswordToken {

	private UserToken user;
	
	/**
	 * 加密盐
	 */
	private String salt;
	
	private String pubEncryptPassword;
	
	private String authcKey = "user";
	
	public AuthorizationToken(String username, String password, String pubEncryptPassword, String authcKey, UserToken user){
		super(username, password);
		this.pubEncryptPassword = pubEncryptPassword;
		this.authcKey = authcKey;
		this.user = user;
	}
	
	public AuthorizationToken(String username, String password, String pubEncryptPassword, String salt, String authcKey, UserToken user){
		super(username, password);
		this.salt = salt;
		this.pubEncryptPassword = pubEncryptPassword;
		this.authcKey = authcKey;
		this.user = user;
	}

	public Object getUser() {
		return user;
	}

	public void setUser(UserToken user) {
		this.user = user;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPubEncryptPassword() {
		return pubEncryptPassword;
	}

	public void setPubEncryptPassword(String pubEncryptPassword) {
		this.pubEncryptPassword = pubEncryptPassword;
	}

	public String getAuthcKey() {
		return authcKey;
	}

	public void setAuthcKey(String authcKey) {
		this.authcKey = authcKey;
	}
}
