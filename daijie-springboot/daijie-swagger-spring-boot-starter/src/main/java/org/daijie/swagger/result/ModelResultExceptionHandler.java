package org.daijie.swagger.result;

import org.daijie.common.result.ControllerExceptionHandlerResolver;
import org.daijie.swagger.exception.ApiException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

/**
 * 异常时默认返回结果数据封装
 *
 * @author daijie
 * @since 2020年02月25日
 */
@Component
public class ModelResultExceptionHandler extends ControllerExceptionHandlerResolver {

    @Override
    public Object resolveException(HandlerMethod handlerMethod, Exception exception) {
        if(exception instanceof ApiException){
            return Result.build(null, exception.getMessage(), Result.ERROR, ((ApiException) exception).getCode()).toJsonStr();
        }else if(exception.getMessage() != null && exception.getMessage().contains("org.daijie.shiro.exception.UserExpireException")){
            return Result.build(null, ResultCode.CODE_300.getDescription(), Result.ERROR, ResultCode.CODE_300).toJsonStr();
        }else if(exception.getMessage() != null && exception.getMessage().contains("com.netflix.client.ClientException")){
            return Result.build(null, ResultCode.CODE_501.getDescription(), Result.ERROR, ResultCode.CODE_501).toJsonStr()+exception.getMessage().substring(exception.getMessage().lastIndexOf(":"));
        }else{
            return Result.build(null, exception.getMessage(), Result.ERROR, ResultCode.CODE_500).toJsonStr();
        }
    }
}
