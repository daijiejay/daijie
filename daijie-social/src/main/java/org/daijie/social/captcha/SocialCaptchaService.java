package org.daijie.social.captcha;

/**
 * 第三方验证服务
 * @author daijie_jay
 * @date 2017年12月4日
 */
public interface SocialCaptchaService {

	/**
	 * 获取验证码相关凭证或url
	 * @return
	 */
	public String getCaptcha();
	
	/**
	 * 验证验证码
	 * @param verifyCode
	 * @return
	 */
	public Boolean verifyCaptcha(String verifyCode);
}
