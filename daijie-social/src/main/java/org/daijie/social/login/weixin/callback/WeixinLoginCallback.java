package org.daijie.social.login.weixin.callback;

import org.daijie.social.login.LoginCallback;
import org.daijie.social.login.weixin.model.WeixinError;
import org.daijie.social.login.weixin.model.WeixinUserInfo;

/**
 * 微信登录回调
 * @author daijie_jay
 * @date 2017年11月28日
 */
public interface WeixinLoginCallback extends LoginCallback<WeixinUserInfo, WeixinError> {

}
