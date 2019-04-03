package org.daijie.core.kisso.captcha;

import com.baomidou.kisso.common.captcha.service.Captcha;

/**
 * 图形验证码工具
 * @author daijie_jay
 * @since 2017年12月1日
 */
public class CaptchaTool {

	/**
	 * 获取图形验证码
	 * @return Captcha
	 */
	public static Captcha getCaptcha(){
		return new KissoCaptchaService().getCaptcha();
	}
}
