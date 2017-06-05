package org.daijie.core.factory.specific;

import org.daijie.core.controller.enums.ResultCode;
import org.daijie.core.factory.InitialFactory;
import org.daijie.core.httpResult.ApiResult;
import org.daijie.core.httpResult.ApiResultWrapper;

/**
 * 
 * @author daijie
 * @date 2017年5月15日
 * 用于API返回数据封装类的工厂实例
 * 
 */
public abstract class ApiResultInitialFactory implements InitialFactory {

	/**
	 * 默认初始化返回结果
	 */
	@Override
	public Object init() {
		return new ApiResultWrapper(ApiResult.SUCCESS,
				ResultCode.CODE_200.getValue(),
				ResultCode.CODE_200.getDescription());
	}

	/**
	 * 添加返回属性，如果为value为null自动转换为空字符串
	 * @param key
	 * @param value
	 * @return
	 */
	public static ApiResultWrapper addData(String key, Object value){
		if(value == null)
			return Result.clear().addData(key, "");
		else
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
	public static ApiResult build(String msg, boolean success, String code){
		return Result.clear().setMsg(msg).setSuccess(success).setCode(code).build();
	}
	
	/**
	 * 静态类继承ApiResultInitialFactory，用于每次调用时初始默认的返回参数
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
		
		/**
		 * 默认初始化父类返回结果
		 * @return
		 */
		private static ApiResultWrapper initApiResultWrapper(){
			return (ApiResultWrapper) result.init();
		}
		
		/**
		 * 默认初始化返回结果的辅助方法 
		 * @return
		 */
		private static ApiResultWrapper clear(){
			return initApiResultWrapper();
		}
	}
}
