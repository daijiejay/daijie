package org.daijie.shiro.security.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.daijie.swagger.result.Result;
import org.daijie.swagger.result.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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
			if (ctx.getThrowable() instanceof ZuulException) {
				out.write(Result.build(null, ResultCode.CODE_501.getDescription(), Result.ERROR, ResultCode.CODE_501).toJsonStr());
			} else {
				out.write(Result.build(null, ctx.getThrowable().getMessage(), Result.ERROR, ResultCode.CODE_500).toJsonStr());
			}
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
