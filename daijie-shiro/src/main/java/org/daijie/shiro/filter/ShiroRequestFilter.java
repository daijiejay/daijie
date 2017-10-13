package org.daijie.shiro.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.daijie.core.filter.HeaderHttpServletRequestWrapper;
import org.daijie.core.filter.ParametersFilter;
import org.daijie.shiro.authc.ShiroConstants;

/**
 * 请求会话参数处理
 * @author daijie
 * @date 2017年6月13日
 */
public class ShiroRequestFilter extends ParametersFilter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest hreq = (HttpServletRequest) req;
		String uri = hreq.getRequestURI();
		String method = hreq.getMethod();
		if(uri.contains("middle[")){
			HeaderHttpServletRequestWrapper requestWrapper = new HeaderHttpServletRequestWrapper(hreq);
			requestWrapper.putHeader(ShiroConstants.REDIRECT_URI, uri);
			requestWrapper.putHeader(ShiroConstants.REDIRECT_METHOD, method);
			super.doFilter(requestWrapper, res, chain);
		}
		super.doFilter(req, res, chain);
	}

}