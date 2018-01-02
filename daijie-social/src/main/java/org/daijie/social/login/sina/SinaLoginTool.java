package org.daijie.social.login.sina;

import org.daijie.social.login.LoginResult;
import org.daijie.social.login.sina.callback.SinaLoginCallback;
import org.daijie.social.login.sina.model.SinaAccessToken;
import org.daijie.social.login.sina.model.SinaError;
import org.daijie.social.login.sina.model.SinaUserInfo;
import org.daijie.social.login.sina.service.SinaLoginService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 新浪微博登录工具
 * @author daijie_jay
 * @since 2017年11月28日
 */
public class SinaLoginTool {
	
	private static SinaLoginService sinaLoginService;

	@Autowired
	public void setSinaLoginService(SinaLoginService sinaLoginService) {
		SinaLoginTool.sinaLoginService = sinaLoginService;
	}

	/**
	 * 新浪微博登录
	 * 实用于已得到新浪微博临时code
	 * @param appAuthCode 临时code
	 * @param callback 回调函数
	 * @return String
	 */
	public static String login(String appAuthCode, SinaLoginCallback callback){
		LoginResult result = sinaLoginService.getAccessToken(appAuthCode);
		if(result.getResult()){
			SinaAccessToken accessToken = (SinaAccessToken) result;
			result = sinaLoginService.getUserInfo(accessToken.getAccess_token());
			if(result.getResult()){
				SinaUserInfo userInfo = (SinaUserInfo) result;
				callback.handle(userInfo);
				return sinaLoginService.getRedirectUrl();
			}
		}
		callback.errer((SinaError) result);
		return sinaLoginService.getErrorUrl();
	}
	
	/**
	 * 访问新浪微博二维码
	 * @param state 数据声明
	 * @return String
	 */
	public static String loadQrcode(String state){
		return sinaLoginService.loadQrcode();
	}
	
	/**
	 * 访问新浪微博认证页
	 * @param state 数据声明
	 * @return String
	 */
	public static String loadAuthPage(String state){
		return sinaLoginService.loadAuthPage(state);
	}
}
