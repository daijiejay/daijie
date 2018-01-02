package org.daijie.core.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 处理异常的controller
 * @author daijie_jay
 * @since 2018年1月1日
 */
@ControllerAdvice
public interface ExceptionController extends Controller {

	/**
	 * 异常处理
	 * @param e 捕获异常
	 * @return 返回异常处理完成后的返回数据
	 */
	@ExceptionHandler(Exception.class)
	Object exceptionHandler(Exception e);
}
