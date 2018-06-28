package org.daijie.social.login.sina.service;

import org.daijie.core.util.http.HttpConversationUtil;
import org.daijie.social.login.AbstractLoginService;
import org.daijie.social.login.LoginResult;
import org.daijie.social.login.qq.QQLoginConstants;
import org.daijie.social.login.qq.QQLoignProperties;
import org.daijie.social.login.sina.SinaLoginConstants;
import org.daijie.social.login.sina.model.SinaAccessToken;
import org.daijie.social.login.sina.model.SinaError;
import org.daijie.social.login.sina.model.SinaUser;
import org.daijie.social.login.sina.model.SinaUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * 新浪微博登录服务
 * @author daijie_jay
 * @since 2017年11月28日
 */
@Service
public class SinaLoginService extends AbstractLoginService<QQLoignProperties> {
	
	private static final Logger logger = LoggerFactory.getLogger(SinaLoginService.class);
	
	public LoginResult getAccessToken(String code) {
		String callback = SinaLoginConstants.LOGIN_CALLBACK;
		if(!StringUtils.isEmpty(properties.getCallbackUri())){
			callback = properties.getCallbackUri();
		}
		if(!callback.contains("http")){
			String serverName = HttpConversationUtil.getRequest().getServerName();
			callback = serverName + callback;
		}
		StringBuilder uri = new StringBuilder();
		uri.append(QQLoginConstants.HOST_OPEN + QQLoginConstants.ACCESS_TOKEN + "?client_id=");
		uri.append(properties.getAppid());
		uri.append("&client_secret=").append(properties.getAppsecret());
		uri.append("&redirect_uri=" + callback);
		uri.append("&code=" + code).append("&grant_type=authorization_code");
		try {
			String result = restTemplate.getForObject(uri.toString(), String.class);
			JSONObject json = JSONUtil.parseObj(result);
			ObjectMapper mapper = new ObjectMapper();
			if(json.getStr("access_token") != null){
				SinaAccessToken accessToken = mapper.readValue(result, SinaAccessToken.class);
				return accessToken;
			}else{
				SinaError error = new SinaError();
				error.setErrcode(json.getStr("error_code"));
				error.setErrmsg(json.getStr("error") +":"+ json.getStr("error_description"));
				return error;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public LoginResult getOpenid(String access_token){
		StringBuilder uri = new StringBuilder();
		uri.append(QQLoginConstants.HOST_OPEN + QQLoginConstants.OPENID + "?");
		uri.append("access_token=" + access_token);
		try {
			String result = restTemplate.getForObject(uri.toString(), String.class);
			JSONObject json = JSONUtil.parseObj(result);
			ObjectMapper mapper = new ObjectMapper();
			if(json.getStr("uid") != null){
				SinaUser user = mapper.readValue(result, SinaUser.class);
				return user;
			}else{
				SinaError error = new SinaError();
				error.setErrcode(json.getStr("error_code"));
				error.setErrmsg(json.getStr("error") +":"+ json.getStr("error_description"));
				return error;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public LoginResult getUserInfo(String access_token, String openid) {
		StringBuilder uri = new StringBuilder();
		uri.append(SinaLoginConstants.HOST_OPEN + SinaLoginConstants.USERINFO + "?uid=");
		uri.append(openid);
		uri.append("&access_token=" + access_token);
		try {
			String result = restTemplate.getForObject(uri.toString(), String.class);
			JSONObject json = JSONUtil.parseObj(result);
			ObjectMapper mapper = new ObjectMapper();
			if(json.getStr("id") != null){
				SinaUserInfo userInfo = mapper.readValue(result, SinaUserInfo.class);
				return userInfo;
			}else{
				SinaError error = new SinaError();
				error.setErrcode(json.getStr("error_code"));
				error.setErrmsg(json.getStr("error") +":"+ json.getStr("error_description"));
				return error;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@Override
	public String loadAuthPage(String state) {
		String callback = SinaLoginConstants.LOGIN_CALLBACK;
		if(!StringUtils.isEmpty(properties.getCallbackUri())){
			callback = properties.getCallbackUri();
		}
		if(!callback.contains("http")){
			String serverName = HttpConversationUtil.getRequest().getServerName();
			callback = serverName + callback;
		}
		StringBuilder uri = new StringBuilder();
		uri.append(SinaLoginConstants.HOST_OPEN + SinaLoginConstants.AUTH + "?client_id=");
		uri.append(properties.getAppid());
		uri.append("&redirect_uri=" + callback);
		uri.append("&state=" + state);
		return REDIRECT + uri.toString();
	}

	@Override
	public String loadQrcode(String state) {
		return null;
	}

	@Override
	public LoginResult getUserInfo(String voucher) {
		LoginResult openidResult = getOpenid(voucher);
		if(openidResult.getResult()){
			SinaUser user = (SinaUser) openidResult;
			return getUserInfo(voucher, user.getUid());
		}else{
			return openidResult;
		}
	}
}
