package org.daijie.shiro.security.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.daijie.core.controller.enums.ResultCode;
import org.daijie.core.result.ApiResult;
import org.daijie.core.result.factory.ModelResultInitialFactory.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * 处理异常默认返回数据
 * @author daijie_jay
 * @since 2018年6月11日
 */
public class ResultErrorZuulFilter extends ZuulFilter {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		HttpServletResponse response = ctx.getResponse();
        logger.debug("error uri:" + request.getRequestURI(), ctx.getThrowable());
        PrintWriter out = null;
		try{
			response.setContentType("application/json;charset=utf-8");
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			out.write(Result.build(null, ctx.getThrowable().getMessage(), ApiResult.ERROR, ResultCode.CODE_500).toJsonStr());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.ERROR_TYPE;
	}

	@Override
	public int filterOrder() {
		return FilterConstants.SEND_ERROR_FILTER_ORDER;
	}

}
