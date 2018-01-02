package org.daijie.social.login.ali;

import org.daijie.social.login.LoginResult;
import org.daijie.social.login.ali.callback.AliLoginCallback;
import org.daijie.social.login.ali.model.AliUserInfo;
import org.daijie.social.login.ali.service.AliLoginService;
import org.daijie.social.login.ali.model.AliError;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 支付宝登录工具
 * @author daijie_jay
 * @since 2017年11月28日
 */
public class AliLoginTool {
	
	private static AliLoginService aliLoginService;

	@Autowired
	public void setAliLoginService(AliLoginService aliLoginService) {
		AliLoginTool.aliLoginService = aliLoginService;
	}

	/**
	 * 支付宝登录
	 * 实用于已得到支付宝临时code
	 * @param appAuthCode 临时code
	 * @param callback 回调函数
	 * @return String
	 */
	public static String login(String appAuthCode, AliLoginCallback callback){
		LoginResult result = aliLoginService.getUserInfo(appAuthCode);
		if(result.getResult()){
			AliUserInfo userInfo = (AliUserInfo) result;
			callback.handle(userInfo);
			return aliLoginService.getRedirectUrl();
		}
		callback.errer((AliError) result);
		return aliLoginService.getErrorUrl();
	}
	
	/**
	 * 访问支付宝二维码
	 * @param state 数据声明
	 * @return String
	 */
	public static String loadQrcode(String state){
		return aliLoginService.loadQrcode(state);
	}
	
	/**
	 * 访问支付宝认证页
	 * @param state 数据声明
	 * @return String
	 */
	public static String loadAuthPage(String state){
		return aliLoginService.loadAuthPage(state);
	}
}
