package org.daijie.social.login.weixin;

import org.daijie.social.login.LoginResult;
import org.daijie.social.login.weixin.callback.WeixinLoginCallback;
import org.daijie.social.login.weixin.model.WeixinAccessToken;
import org.daijie.social.login.weixin.model.WeixinError;
import org.daijie.social.login.weixin.model.WeixinUserInfo;
import org.daijie.social.login.weixin.service.WeixinLoginService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 微信登录工具
 * @author daijie_jay
 * @date 2017年11月28日
 */
public class WeixinLoginTool {
	
	private static WeixinLoginService wxLoginService;

	@Autowired
	public void setWeixinLoginService(WeixinLoginService wxLoginService) {
		WeixinLoginTool.wxLoginService = wxLoginService;
	}

	/**
	 * 微信登录
	 * 实用于已得到微信临时code
	 * @param code
	 * @return
	 */
	public static String login(String appAuthCode, WeixinLoginCallback callback){
		LoginResult result = wxLoginService.getAccessToken(appAuthCode);
		if(result.getResult()){
			WeixinAccessToken accessToken = (WeixinAccessToken) result;
			result = wxLoginService.getUserInfo(accessToken.getAccess_token());
			if(result.getResult()){
				WeixinUserInfo userInfo = (WeixinUserInfo) result;
				callback.handle(userInfo);
				return wxLoginService.getRedirectUrl();
			}
		}
		callback.errer((WeixinError) result);
		return wxLoginService.getErrorUrl();
	}
	
	/**
	 * 访问微信二维码
	 * @param state 
	 * @return
	 */
	public static String loadQrcode(String state){
		return wxLoginService.loadQrcode(state);
	}
	
	/**
	 * 访问微信认证页
	 * @param state 
	 * @return
	 */
	public static String loadAuthPage(String state){
		return wxLoginService.loadAuthPage(state);
	}
}
