package org.daijie.core.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 处理异常的controller
 * @author daijie
 *
 * @param <E>
 */
@ControllerAdvice
public interface ExceptionController extends Controller {

	/**
	 * 异常处理
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	Object exceptionHandler(Exception e);
}
