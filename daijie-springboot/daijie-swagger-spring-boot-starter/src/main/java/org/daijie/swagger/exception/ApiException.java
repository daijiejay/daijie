package org.daijie.swagger.exception;

import org.daijie.swagger.result.ResultCode;

/**
 * api异常类
 * @author daijie
 * @since 2017年5月16日
 */
@SuppressWarnings("serial")
public class ApiException extends RuntimeException {

	//错误码
	private ResultCode code = ResultCode.CODE_500;
	
	public ApiException(){
		super();
	}
	
	public ApiException(String msg){
		super(msg);
	}
	
	public ApiException(Throwable throwable){
		super(throwable);
	}
	
	public ApiException(ResultCode code, String msg){
		super(msg);
		this.code = code;
	}
	
	public ApiException(ResultCode code, Throwable throwable, String msg){
		super(msg, throwable);
		this.code = code;
	}

	public ResultCode getCode() {
		return code;
	}

	public void setCode(ResultCode code) {
		this.code = code;
	}
}
