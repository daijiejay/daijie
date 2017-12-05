package org.daijie.social.cloud.controller;

import org.daijie.core.factory.specific.ModelResultInitialFactory.Result;
import org.daijie.core.result.ModelResult;
import org.daijie.social.captcha.SocialCaptchaTool;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 第三方验证码测试
 * @author daijie_jay
 * @date 2017年11月28日
 */
@RestController
public class SocialCaptchaController {

	/**
	 * 获取腾讯验证码
	 * @return
	 */
	@RequestMapping(value = "tx/captcha", method = RequestMethod.GET)
	public ModelResult<String> getTXCaptcha(){
		return Result.build(SocialCaptchaTool.getTXCaptcha());
	}
	
	/**
	 * 验证腾讯验证码
	 * @param verifyCode
	 * @return
	 */
	@RequestMapping(value = "tx/captcha/verify", method = RequestMethod.POST)
	public ModelResult<Boolean> verifyTXCaptcha(@RequestParam String verifyCode){
		return Result.build(SocialCaptchaTool.verifyTXCaptcha(verifyCode));
	}
}
