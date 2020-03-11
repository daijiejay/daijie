package org.daijie.shiro.security.swagger;

import springfox.documentation.swagger2.web.Swagger2Controller;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 调用官方默认资源路径跳转拦截
 * @author daijie_jay
 * @since 2018年8月25日
 */
public class FocusSwaggerFilter implements Filter {

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (((HttpServletRequest) request).getRequestURI().contains(Swagger2Controller.DEFAULT_URL)) {
			((HttpServletResponse) response).sendRedirect(FocusSwaggerController.DEFAULT_URL + FocusSwaggerController.PARAM + ((HttpServletRequest) request).getParameter("group"));
		}
		if (((HttpServletRequest) request).getRequestURI().equals(FocusSwaggerController.SWAGGER_RESOURCES_URL)) {
			((HttpServletResponse) response).sendRedirect(FocusSwaggerController.RESOURCES_URL);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}
}
