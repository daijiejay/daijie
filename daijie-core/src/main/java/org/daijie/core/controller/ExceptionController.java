package org.daijie.core.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 定义异常的controller
 * @author daijie
 *
 * @param <E>
 */
@ControllerAdvice
public interface ExceptionController<E extends Exception> extends Controller {

	/**
	 * 异常处理
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	Object exceptionHandler(E e);
}
