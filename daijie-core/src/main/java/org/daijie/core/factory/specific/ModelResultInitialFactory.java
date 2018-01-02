package org.daijie.core.factory.specific;

import org.daijie.core.controller.enums.ResultCode;
import org.daijie.core.factory.InitialFactory;
import org.daijie.core.result.ApiResult;
import org.daijie.core.result.ModelResult;
import org.daijie.core.result.ModelResultWrapper;

/**
 * 实例工厂，实例具体返回对象
 * @author daijie
 * @since 2017年10月27日
 */
public abstract class ModelResultInitialFactory implements InitialFactory {
	
	public static <E> ModelResult<E> build(){
		return Result.build(null);
	}
	
	public static <E> ModelResult<E> build(E value){
		return Result.clear(value).build();
	}
	
	public static <E> ModelResult<E> build(E value, boolean success){
		return Result.clear(value).setSuccess(success).build();
	}

	public static <E> ModelResult<E> build(E value, String msg, boolean success){
		return Result.clear(value).setMsg(msg).setSuccess(success).build();
	}

	public static <E> ModelResult<E> build(E value, String msg, boolean success, ResultCode code){
		return Result.clear(value).setMsg(msg).setSuccess(success).setCode(code).build();
	}
	
	public static <E> ModelResult<E> build(boolean success){
		return build(null, success);
	}
	
	public static <E> ModelResult<E> build(String msg, boolean success){
		return build(null, msg, success);
	}
	
	public static <E> ModelResult<E> build(String msg, boolean success, ResultCode code){
		return build(null, msg, success, code);
	}
	
	public static class Result extends ModelResultInitialFactory{
		
		private static <E> ModelResultWrapper<E> clear(E value){
			return new ModelResultWrapper<E>(ApiResult.SUCCESS, ResultCode.CODE_200).setData(value);
		}
	}
}
