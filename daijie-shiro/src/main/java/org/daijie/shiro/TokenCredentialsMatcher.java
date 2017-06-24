package org.daijie.shiro;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.session.Session;
import org.daijie.core.util.encrypt.PasswordUtils;
import org.daijie.core.util.encrypt.RSAUtils;
import org.daijie.core.util.http.HttpConversationUtil;
import org.daijie.shiro.authc.AuthorizationToken;
import org.daijie.shiro.authc.ShiroConstants;
import org.daijie.shiro.session.ClusterRedisSession;
import org.daijie.shiro.session.RedisSession;
import org.daijie.shiro.session.RedisSessionFactory;

/**
 * 用户登录令牌匹配器
 * @author daijie
 * @date 2017年6月22日
 */
public class TokenCredentialsMatcher implements CredentialsMatcher {
	
	private boolean isValidation = false;

	private RedisSessionFactory redisSession;
	
	private Session session;
	
	/**
	 * 用户密码匹配
	 */
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token,
			AuthenticationInfo info) {
		if(!isValidation){
			return true;
		}
		AuthorizationToken authcToken = (AuthorizationToken) token;
		SimpleAuthenticationInfo authcInfo = (SimpleAuthenticationInfo) info;
		if(redisSession instanceof RedisSession || redisSession instanceof ClusterRedisSession){
			session = ((RedisSession) redisSession).readSession(HttpConversationUtil.getToken());
		}else{
			session = ((ClusterRedisSession) redisSession).readSession(HttpConversationUtil.getToken());
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

	public RedisSessionFactory getRedisSession() {
		return redisSession;
	}

	public void setRedisSession(RedisSessionFactory redisSession) {
		this.redisSession = redisSession;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

}
