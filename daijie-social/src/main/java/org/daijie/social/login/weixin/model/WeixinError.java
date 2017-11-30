package org.daijie.social.login.weixin.model;

import org.daijie.social.login.LoginResult;

/**
 * 错误信息
 * @author daijie_jay
 * @date 2017年11月28日
 */
public class WeixinError implements LoginResult {

	private String errcode;
	
	private String errmsg;

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
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
