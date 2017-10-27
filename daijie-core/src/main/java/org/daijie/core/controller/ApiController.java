package org.daijie.core.controller;

import org.daijie.core.controller.enums.ResultCode;
import org.daijie.core.controller.exception.ApiException;
import org.daijie.core.factory.specific.ApiResultInitialFactory;
import org.daijie.core.factory.specific.ModelResultInitialFactory.Result;
import org.daijie.core.result.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 对contorller的公共属性及方法进行封装
 * @author daijie_jay
 *
 * @param <E>服务出现异常时进行处理
 */
@RequestMapping(produces = "application/json;charset=UTF-8")
public abstract class ApiController<S, E extends Exception> extends ApiResultInitialFactory 
		implements ExceptionController<E> {

	@Autowired
	public S service;
	
	public ApiController() {
		super();
	}

	/**
	 * 异常时，根据异常级别响应默认返回数据
	 */
	@Override
	public Object exceptionHandler(E e) {
		e.printStackTrace();
		if(e instanceof ApiException){
			return Result.build(null, e.getMessage(), ApiResult.ERROR, ((ApiException) e).getCode());
		}else{
			return Result.build(null, e.getMessage(), ApiResult.ERROR, ResultCode.CODE_500);
		}
	}
}
