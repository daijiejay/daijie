package org.daijie.social.login.ali.model;

import org.daijie.social.login.LoginResult;

import com.alipay.api.response.AlipayOpenAuthTokenAppResponse;

@SuppressWarnings("serial")
public class AliUserInfo extends AlipayOpenAuthTokenAppResponse implements LoginResult {

	@Override
	public Boolean getResult() {
		return true;
	}

}
