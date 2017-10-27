package org.daijie.core.factory.specific;

import org.daijie.core.controller.enums.ResultCode;
import org.daijie.core.factory.InitialFactory;
import org.daijie.core.httpResult.ApiResult;
import org.daijie.core.httpResult.ModelResult;
import org.daijie.core.httpResult.ModelResultWrapper;

/**
 * 实例工厂，实例具体返回对象
 * @author daijie
 * @date 2017年10月27日
 */
public abstract class ModelResultInitialFactory implements InitialFactory {
	
	/**
	 * 构建默认返回数据
	 * @param <E>
	 * @return
	 */
	public static <E> ModelResult<E> build(){
		return Result.build(null);
	}
	
	/**
	 * 构建默认返回数据
	 * @param <E>
	 * @return
	 */
	public static <E> ModelResult<E> build(E value){
		return Result.clear(value).build();
	}
	
	/**
	 * 构建默认数据的基础上设置返回状态
	 * @param success
	 * @return
	 */
	public static <E> ModelResult<E> build(E value, boolean success){
		return Result.clear(value).setSuccess(success).build();
	}

	/**
	 * 构建默认数据的基础上设置返回状态、消息
	 * @param success
	 * @return
	 */
	public static <E> ModelResult<E> build(E value, String msg, boolean success){
		return Result.clear(value).setMsg(msg).setSuccess(success).build();
	}

	/**
	 * 构建默认数据的基础上设置返回状态、消息、结果码
	 * @param <E>
	 * @param success
	 * @return
	 */
	public static <E> ModelResult<E> build(E value, String msg, boolean success, ResultCode code){
		return Result.clear(value).setMsg(msg).setSuccess(success).setCode(code).build();
	}
	
	/**
	 * 构建默认数据的基础上设置返回状态
	 * @param success
	 * @return
	 */
	public static <E> ModelResult<E> build(boolean success){
		return build(null, success);
	}
	
	/**
	 * 构建默认数据的基础上设置返回状态、消息
	 * @param success
	 * @return
	 */
	public static <E> ModelResult<E> build(String msg, boolean success){
		return build(null, msg, success);
	}
	
	/**
	 * 构建默认数据的基础上设置返回状态、消息、结果码
	 * @param <E>
	 * @param success
	 * @return
	 */
	public static <E> ModelResult<E> build(String msg, boolean success, ResultCode code){
		return build(null, msg, success, code);
	}
	
	/**
	 * 静态类继承ApiResultInitialFactory，用于每次调用时初始默认的返回参数
	 * @author daijie
	 * @date 2017年5月15日
	 */
	public static class Result extends ModelResultInitialFactory{
		
		/**
		 * 默认初始化返回结果的辅助方法 
		 * @param <E>
		 * @return
		 */
		private static <E> ModelResultWrapper<E> clear(E value){
			return new ModelResultWrapper<E>(ApiResult.SUCCESS, ResultCode.CODE_200).setData(value);
		}
	}
}
