package org.daijie.social.login.qq.callback;

import org.daijie.social.login.qq.QQLoginConstants;
import org.daijie.social.login.qq.QQLoginTool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 百度登录扫描回调
 * @author daijie_jay
 * @date 2017年11月28日
 */
@Controller
public class QQLoginCallbackController {

	/**
	 * 处理回调业务
	 * @param code
	 * @param state
	 * @return
	 */
	@GetMapping(value = QQLoginConstants.LOGIN_CALLBACK)
	public String callback(@RequestParam String code, String state, QQLoginCallback callback){
		return QQLoginTool.login(code, callback);
	}
}
