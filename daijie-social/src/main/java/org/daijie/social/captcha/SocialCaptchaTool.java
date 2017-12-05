package org.daijie.social.captcha;

import org.daijie.social.captcha.tx.TXCaptchaService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 第三方验证码工具
 * @author daijie_jay
 * @date 2017年12月4日
 */
public class SocialCaptchaTool {
	
	private static TXCaptchaService txCaptchaService;
	
	@Autowired
	public void setTXCaptchaService(TXCaptchaService txCaptchaService){
		SocialCaptchaTool.txCaptchaService = txCaptchaService;
	}

	public static String getTXCaptcha(){
		return txCaptchaService.getCaptcha();
	}
	
	public static Boolean verifyTXCaptcha(String verifyCode){
		return txCaptchaService.verifyCaptcha(verifyCode);
	}
}
