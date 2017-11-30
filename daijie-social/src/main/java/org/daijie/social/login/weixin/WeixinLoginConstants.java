package org.daijie.social.login.weixin;

/**
 * 微信登录常量配置
 * @author daijie_jay
 * @date 2017年11月28日
 */
public class WeixinLoginConstants {

	public static final String LOGIN_CALLBACK = "weixin/login/Callback";
	
	public static final String HOST_API = "https://api.weixin.qq.com";
	
	public static final String HOST_OPEN = "https://open.weixin.qq.com";

	public static final String TOKEN = "/cgi-bin/token";

	public static final String GET_CALLCACK_IP = "/cgi-bin/getcallbackip";

	public static final String GET_MENU = "/cgi-bin/menu/get";

	public static final String CREATE_MENU = "/cgi-bin/menu/create";

	/**
	 * 临时code获取accessToken
	 */
	public static final String ACCESS_TOKEN = "/sns/oauth2/access_token";
	
	/**
	 * 刷新延迟accessToken有效期
	 */
	public static final String REFRESH_TOKEN = "/sns/oauth2/refresh_token";
	
	/**
	 * 验证accessToken有效
	 */
	public static final String VERIFY_AUTH = "/sns/auth";

	/**
	 * 获取用户信息
	 */
	public static final String USER_INFO = "/sns/userinfo";
	
	/**
	 * web端扫码获取code
	 */
	public static final String QR_CONNECT = "/connect/qrconnect";
	
	/**
	 * wap端调用微信获取code
	 */
	public static final String AUTHORIZE = "/connect/oauth2/authorize";
}
