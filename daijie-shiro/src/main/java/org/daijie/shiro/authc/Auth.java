package org.daijie.shiro.authc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.daijie.core.util.encrypt.RSAUtil;
import org.daijie.core.util.http.CookieUtil;
import org.daijie.core.util.http.HttpConversationUtil;
import org.daijie.shiro.session.ShiroRedisSession.Redis;

import com.baomidou.kisso.SSOHelper;
import com.xiaoleilu.hutool.bean.BeanUtil;

/**
 * 登录用户管理类
 * @author daijie_jay
 * @date 2017年11月15日
 */
public final class Auth {
	
	public static final String AUTH_KEY = "_AUTH_KEY_USER";
	
	public static final String AUTHC_KEY = "user";
	
	private static UserToken userToken;
	
	/**
	 * 设置登录用户权限集
	 * @param key
	 * @return
	 */
	public static void setPermissions(List<String> permissions){
		userToken.setPermissions(permissions);
	}
	
	/**
	 * 设置登录用户角色集
	 * @param key
	 * @return
	 */
	public static void setRoles(List<String> roles){
		userToken.setRoles(roles);
	}
	
	/**
	 * 用户登录
	 * @param username 登录账号
	 * @param pubPwd 登录公钥加密密码
	 * @param salt 登录账号加密盐
	 * @param saltPwd 登录账号盐加密后的密码
	 * @param authc 缓存登录账号信息的值
	 */
	public static void login(String username, String pubPwd, String salt, String saltPwd, Object authc){
		login(username, pubPwd, salt, saltPwd, AUTHC_KEY, authc);
	}
	
	/**
	 * 用户登录
	 * @param username 登录账号
	 * @param pubPwd 登录公钥加密密码
	 * @param salt 登录账号加密盐
	 * @param saltPwd 登录账号盐加密后的密码
	 * @param authcKey 缓存登录账号信息的键
	 * @param authc 缓存登录账号信息的值
	 */
	public static void login(String username, String pubPwd, String salt, String saltPwd, String authcKey, Object authc){
		userToken = new UserToken();
		userToken.setAuthc(authc);
		AuthorizationToken token = new AuthorizationToken(username, 
				saltPwd, 
				pubPwd, salt, authcKey, userToken);
		token.setRememberMe(true);  
		Subject subject = SecurityUtils.getSubject();
		subject.login(token);
		subject.isAuthenticated();
	}
	
	/**
	 * 登出，清除用户缓存和cookie
	 */
	public static void logOut(){
		Session session = Redis.getSession();
		if(session.getAttribute("kissoEnable") != null && (boolean) session.getAttribute("kissoEnable")){
			SSOHelper.clearLogin(HttpConversationUtil.getRequest(), HttpConversationUtil.getResponse());
		}else{
			CookieUtil.set(HttpConversationUtil.TOKEN_NAME, session.getId().toString(), 0);
		}
		Redis.deleteSession();
	}
	
	public static String getAuthcKey(){
		return (String) Redis.getAttribute(AUTH_KEY);
	}

	/**
	 * 获取登录用户基本信息
	 * @param className
	 * @return
	 */
	public static <T> T getAuthc(Class<T> className){
		return getAuthc(getAuthcKey(), className);
	}
	
	/**
	 * 获取登录用户基本信息
	 * @param key
	 * @param className
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getAuthc(String key, Class<T> className){
		Object value = Redis.getAttribute(key);
		if(value instanceof UserToken){
			Object authc = ((UserToken) value).getAuthc();
			if(authc instanceof Map){
				return BeanUtil.mapToBean((Map<?, ?>)authc, className, true);
			}
			return (T) authc;
		}
		return null;
	}
	
	/**
	 * 获取登录用户权限集
	 * @param key
	 * @return
	 */
	public static List<String> getPermissions(){
		return getPermissions(getAuthcKey());
	}
	
	/**
	 * 获取登录用户权限集
	 * @param key
	 * @return
	 */
	public static List<String> getPermissions(String key){
		Object value = Redis.getAttribute(key);
		if(value instanceof UserToken){
			return ((UserToken) value).getPermissions();
		}
		return new ArrayList<String>();
	}
	
	/**
	 * 获取登录用户角色集
	 * @param key
	 * @return
	 */
	public static List<String> getRoles(){
		return getRoles(getAuthcKey());
	}
	
	/**
	 * 获取登录用户角色集
	 * @param key
	 * @return
	 */
	public static List<String> getRoles(String key){
		Object value = Redis.getAttribute(key);
		if(value instanceof UserToken){
			return ((UserToken) value).getRoles();
		}
		return new ArrayList<String>();
	}
	
	/**
	 * 刷新登录用户基本信息
	 * @param key
	 * @return
	 */
	public static void refreshAuthc(Object authc){
		refreshAuthc(getAuthcKey(), authc);
	}
	
	/**
	 * 刷新登录用户基本信息
	 * @param key
	 * @return
	 */
	public static void refreshAuthc(String key, Object authc){
		Object value = Redis.getAttribute(key);
		if(value instanceof UserToken){
			userToken = (UserToken) value;
			userToken.setAuthc(authc);
			Redis.setAttribute(key, userToken);
		}
	}
	
	/**
	 * 刷新登录用户权限集
	 * @param key
	 * @return
	 */
	public static void refreshPermissions(List<String> permissions){
		refreshPermissions(getAuthcKey(), permissions);
	}
	
	/**
	 * 刷新登录用户权限集
	 * @param key
	 * @return
	 */
	public static void refreshPermissions(String key, List<String> permissions){
		Object value = Redis.getAttribute(key);
		if(value instanceof UserToken){
			userToken = (UserToken) value;
			userToken.setPermissions(permissions);
			Redis.setAttribute(key, userToken);
		}
	}
	
	/**
	 * 刷新登录用户角色集
	 * @param key
	 * @return
	 */
	public static void refreshRoles(List<String> roles){
		refreshRoles(getAuthcKey(), roles);
	}
	
	/**
	 * 刷新登录用户角色集
	 * @param key
	 * @return
	 */
	public static void refreshRoles(String key, List<String> roles){
		Object value = Redis.getAttribute(key);
		if(value instanceof UserToken){
			userToken = (UserToken) value;
			userToken.setRoles(roles);
			Redis.setAttribute(key, userToken);
		}
	}
	
	/**
	 * 初始化一对公钥与私钥
	 */
	public static void initSecretKey(){
		RSAUtil.init();
		Redis.setAttribute(ShiroConstants.RSA_PUBLIC_KEY + Redis.getSession().getId(), RSAUtil.getPubKey());
		Redis.setAttribute(ShiroConstants.RSA_PRIVATE_KEY + Redis.getSession().getId(), RSAUtil.getPriKey());
	}
	
	/**
	 * 获取缓存中的公钥，如果没有将初始化一对公钥与私钥
	 * @return
	 */
	public static String getPublicKey(){
		Object key = Redis.getAttribute(ShiroConstants.RSA_PUBLIC_KEY + Redis.getSession().getId());
		if(key != null){
			return (String) key;
		}else{
			initSecretKey();
			return RSAUtil.getPubKey();
		}
	}

	/**
	 * 获取缓存中的私钥
	 * @return
	 */
	public static String getPrivateKey(){
		Object key = Redis.getAttribute(ShiroConstants.RSA_PRIVATE_KEY + Redis.getSession().getId());
		if(key != null){
			return (String) key;
		}
		return null;
	}
}
