package org.daijie.shiro.authc;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.kisso.SSOHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.daijie.core.util.encrypt.RSAUtil;
import org.daijie.shiro.exception.UserExpireException;
import org.daijie.shiro.session.ShiroRedisSession;
import org.daijie.shiro.session.ShiroRedisSession.Redis;
import org.daijie.shiro.util.CookieUtil;
import org.daijie.shiro.util.HttpConversationUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 登录用户工具类
 * @author daijie_jay
 * @since 2017年11月15日
 */
public final class Auth {
	
	public static final String AUTH_KEY = "_AUTH_KEY_USER";
	
	public static final String AUTHC_KEY = "user";
	
	private static UserToken userToken;
	
	/**
	 * 设置登录用户权限集
	 * @param permissions 权限集合
	 */
	public static void setPermissions(List<String> permissions){
		if (userToken == null) {
			userToken = new UserToken();
		}
		userToken.setPermissions(permissions);
	}
	
	/**
	 * 设置登录用户权限集
	 * @param permissions 权限集合
	 */
	public static void setPermissions(String... permissions){
		setPermissions(Arrays.asList(permissions));
	}
	
	/**
	 * 设置登录用户角色集
	 * @param roles 角色集合
	 */
	public static void setRoles(List<String> roles){
		if (userToken == null) {
			userToken = new UserToken();
		}
		userToken.setRoles(roles);
	}
	
	/**
	 * 设置登录用户角色集
	 * @param roles 角色集合
	 */
	public static void setRoles(String... roles){
		setRoles(Arrays.asList(roles));
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
		Redis.isExpire(false);
		if (userToken == null) {
			userToken = new UserToken();
		}
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
		if (session != null) {
			if(session.getAttribute("kissoEnable") != null && (boolean) session.getAttribute("kissoEnable")){
				SSOHelper.clearLogin(HttpConversationUtil.getRequest(), HttpConversationUtil.getResponse());
			}
			Redis.deleteSession();
			CookieUtil.set(ShiroRedisSession.token, session.getId().toString(), 0);
		}
	}
	
	/**
	 * 是否登录
	 * @return boolean
	 */
	public static boolean isLogin() {
		return getAuthcKey() != null && Redis.getAttribute(getAuthcKey()) != null;
	}
	
	/**
	 * 获取登录用户redis中的key
	 * @return String
	 */
	public static String getAuthcKey(){
		return (String) Redis.getAttribute(AUTH_KEY);
	}

	/**
	 * 获取登录用户基本信息
	 * 如果没有获取到，将抛出用户过期UserExpireException异常
	 * @param <T> 用户类型
	 * @param className 用户对象类型
	 * @return Object
	 */
	public static <T> T getAuthc(Class<T> className){
		return getAuthc(getAuthcKey(), className);
	}
	
	/**
	 * 获取登录用户基本信息
	 * 如果没有获取到，将抛出用户过期UserExpireException异常
	 * @param <T> 用户类型
	 * @param key 用户键
	 * @param className 用户对象类型
	 * @return Object
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
		throw new UserExpireException();
	}
	
	/**
	 * 获取登录用户权限集
	 * @return List
	 */
	public static List<String> getPermissions(){
		return getPermissions(getAuthcKey());
	}
	
	/**
	 * 获取登录用户权限集
	 * @param key 用户键
	 * @return List
	 */
	public static List<String> getPermissions(String key){
		Object value = Redis.getAttribute(key);
		if(value instanceof UserToken){
			return ((UserToken) value).getPermissions();
		}
		return new ArrayList<String>();
	}
	
	/**
	 * 验证用户是否有该权限
	 * @param permissions 权限集
	 * @return boolean
	 */
	public static boolean hasAnyPermissions(String... permissions){
		List<String> list = getPermissions();
		list.retainAll(Arrays.asList(permissions));
		return list.size() > 0;
	}
	
	/**
	 * 获取登录用户角色集
	 * @return List
	 */
	public static List<String> getRoles(){
		return getRoles(getAuthcKey());
	}
	
	/**
	 * 获取登录用户角色集
	 * @param key 用户键
	 * @return List
	 */
	public static List<String> getRoles(String key){
		Object value = Redis.getAttribute(key);
		if(value instanceof UserToken){
			return ((UserToken) value).getRoles();
		}
		return new ArrayList<String>();
	}
	
	/**
	 * 验证用户是否有该角色
	 * @param roles 角色集
	 * @return boolean
	 */
	public static boolean hasAnyRoles(String... roles){
		List<String> list = getRoles();
		list.retainAll(Arrays.asList(roles));
		return list.size() > 0;
	}
	
	/**
	 * 刷新登录用户基本信息
	 * @param authc 用户基本信息对象
	 */
	public static void refreshAuthc(Object authc){
		refreshAuthc(getAuthcKey(), authc);
	}
	
	/**
	 * 刷新登录用户基本信息
	 * @param key 用户键
	 * @param authc 用户基本信息对象
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
	 * @param permissions 用户权限
	 */
	public static void refreshPermissions(List<String> permissions){
		refreshPermissions(getAuthcKey(), permissions);
	}
	
	/**
	 * 刷新登录用户权限集
	 * @param key 用户键
	 * @param permissions 用户权限
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
	 * 刷新登录用户权限集
	 * @param permissions 用户权限
	 */
	public static void refreshPermissions(String... permissions){
		refreshPermissions(Arrays.asList(permissions));
	}
	
	/**
	 * 刷新登录用户权限集
	 * @param key 用户键
	 * @param permissions 用户权限
	 */
	public static void refreshPermissions(String key, String... permissions){
		refreshPermissions(key, Arrays.asList(permissions));
	}
	
	/**
	 * 刷新登录用户角色集
	 * @param roles 角色集
	 */
	public static void refreshRoles(List<String> roles){
		refreshRoles(getAuthcKey(), roles);
	}
	
	/**
	 * 刷新登录用户角色集
	 * @param key 用户键
	 * @param roles 角色集
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
	 * 刷新登录用户角色集
	 * @param roles 角色集
	 */
	public static void refreshRoles(String... roles){
		refreshRoles(Arrays.asList(roles));
	}
	
	/**
	 * 刷新登录用户角色集
	 * @param key 用户键
	 * @param roles 角色集
	 */
	public static void refreshRoles(String key, String... roles){
		refreshRoles(key, Arrays.asList(roles));
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
	 * @return String
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
	 * @return String
	 */
	public static String getPrivateKey(){
		Object key = Redis.getAttribute(ShiroConstants.RSA_PRIVATE_KEY + Redis.getSession().getId());
		if(key != null){
			return (String) key;
		}
		return null;
	}
	
	/**
	 * 用公钥解析公钥加密的密码
	 * @param priKeyCryptograph 私钥加密的密文
	 * @return String
	 * @throws Exception 解密异常
	 */
	public static String decryptByPubKey(String priKeyCryptograph) throws Exception {
		RSAUtil.set(null, getPublicKey());
		String password = RSAUtil.decryptByPubKey(priKeyCryptograph);
		return password;
	}
	
	/**
	 * 用私钥解析公钥加密的密码
	 * @param pubKeyCryptograph 公钥加密的密文
	 * @return String
	 * @throws Exception 解密异常
	 */
	public static String decryptByPriKey(String pubKeyCryptograph) throws Exception {
		RSAUtil.set(null, getPrivateKey());
		String password = RSAUtil.decryptByPriKey(pubKeyCryptograph);
		return password;
	}
}
