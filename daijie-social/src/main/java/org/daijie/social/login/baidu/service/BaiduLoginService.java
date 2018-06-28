package org.daijie.social.login.baidu.service;

import org.daijie.core.util.http.HttpConversationUtil;
import org.daijie.social.login.AbstractLoginService;
import org.daijie.social.login.LoginResult;
import org.daijie.social.login.baidu.BaiduLoginConstants;
import org.daijie.social.login.baidu.BaiduLoignProperties;
import org.daijie.social.login.baidu.model.BaiduAccessToken;
import org.daijie.social.login.baidu.model.BaiduError;
import org.daijie.social.login.baidu.model.BaiduUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * 百度登录服务
 * @author daijie_jay
 * @since 2017年11月28日
 */
@Service
public class BaiduLoginService extends AbstractLoginService<BaiduLoignProperties> {
	
	private static final Logger logger = LoggerFactory.getLogger(BaiduLoginService.class);
	
	/**
	 * 根据票据code获取accessToken
	 * @param code 临时code
	 * @return LoginResult
	 */
	public LoginResult getAccessToken(String code) {
		StringBuilder uri = new StringBuilder();
		uri.append(BaiduLoginConstants.HOST_OPEN + BaiduLoginConstants.ACCESS_TOKEN + "?client_id=");
		uri.append(properties.getAppid());
		uri.append("&client_secret=").append(properties.getAppsecret());
		uri.append("&code=" + code).append("&grant_type=authorization_code");
		try {
			String result = restTemplate.getForObject(uri.toString(), String.class);
			JSONObject json = JSONUtil.parseObj(result);
			ObjectMapper mapper = new ObjectMapper();
			if(json.getStr("access_token") != null){
				BaiduAccessToken accessToken = mapper.readValue(result, BaiduAccessToken.class);
				return accessToken;
			}else{
				BaiduError error = new BaiduError();
				error.setErrcode(json.getStr("error"));
				error.setErrmsg(json.getStr("error_description"));
				return error;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@Override
	public LoginResult getUserInfo(String access_token) {
		StringBuilder uri = new StringBuilder();
		uri.append(BaiduLoginConstants.HOST_OPEN + BaiduLoginConstants.USERINFO + "?");
		uri.append("access_token=" + access_token);
		try {
			String result = restTemplate.getForObject(uri.toString(), String.class);
			JSONObject json = JSONUtil.parseObj(result);
			ObjectMapper mapper = new ObjectMapper();
			if(json.getStr("userid") != null){
				BaiduUserInfo userInfo = mapper.readValue(result, BaiduUserInfo.class);
				return userInfo;
			}else{
				BaiduError error = new BaiduError();
				error.setErrcode(json.getStr("error"));
				error.setErrmsg(json.getStr("error_description"));
				return error;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@Override
	public String loadAuthPage(String state) {
		String callback = BaiduLoginConstants.LOGIN_CALLBACK;
		if(!StringUtils.isEmpty(properties.getCallbackUri())){
			callback = properties.getCallbackUri();
		}
		if(!callback.contains("http")){
			String serverName = HttpConversationUtil.getRequest().getServerName();
			callback = serverName + callback;
		}
		StringBuilder uri = new StringBuilder();
		uri.append(BaiduLoginConstants.HOST_OPEN + BaiduLoginConstants.AUTH + "?client_id=");
		uri.append(properties.getAppid());
		uri.append("&redirect_uri=" + callback);
		uri.append("&response_type=code");
		uri.append("&state=" + state);
		return REDIRECT + uri.toString();
	}

	@Override
	public String loadQrcode(String state) {
		return null;
	}
}
