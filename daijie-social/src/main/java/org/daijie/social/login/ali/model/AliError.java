package org.daijie.social.login.ali.model;

import org.daijie.social.login.LoginResult;

import com.alipay.api.response.AlipayOpenAuthTokenAppResponse;

/**
 * 错误信息
 * @author daijie_jay
 * @since 2017年11月28日
 */
@SuppressWarnings("serial")
public class AliError extends AlipayOpenAuthTokenAppResponse implements LoginResult {

	private String errcode;
	
	private String errmsg;

	public String getErrcode() {
		this.errcode = this.getCode();
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		this.errmsg = this.getMsg();
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	@Override
	public Boolean getResult() {
		return false;
	}
}
