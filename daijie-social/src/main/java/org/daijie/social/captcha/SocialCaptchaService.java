package org.daijie.social.captcha;

/**
 * 第三方验证服务
 * @author daijie_jay
 * @since 2017年12月4日
 */
public interface SocialCaptchaService {

	/**
	 * 获取验证码相关凭证或url
	 * @return String
	 */
	public String getCaptcha();
	
	/**
	 * 验证验证码
	 * @param verifyCode 验证码
	 * @return Boolean
	 */
	public Boolean verifyCaptcha(String verifyCode);
}
