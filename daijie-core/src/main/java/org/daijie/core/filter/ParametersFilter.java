package org.daijie.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;

/**
 * 拦截请求对body参数进行处理
 * @author daijie
 * @date 2017年6月13日
 */
@WebFilter(filterName = "parametersFilter", urlPatterns = "/*", initParams = @WebInitParam(name = "paramName", value = "paramValue"))
public class ParametersFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest hreq = (HttpServletRequest) req;
		String reqMethod = hreq.getMethod();
		if (!"GET".equals(reqMethod)) {
			ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(hreq);
			chain.doFilter(requestWrapper, res);
			return;
		} else {
			// get请求直接放行
			chain.doFilter(req, res);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}