package org.daijie.shiro;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.daijie.core.util.HttpRequestUtil;
import org.daijie.core.util.PasswordUtils;
import org.daijie.core.util.RSAUtils;
import org.daijie.shiro.authc.AuthorizationToken;
import org.daijie.shiro.authc.ShiroConstants;
import org.daijie.shiro.session.ClusterRedisSession;
import org.daijie.shiro.session.RedisSession;

public class TokenCredentialsMatcher implements CredentialsMatcher {

	private SessionDAO redisSession;
	
	private boolean isValidation = false;
	
	private Session session;
	
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token,
			AuthenticationInfo info) {
		if(!isValidation){
			return true;
		}
		AuthorizationToken authcToken = (AuthorizationToken) token;
		SimpleAuthenticationInfo authcInfo = (SimpleAuthenticationInfo) info;
		if(redisSession instanceof RedisSession || redisSession instanceof ClusterRedisSession){
			session = redisSession.readSession(HttpRequestUtil.getToken());
		}
		String privateKey = (String) session.getAttribute(ShiroConstants.RSA_PRIVATE_KEY + session.getId());
		if(StringUtils.isBlank(privateKey)){
			throw new AuthenticationException("The data has expired. Please refresh retry!");
		}
		try {
			String password=new String(RSAUtils.decryptByPrivateKeyForJS(new String(authcToken.getPassword()).getBytes(), privateKey));
			password=PasswordUtils.generatePassword(password, authcInfo.getCredentialsSalt().getBytes());
			if(password.equals(authcInfo.getCredentials())){
				return true;
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new AuthenticationException("Validation failure!");
		} finally {
			session.removeAttribute(ShiroConstants.RSA_PRIVATE_KEY + session.getId());
		}
		return true;
	}

	public SessionDAO getRedisSession() {
		return redisSession;
	}

	public void setRedisSession(SessionDAO redisSession) {
		this.redisSession = redisSession;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

}
