package org.daijie.social.login.qq.service;

import org.apache.log4j.Logger;
import org.daijie.core.util.http.HttpConversationUtil;
import org.daijie.social.login.AbstractLoginService;
import org.daijie.social.login.LoginResult;
import org.daijie.social.login.qq.QQLoginConstants;
import org.daijie.social.login.qq.QQLoignProperties;
import org.daijie.social.login.qq.model.QQAccessToken;
import org.daijie.social.login.qq.model.QQError;
import org.daijie.social.login.qq.model.QQUser;
import org.daijie.social.login.qq.model.QQUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaoleilu.hutool.json.JSONObject;
import com.xiaoleilu.hutool.json.JSONUtil;

/**
 * QQ登录服务
 * @author daijie_jay
 * @date 2017年11月28日
 */
@Service
public class QQLoginService extends AbstractLoginService<QQLoignProperties> {
	
	private static final Logger logger = Logger.getLogger(QQLoginService.class);
	
	/**
	 * 根据票据code获取accessToken
	 * @param code 
	 * @return
	 */
	public LoginResult getAccessToken(String code) {
		StringBuilder uri = new StringBuilder();
		uri.append(QQLoginConstants.HOST_OPEN + QQLoginConstants.ACCESS_TOKEN + "?client_id=");
		uri.append(properties.getAppid());
		uri.append("&client_secret=").append(properties.getAppsecret());
		uri.append("&code=" + code).append("&grant_type=authorization_code");
		try {
			String result = restTemplate.getForObject(uri.toString(), String.class);
			JSONObject json = JSONUtil.parseObj(result);
			ObjectMapper mapper = new ObjectMapper();
			if(json.getStr("access_token") != null){
				QQAccessToken accessToken = mapper.readValue(result, QQAccessToken.class);
				return accessToken;
			}else{
				QQError error = new QQError();
				error.setErrcode(json.getStr("code"));
				error.setErrmsg(json.getStr("msg"));
				return error;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 获取QQ认证token
	 * @param access_token
	 * @return
	 */
	public LoginResult getOpenid(String access_token){
		StringBuilder uri = new StringBuilder();
		uri.append(QQLoginConstants.HOST_OPEN + QQLoginConstants.OPENID + "?");
		uri.append("access_token=" + access_token);
		try {
			String result = restTemplate.getForObject(uri.toString(), String.class);
			JSONObject json = JSONUtil.parseObj(result);
			if(json.getStr("openid") != null){
				QQUser user = new QQUser();
				user.setAppid(json.getStr("client_id"));
				user.setOpenid(json.getStr("openid"));
				return user;
			}else{
				QQError error = new QQError();
				error.setErrcode(json.getStr("ret"));
				error.setErrmsg(json.getStr("msg"));
				return error;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 获取QQ个人用户信息
	 * @param appid
	 * @param openid
	 * @return
	 */
	public LoginResult getUserInfo(String access_token, String appid, String openid) {
		StringBuilder uri = new StringBuilder();
		uri.append(QQLoginConstants.HOST_OPEN + QQLoginConstants.USERINFO + "?oauth_consumer_key=");
		uri.append(appid);
		uri.append("&openid=" + openid);
		uri.append("&access_token=" + access_token);
		try {
			String result = restTemplate.getForObject(uri.toString(), String.class);
			JSONObject json = JSONUtil.parseObj(result);
			ObjectMapper mapper = new ObjectMapper();
			if(json.getInt("ret") == 0){
				QQUserInfo userInfo = mapper.readValue(result, QQUserInfo.class);
				return userInfo;
			}else{
				QQError error = new QQError();
				error.setErrcode(json.getStr("ret"));
				error.setErrmsg(json.getStr("msg"));
				return error;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 访问QQ认证页
	 * @return
	 */
	@Override
	public String loadAuthPage(String state) {
		String callback = QQLoginConstants.LOGIN_CALLBACK;
		if(!StringUtils.isEmpty(properties.getCallbackUri())){
			callback = properties.getCallbackUri();
		}
		if(!callback.contains("http")){
			String serverName = HttpConversationUtil.getRequest().getServerName();
			callback = serverName + callback;
		}
		StringBuilder uri = new StringBuilder();
		uri.append(QQLoginConstants.HOST_OPEN + QQLoginConstants.AUTH + "?client_id=");
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

	@Override
	public LoginResult getUserInfo(String voucher) {
		LoginResult openidResult = getOpenid(voucher);
		if(openidResult.getResult()){
			QQUser user = (QQUser) openidResult;
			return getUserInfo(voucher, user.getAppid(), user.getOpenid());
		}else{
			return openidResult;
		}
	}
}
