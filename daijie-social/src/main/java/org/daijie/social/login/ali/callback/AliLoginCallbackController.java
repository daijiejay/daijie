package org.daijie.social.login.ali.callback;

import org.daijie.social.login.ali.AliLoginConstants;
import org.daijie.social.login.ali.AliLoginTool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 微信登录扫描回调
 * @author daijie_jay
 * @since 2017年11月28日
 */
@Controller
public class AliLoginCallbackController {

	/**
	 * 处理回调业务
	 * @param code 临时code
	 * @param state 数据声明
	 * @param callback 回调函数
	 * @return String
	 */
	@GetMapping(value = AliLoginConstants.LOGIN_CALLBACK)
	public String callback(@RequestParam String code, String state, AliLoginCallback callback){
		return AliLoginTool.login(code, callback);
	}
}
