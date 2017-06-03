package org.daijie.core.factory.specific;

import org.daijie.core.controller.enums.ResultCode;
import org.daijie.core.factory.InitialFactory;
import org.daijie.core.httpResult.ApiResult;
import org.daijie.core.httpResult.ApiResultWrapper;

/**
 * 用于API返回数据封装类的工厂实例
 * @author daijie
 * @date 2017年5月15日
 */
public abstract class ApiResultInitialFactory implements InitialFactory {

	@Override
	public Object init() {
		return new ApiResultWrapper(ApiResult.SUCCESS,
				ResultCode.CODE_200.getValue(),
				ResultCode.CODE_200.getDescription());
	}

	public static ApiResultWrapper addData(String key, Object value){
		if(value == null)
			return Result.clear().addData(key, "");
		else
			return Result.clear().addData(key, value);
	}
	
	public static ApiResult build(){
		return Result.clear().build();
	}
	
	public static ApiResult build(boolean success){
		return Result.clear().setSuccess(success).build();
	}
	
	public static ApiResult build(String msg, boolean success){
		return Result.clear().setMsg(msg).setSuccess(success).build();
	}
	
	public static ApiResult build(String msg, boolean success, String code){
		return Result.clear().setMsg(msg).setSuccess(success).setCode(code).build();
	}
	
	/**
	 * 初始默认的返回参数
	 * @author daijie
	 * @date 2017年5月15日
	 */
	public static class Result extends ApiResultInitialFactory{
		
		private static Result result;
		
		static{
			if(result == null){
				result = new Result();
			}
		}
		
		private static ApiResultWrapper initApiResultWrapper(){
			return (ApiResultWrapper) result.init();
		}
		
		private static ApiResultWrapper clear(){
			return initApiResultWrapper();
		}
	}
}
