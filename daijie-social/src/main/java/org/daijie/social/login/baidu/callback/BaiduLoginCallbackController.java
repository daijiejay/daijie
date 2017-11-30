package org.daijie.social.login.baidu.callback;

import org.daijie.social.login.ali.AliLoginConstants;
import org.daijie.social.login.baidu.BaiduLoginTool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 百度登录扫描回调
 * @author daijie_jay
 * @date 2017年11月28日
 */
@Controller
public class BaiduLoginCallbackController {

	/**
	 * 处理回调业务
	 * @param code
	 * @param state
	 * @return
	 */
	@GetMapping(value = AliLoginConstants.LOGIN_CALLBACK)
	public String callback(@RequestParam String code, String state, BaiduLoginCallback callback){
		return BaiduLoginTool.login(code, callback);
	}
}
