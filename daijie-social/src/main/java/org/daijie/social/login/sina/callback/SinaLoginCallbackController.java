package org.daijie.social.login.sina.callback;

import org.daijie.social.login.sina.SinaLoginConstants;
import org.daijie.social.login.sina.SinaLoginTool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 新浪微博登录扫描回调
 * @author daijie_jay
 * @since 2017年11月28日
 */
@Controller
public class SinaLoginCallbackController {

	/**
	 * 处理回调业务
	 * @param code 临时code
	 * @param state 数据声明
	 * @param callback 回调函数
	 * @return String
	 */
	@GetMapping(value = SinaLoginConstants.LOGIN_CALLBACK)
	public String callback(@RequestParam String code, String state, SinaLoginCallback callback){
		return SinaLoginTool.login(code, callback);
	}
}
