package org.daijie.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.session.Session;
import org.daijie.core.util.encrypt.PasswordUtil;
import org.daijie.core.util.encrypt.RSAUtil;
import org.daijie.shiro.authc.AuthorizationToken;
import org.daijie.shiro.authc.ShiroConstants;
import org.daijie.shiro.session.ClusterRedisSession;
import org.daijie.shiro.session.RedisSession;
import org.daijie.shiro.session.RedisSessionFactory;
import org.daijie.shiro.session.ShiroRedisSession.Redis;

/**
 * 用户登录令牌匹配器
 * @author daijie
 * @since 2017年6月22日
 */
public class TokenCredentialsMatcher implements CredentialsMatcher {
	
	/**
	 * 是否开通匹配器
	 */
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
		if(redisSession instanceof RedisSession){
			session = ((RedisSession) redisSession).readSession(Redis.getToken());
		}else if(redisSession instanceof ClusterRedisSession){
			session = ((ClusterRedisSession) redisSession).readSession(Redis.getToken());
		}
		String privateKey = (String) session.getAttribute(ShiroConstants.RSA_PRIVATE_KEY + session.getId());
		if(StringUtils.isBlank(privateKey)){
			throw new AuthenticationException("数据已经过期。请刷新重试!");
		}
		try {
			RSAUtil.set(null, privateKey);
			String password = RSAUtil.decryptByPriKey(new String(authcToken.getPubEncryptPassword()));
			password = PasswordUtil.generatePassword(password, authcInfo.getCredentialsSalt().getBytes());
			if(!password.equals(new String((char[]) authcInfo.getCredentials()))){
				throw new AuthenticationException("密码错误!");
			}
		}  catch (Exception e) {
			throw new AuthenticationException("密码错误!");
		} finally {
			session.removeAttribute(ShiroConstants.RSA_PRIVATE_KEY + session.getId());
		}
		return true;
	}

	public boolean isValidation() {
		return isValidation;
	}

	public void setValidation(boolean isValidation) {
		this.isValidation = isValidation;
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
