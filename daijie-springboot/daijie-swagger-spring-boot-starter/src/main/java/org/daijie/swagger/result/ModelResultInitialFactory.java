package org.daijie.swagger.result;

import org.daijie.core.factory.Factory;

/**
 * 实例工厂，实例具体返回对象
 * @author daijie
 * @since 2017年10月27日
 */
public abstract class ModelResultInitialFactory implements Factory {
	
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

	public static <E> ModelResult<E> build(E value, ResultCode code){
		return Result.clear(value).setCode(code).build();
	}

	public static <E> ModelResult<E> build(E value, String msg, boolean success, ResultCode code){
		return Result.clear(value).setMsg(msg).setSuccess(success).setCode(code).build();
	}

	public static <E> ModelResult<E> build(ResultCode code){
		return build(null, code);
	}

	public static <E> ModelResult<E> build(String msg, boolean success){
		return build(null, msg, success);
	}

	public static <E> ModelResult<E> build(String msg, boolean success, ResultCode code){
		return build(null, msg, success, code);
	}
}
