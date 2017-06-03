package org.daijie.core.controller.exception;

/**
 * api异常类
 * @author daijie
 * @date 2017年5月16日
 */
@SuppressWarnings("serial")
public class ApiException extends RuntimeException {

	//错误码
	private String code = "500";
	
	public ApiException(){
		super();
	}
	
	public ApiException(String msg){
		super(msg);
	}
	
	public ApiException(Throwable throwable){
		super(throwable);
	}
	
	public ApiException(String code, String msg){
		super(msg);
		this.code = code;
	}
	
	public ApiException(String code, Throwable throwable, String msg){
		super(msg, throwable);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
