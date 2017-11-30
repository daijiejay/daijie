package org.daijie.social.login.ali.service;

import org.apache.log4j.Logger;
import org.daijie.core.util.http.HttpConversationUtil;
import org.daijie.social.login.AbstractLoginService;
import org.daijie.social.login.LoginResult;
import org.daijie.social.login.ali.AliLoginConstants;
import org.daijie.social.login.ali.AliLoignProperties;
import org.daijie.social.login.ali.model.AliError;
import org.daijie.social.login.ali.model.AliUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayOpenAuthTokenAppRequest;
import com.alipay.api.response.AlipayOpenAuthTokenAppResponse;
import com.xiaoleilu.hutool.util.BeanUtil;

/**
 * 支付宝登录服务
 * @author daijie_jay
 * @date 2017年11月28日
 */
@Service
public class AliLoginService extends AbstractLoginService<AliLoignProperties> {
	
	private static final Logger logger = Logger.getLogger(AliLoginService.class);
	
	/**
	 * 获取支付宝用户个人信息
	 * @param access_token 
	 * @return
	 */
	@Override
	public LoginResult getUserInfo(String appAuthCode) {
		try {
			AlipayClient alipayClient = new DefaultAlipayClient(
					AliLoginConstants.HOST_OPEN + AliLoginConstants.GATEWAY, 
					properties.getAppid(), 
					properties.getAppsecret(), 
					"json",
					"UTF-8", 
					properties.getPublicKey(), 
					"RSA2");
			AlipayOpenAuthTokenAppRequest request = new AlipayOpenAuthTokenAppRequest();
			request.setBizContent(
					"{" + "\"grant_type\":\"authorization_code\"," + "\"code\":\"" + appAuthCode + "\"" + "}");
			AlipayOpenAuthTokenAppResponse response = alipayClient.execute(request);
			if(response.isSuccess()){
				AliUserInfo userInfo = new AliUserInfo();
				BeanUtil.copyProperties(response, userInfo);
				return userInfo;
			}else{
				AliError error = new AliError();
				BeanUtil.copyProperties(response, error);
				return error;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public String loadQrcode(String state) {
		String callback = AliLoginConstants.LOGIN_CALLBACK;
		if(!StringUtils.isEmpty(properties.getCallbackUri())){
			callback = properties.getCallbackUri();
		}
		if(!callback.contains("http")){
			String serverName = HttpConversationUtil.getRequest().getServerName();
			callback = serverName + callback;
		}
		StringBuilder uri = new StringBuilder();
		uri.append(AliLoginConstants.HOST_OPEN + AliLoginConstants.AUTH + "?appid=");
		uri.append(properties.getAppid());
		uri.append("&redirect_uri=" + callback);
		return REDIRECT + uri.toString();
	}

	@Override
	public String loadAuthPage(String state) {
		return null;
	}
}
