package org.daijie.social.login.baidu;

import org.daijie.social.login.LoginResult;
import org.daijie.social.login.baidu.callback.BaiduLoginCallback;
import org.daijie.social.login.baidu.model.BaiduUserInfo;
import org.daijie.social.login.baidu.service.BaiduLoginService;
import org.daijie.social.login.baidu.model.BaiduAccessToken;
import org.daijie.social.login.baidu.model.BaiduError;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 百度登录工具
 * @author daijie_jay
 * @date 2017年11月28日
 */
public class BaiduLoginTool {
	
	private static BaiduLoginService baiduLoginService;

	@Autowired
	public void setBaiduLoginService(BaiduLoginService baiduLoginService) {
		BaiduLoginTool.baiduLoginService = baiduLoginService;
	}

	/**
	 * 百度登录
	 * 实用于已得到百度临时code
	 * @param code
	 * @return
	 */
	public static String login(String appAuthCode, BaiduLoginCallback callback){
		LoginResult result = baiduLoginService.getAccessToken(appAuthCode);
		if(result.getResult()){
			BaiduAccessToken accessToken = (BaiduAccessToken) result;
			result = baiduLoginService.getUserInfo(accessToken.getAccess_token());
			if(result.getResult()){
				BaiduUserInfo userInfo = (BaiduUserInfo) result;
				callback.handle(userInfo);
				return baiduLoginService.getRedirectUrl();
			}
		}
		callback.errer((BaiduError) result);
		return baiduLoginService.getErrorUrl();
	}
	
	/**
	 * 访问百度二维码
	 * @return
	 */
	public static String loadQrcode(String state){
		return baiduLoginService.loadQrcode(state);
	}
	
	/**
	 * 访问百度认证页
	 * @param state 
	 * @return
	 */
	public static String loadAuthPage(String state){
		return baiduLoginService.loadAuthPage(state);
	}
}
