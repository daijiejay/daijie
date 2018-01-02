package org.daijie.social.login.qq.callback;

import org.daijie.social.login.qq.QQLoginConstants;
import org.daijie.social.login.qq.QQLoginTool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 百度登录扫描回调
 * @author daijie_jay
 * @since 2017年11月28日
 */
@Controller
public class QQLoginCallbackController {

	/**
	 * 处理回调业务
	 * @param code 临时code
	 * @param state 数据声明
	 * @param callback 回调函数
	 * @return String
	 */
	@GetMapping(value = QQLoginConstants.LOGIN_CALLBACK)
	public String callback(@RequestParam String code, String state, QQLoginCallback callback){
		return QQLoginTool.login(code, callback);
	}
}
