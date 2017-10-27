package org.daijie.core.result;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author daijie
 * @date 2017年6月5日
 * api返回数据封装类
 * 
 */
public class ApiResult {
	public static final boolean SUCCESS = true;
	public static final boolean ERROR = false;

	//请求状态码
	protected String code;
	
	//请求是否成功
	protected boolean success;
	
	//返回消息提示
	protected String msg;
	
	//返回数据
	protected Map<String, java.lang.Object> data;

	public ApiResult() {
		data = new HashMap<String, java.lang.Object>();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, java.lang.Object> getData() {
		return data;
	}

	public void setData(Map<String, java.lang.Object> data) {
		this.data = data;
	}
}
