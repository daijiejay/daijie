package org.daijie.social.login.baidu;

/**
 * 百度登录常量配置
 * @author daijie_jay
 * @since 2017年11月28日
 */
public class BaiduLoginConstants {

	public static final String LOGIN_CALLBACK = "baidu/login/Callback";
	
	public static final String HOST_OPEN = "https://openapi.baidu.com";
	
	public static final String AUTH = "/oauth/2.0/authorize";
	
	public static final String ACCESS_TOKEN = "/oauth/2.0/token";
	
	public static final String OPENID = "/rest/2.0/passport/users/getLoggedInUser";
	
	public static final String USERINFO = "/rest/2.0/passport/users/getInfo";
}
