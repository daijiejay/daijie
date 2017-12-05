package org.daijie.social.captcha.tx;

import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.daijie.core.util.http.HttpConversationUtil;
import org.daijie.social.captcha.SocialCaptchaService;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 腾讯验证码服务
 * @author daijie_jay
 * @date 2017年12月4日
 */
public class TXCaptchaService implements SocialCaptchaService {

	private static final Logger logger = Logger.getLogger(TXCaptchaService.class);

	@Autowired
	private TXCaptchaProperties txCaptchaProperties;

	@Override
	public String getCaptcha(){
		String jsUrl = "";
		CaptchaAPI api = CaptchaAPI.getInstance(txCaptchaProperties.getUrl(), txCaptchaProperties.getSecretId(), txCaptchaProperties.getSecretKey());
		try {
			SortedMap<String, String> args = new TreeMap<String, String>();
			args.put("captchaType", txCaptchaProperties.getCaptchaType());
			args.put("disturbLevel", txCaptchaProperties.getDisturbLevel());
			args.put("isHttps", txCaptchaProperties.getIsHttps());
			args.put("clientType", txCaptchaProperties.getClientType());
			args.put("businessId", txCaptchaProperties.getBusinessId());
			ApiResponse resp = api.getJsUrl(args);
			String content = (String)resp.getBody();
			Gson gson = new Gson();
			HashMap<String, String> map = gson.fromJson(content, new TypeToken<HashMap<String, String>>(){}.getType());
			if (Integer.parseInt(map.get("code")) == 0) {
				jsUrl = map.get("url");
			} else {
				logger.debug(map.toString());
			}
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
		}
		return jsUrl;
	}

	@Override
	public Boolean verifyCaptcha(String verifyCode) {
		if(StringUtils.isNotEmpty(verifyCode)){
			SortedMap<String, String> args = new TreeMap<String, String>();
			args.put("captchaType", txCaptchaProperties.getCaptchaType());
			args.put("ticket", verifyCode);
			args.put("businessId", txCaptchaProperties.getBusinessId());
			args.put("userIp", HttpConversationUtil.getRequest().getRemoteAddr());
			CaptchaAPI api = CaptchaAPI.getInstance(txCaptchaProperties.getUrl(), txCaptchaProperties.getSecretId(), txCaptchaProperties.getSecretKey());
			try {
				ApiResponse resp = api.check(args);
				String content = (String)resp.getBody();
				Gson gson = new Gson();
				HashMap<String, String> map = gson.fromJson(content, new TypeToken<HashMap<String, String>>(){}.getType());
				if (Integer.parseInt(map.get("code")) == 0) {
					return true;
				} else {
					logger.debug(map.toString());
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}
		return null;
	}
}
