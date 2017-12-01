package org.daijie.core.kisso.captcha;

import com.baomidou.kisso.common.captcha.service.Captcha;

/**
 * 获取图形验证码工具
 * @author daijie_jay
 * @date 2017年12月1日
 */
public class CaptchaTool {

	public static Captcha getCaptcha(){
		return new KissoCaptchaService().getCaptcha();
	}
}
