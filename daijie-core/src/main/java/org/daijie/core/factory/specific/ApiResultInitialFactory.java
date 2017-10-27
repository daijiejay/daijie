package org.daijie.core.factory.specific;

import org.daijie.core.controller.enums.ResultCode;
import org.daijie.core.factory.InitialFactory;
import org.daijie.core.result.ApiResult;
import org.daijie.core.result.ApiResultWrapper;

/**
 * 
 * @author daijie
 * @date 2017年5月15日
 * 用于API返回数据封装类的工厂实例
 * 
 */
public abstract class ApiResultInitialFactory implements InitialFactory {

	/**
	 * 添加返回属性，默认key为data
	 * @param value
	 * @return
	 */
	public static ApiResultWrapper addData(Object value){
		String name = value.getClass().getName();
		String key = name.substring(name.lastIndexOf(".")+1).substring(0, 1).toLowerCase()+name.substring(1);
		return Result.clear().addData(key, value);
	}
	
	/**
	 * 添加返回属性
	 * @param key
	 * @param value
	 * @return
	 */
	public static ApiResultWrapper addData(String key, Object value){
		return Result.clear().addData(key, value);
	}
	
	/**
	 * 构建默认返回数据
	 * @return
	 */
	public static ApiResult build(){
		return Result.clear().build();
	}
	
	/**
	 * 构建默认数据的基础上设置返回状态
	 * @param success
	 * @return
	 */
	public static ApiResult build(boolean success){
		return Result.clear().setSuccess(success).build();
	}

	/**
	 * 构建默认数据的基础上设置返回状态、消息
	 * @param success
	 * @return
	 */
	public static ApiResult build(String msg, boolean success){
		return Result.clear().setMsg(msg).setSuccess(success).build();
	}

	/**
	 * 构建默认数据的基础上设置返回状态、消息、结果码
	 * @param success
	 * @return
	 */
	public static ApiResult build(String msg, boolean success, ResultCode code){
		return Result.clear().setMsg(msg).setSuccess(success).setCode(code).build();
	}
	
	/**
	 * 静态类继承ApiResultInitialFactory，用于每次调用时初始默认的返回参数
	 * @author daijie
	 * @date 2017年5月15日
	 */
	private static class Result extends ApiResultInitialFactory{
		
		/**
		 * 默认初始化返回结果的辅助方法 
		 * @return
		 */
		private static ApiResultWrapper clear(){
			return new ApiResultWrapper(ApiResult.SUCCESS, ResultCode.CODE_200);
		}
	}
}
