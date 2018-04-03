package org.daijie.core.result.factory;

import org.daijie.core.controller.enums.ResultCode;
import org.daijie.core.factory.Factory;
import org.daijie.core.result.ApiResult;
import org.daijie.core.result.ApiResultWrapper;

/**
 * 用于API返回数据封装类的工厂实例
 * @author daijie_jay
 * @since 2018年1月1日
 */
public abstract class ApiResultInitialFactory implements Factory {

	/**
	 * 添加返回属性值，默认key为value的类名，不建议用
	 * @param value 值
	 * @return ApiResultWrapper
	 */
	public static ApiResultWrapper addData(Object value){
		String name = value.getClass().getName();
		String key = name.substring(name.lastIndexOf(".")+1).substring(0, 1).toLowerCase()+name.substring(1);
		return Result.clear().addData(key, value);
	}
	
	/**
	 * 添加返回属性值
	 * @param key 键
	 * @param value 值
	 * @return ApiResultWrapper
	 */
	public static ApiResultWrapper addData(String key, Object value){
		return Result.clear().addData(key, value);
	}
	
	/**
	 * 构建默认返回数据
	 * @return ApiResult
	 */
	public static ApiResult build(){
		return Result.clear().build();
	}
	
	/**
	 * 构建默认数据的基础上设置返回状态
	 * @param success 返回状态
	 * @return ApiResult
	 */
	public static ApiResult build(boolean success){
		return Result.clear().setSuccess(success).build();
	}

	/**
	 * 构建默认数据的基础上设置返回状态、消息
	 * @param msg 消息
	 * @param success 返回状态
	 * @return ApiResult
	 */
	public static ApiResult build(String msg, boolean success){
		return Result.clear().setMsg(msg).setSuccess(success).build();
	}

	/**
	 * 构建默认数据的基础上设置返回状态、消息、结果码
	 * @param msg 消息
	 * @param success 返回状态
	 * @param code 状态码
	 * @return ApiResult
	 */
	public static ApiResult build(String msg, boolean success, ResultCode code){
		return Result.clear().setMsg(msg).setSuccess(success).setCode(code).build();
	}
	
	/**
	 * 静态类继承ApiResultInitialFactory，用于每次调用时初始默认的返回参数
	 * @author daijie
	 * @since 2017年5月15日
	 */
	private static class Result extends ApiResultInitialFactory{
		
		/**
		 * 默认初始化返回结果的辅助方法 
		 * @return ApiResultWrapper
		 */
		private static ApiResultWrapper clear(){
			return new ApiResultWrapper(ApiResult.SUCCESS, ResultCode.CODE_200);
		}
	}
}
