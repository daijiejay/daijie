package org.daijie.social.login;

import org.daijie.core.util.ApplicationContextHolder;
import org.daijie.social.login.ali.AliLoginTool;
import org.daijie.social.login.ali.AliLoignProperties;
import org.daijie.social.login.ali.callback.AliLoginCallback;
import org.daijie.social.login.ali.service.AliLoginService;
import org.daijie.social.login.baidu.BaiduLoginTool;
import org.daijie.social.login.baidu.BaiduLoignProperties;
import org.daijie.social.login.baidu.callback.BaiduLoginCallback;
import org.daijie.social.login.baidu.service.BaiduLoginService;
import org.daijie.social.login.qq.QQLoginTool;
import org.daijie.social.login.qq.QQLoignProperties;
import org.daijie.social.login.qq.callback.QQLoginCallback;
import org.daijie.social.login.qq.service.QQLoginService;
import org.daijie.social.login.sina.SinaLoginTool;
import org.daijie.social.login.sina.SinaLoignProperties;
import org.daijie.social.login.sina.callback.SinaLoginCallback;
import org.daijie.social.login.sina.service.SinaLoginService;
import org.daijie.social.login.weixin.WeixinLoginTool;
import org.daijie.social.login.weixin.WeixinLoignProperties;
import org.daijie.social.login.weixin.callback.WeixinLoginCallback;
import org.daijie.social.login.weixin.service.WeixinLoginService;

@SuppressWarnings("unchecked")
public class LoginTool {

	/**
	 * 获取第三方登录属性配置
	 * @return
	 */
	public static <T extends LoginProperties> T getProperties(SocialLoginEnum socialLogin){
		switch (socialLogin) {
		case ALI:
			return (T) ApplicationContextHolder.getBean("aliLoginProperties", AliLoignProperties.class);
		case BAIDU:
			return (T) ApplicationContextHolder.getBean("baiduLoginProperties", BaiduLoignProperties.class);
		case QQ:
			return (T) ApplicationContextHolder.getBean("qqLoginProperties", QQLoignProperties.class);
		case SINA:
			return (T) ApplicationContextHolder.getBean("sinaLoginProperties", SinaLoignProperties.class);
		case WEIXIN:
			return (T) ApplicationContextHolder.getBean("weixinLoginProperties", WeixinLoignProperties.class);
		}
		return null;
	}
	
	private static <T extends LoginService> T getService(SocialLoginEnum socialLogin){
		switch (socialLogin) {
		case ALI:
			return (T) ApplicationContextHolder.getBean("aliLoginService", AliLoginService.class);
		case BAIDU:
			return (T) ApplicationContextHolder.getBean("baiduLoginService", BaiduLoginService.class);
		case QQ:
			return (T) ApplicationContextHolder.getBean("qqLoginService", QQLoginService.class);
		case SINA:
			return (T) ApplicationContextHolder.getBean("sinaLoginService", SinaLoginService.class);
		case WEIXIN:
			return (T) ApplicationContextHolder.getBean("weixinLoginService", WeixinLoginService.class);
		}
		return null;
	}
	
	/**
	 * 第三方登录
	 * @param appAuthCode 第三方临时code
	 * @param socialLogin 第三方名称
	 * @param callback 回调函数
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String login(String appAuthCode, SocialLoginEnum socialLogin, LoginCallback callback){
		switch (socialLogin) {
		case ALI:
			return AliLoginTool.login(appAuthCode, (AliLoginCallback) callback);
		case BAIDU:
			return BaiduLoginTool.login(appAuthCode, (BaiduLoginCallback) callback);
		case QQ:
			return QQLoginTool.login(appAuthCode, (QQLoginCallback) callback);
		case SINA:
			return SinaLoginTool.login(appAuthCode, (SinaLoginCallback) callback);
		case WEIXIN:
			return WeixinLoginTool.login(appAuthCode, (WeixinLoginCallback) callback);
		}
		return null;
	}
	
	/**
	 * 加载第三方二维码扫码授权获取appAuthCode
	 * @param state
	 * @param socialLogin
	 * @return
	 */
	public static String loadQrcode(String state, SocialLoginEnum socialLogin){
		return getService(socialLogin).loadQrcode(state);
	}
	
	/**
	 * 加载第三方认证页授权获取appAuthCode
	 * @param state
	 * @param socialLogin
	 * @return
	 */
	public static String loadAuthPage(String state, SocialLoginEnum socialLogin){
		return getService(socialLogin).loadAuthPage(state);
	}
}
