package org.daijie.social.login.weixin.callback;

import org.daijie.social.login.weixin.WeixinLoginConstants;
import org.daijie.social.login.weixin.WeixinLoginTool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 微信登录扫描回调
 * @author daijie_jay
 * @date 2017年11月28日
 */
@Controller
public class WeixinLoginCallbackController {

	/**
	 * 处理回调业务
	 * @param code
	 * @param state
	 * @return
	 */
	@GetMapping(value = WeixinLoginConstants.LOGIN_CALLBACK)
	public String callback(@RequestParam String code, String state, WeixinLoginCallback callback){
		return WeixinLoginTool.login(code, callback);
	}
}
