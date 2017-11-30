package org.daijie.social.login.ali.callback;

import org.daijie.social.login.LoginCallback;
import org.daijie.social.login.ali.model.AliError;
import org.daijie.social.login.ali.model.AliUserInfo;

/**
 * 支付宝登录回调
 * @author daijie_jay
 * @date 2017年11月28日
 */
public interface AliLoginCallback extends LoginCallback<AliUserInfo, AliError> {
	
}
