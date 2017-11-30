package org.daijie.social.login.qq;

import org.daijie.social.login.LoginResult;
import org.daijie.social.login.qq.callback.QQLoginCallback;
import org.daijie.social.login.qq.model.QQAccessToken;
import org.daijie.social.login.qq.model.QQError;
import org.daijie.social.login.qq.model.QQUserInfo;
import org.daijie.social.login.qq.service.QQLoginService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * QQ登录工具
 * @author daijie_jay
 * @date 2017年11月28日
 */
public class QQLoginTool {
	
	private static QQLoginService qqLoginService;

	@Autowired
	public void setQQLoginService(QQLoginService qqLoginService) {
		QQLoginTool.qqLoginService = qqLoginService;
	}

	/**
	 * QQ登录
	 * 实用于已得到QQ临时code
	 * @param code
	 * @return
	 */
	public static String login(String appAuthCode, QQLoginCallback callback){
		LoginResult result = qqLoginService.getAccessToken(appAuthCode);
		if(result.getResult()){
			QQAccessToken accessToken = (QQAccessToken) result;
			result = qqLoginService.getUserInfo(accessToken.getAccess_token());
			if(result.getResult()){
				QQUserInfo userInfo = (QQUserInfo) result;
				callback.handle(userInfo);
				return qqLoginService.getRedirectUrl();
			}
		}
		callback.errer((QQError) result);
		return qqLoginService.getErrorUrl();
	}
	
	/**
	 * 访问QQ二维码
	 * @return
	 */
	public static String loadQrcode(String state){
		return qqLoginService.loadQrcode(state);
	}
	
	/**
	 * 访问QQ认证页
	 * @param state 
	 * @return
	 */
	public static String loadAuthPage(String state){
		return qqLoginService.loadAuthPage(state);
	}
}
