package org.daijie.common.result;

import org.springframework.web.method.HandlerMethod;

/**
 * 异常时默认返回处理
 * @author daijie
 * @since 2020年2月25日
 */
public interface ResultExceptionHandler {

    /**
     * 运行时异常时处理默认返回结果
     * @param handlerMethod 运行时异常被接入的方法
     * @param exception 出现的具体异常类
     * @return 处理后的默认返回结果
     */
    Object resolveException(HandlerMethod handlerMethod, Exception exception);
}
