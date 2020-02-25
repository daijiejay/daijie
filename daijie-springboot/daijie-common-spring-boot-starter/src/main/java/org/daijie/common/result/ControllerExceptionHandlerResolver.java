package org.daijie.common.result;

import org.daijie.common.annotation.ErrorMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * http请求异常统一处理，实现spring自代的HandlerExceptionResolver异常处理器
 * 使用@RestController注解的类或@ResponseBody注解的方法，所有请求出现异常时返回默认数据
 * 使用@Controller注解的类，可在该类或类方法上配置注解@ErrorMapping，通过path属性配置出现请求异常时需要跳转的路径
 * @author daijie_jay
 * @since 2018年3月23日
 */
public abstract class ControllerExceptionHandlerResolver implements ResultExceptionHandler, HandlerExceptionResolver, Ordered {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private int order = Ordered.LOWEST_PRECEDENCE;
	
	private String errorMappingPath = "/error";

	@Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception exception) {
        logger.debug("error uri:" + request.getRequestURI(), exception);
        if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			Class<? extends Object> cls = handlerMethod.getBean().getClass();
			try {
				//可能会有空的情况，重新再获取
				if(cls.getAnnotations().length == 0){
					String typeName = cls.getTypeName();
					cls = Class.forName(typeName.substring(0, typeName.indexOf("$")));
				}
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			if(cls.isAnnotationPresent(RestController.class) || method.isAnnotationPresent(ResponseBody.class)){
				PrintWriter out = null;
				try{
					response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
					response.setCharacterEncoding("UTF-8");
					out = response.getWriter();
					out.write(this.resolveException(handlerMethod, exception).toString());
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						out.close();
					}
				}
				return new ModelAndView();
			}else if(cls.isAnnotationPresent(Controller.class)){
				ErrorMapping errorMapping = method.getAnnotation(ErrorMapping.class);
				if(errorMapping == null){
					errorMapping = cls.getAnnotation(ErrorMapping.class);
				}
				if(errorMapping != null){
					if(!"".equals(errorMapping.path())){
						return new ModelAndView("forward:" + errorMapping.path());
					}
				}
				return new ModelAndView("forward:" + errorMappingPath);
			}
			
		}
        return null;
    }

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getOrder() {
		return this.order;
	}
}
