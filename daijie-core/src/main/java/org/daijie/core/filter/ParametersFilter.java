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
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

/**
 * 拦截请求对body参数进行处理
 * @author daijie
 * @since 2017年6月13日
 */
@WebFilter(filterName = "parametersFilter", urlPatterns = "/*", initParams = @WebInitParam(name = "paramName", value = "paramValue"))
public class ParametersFilter implements Filter {
	
	private static final String REMOTE_AJAX_ORIGIN = "Access-Control-Allow-Origin";
	private static final String REMOTE_AJAX_METHODS = "Access-Control-Allow-Methods";
	private static final String REMOTE_AJAX_HEADERS = "Access-Control-Allow-Headers";
	private static final String REMOTE_AJAX_CREDENTIALS = "Access-Control-Allow-Credentials";
	
	private final HttpRequestProperties requestProperties;
	
	public ParametersFilter(HttpRequestProperties requestProperties) {
		this.requestProperties = requestProperties;
	}

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest hreq = (HttpServletRequest) req;
		HttpServletResponse hres = (HttpServletResponse) res;
		String reqMethod = hreq.getMethod().toUpperCase();
		if (requestProperties.getRemoteAjaxEanble()) {
			String originUrl = hreq.getHeader("Origin");
			if (requestProperties.getAccessControlAllowOrigin().length == 1 
					&& requestProperties.getAccessControlAllowOrigin()[0].equals("*")) {
				hres.addHeader(REMOTE_AJAX_ORIGIN, requestProperties.getAccessControlAllowOrigin()[0]);
			} else if (originUrl != null && originUrl.equals("null")) {
				hres.addHeader(REMOTE_AJAX_ORIGIN, originUrl);
			}else {
				for (String url : requestProperties.getAccessControlAllowOrigin()) {
					if (originUrl != null && originUrl.contains(url)) {
						hres.addHeader(REMOTE_AJAX_ORIGIN, originUrl);
					}
				}
			}
			hres.addHeader(REMOTE_AJAX_METHODS, requestProperties.getAccessControlAllowMethods());
			hres.addHeader(REMOTE_AJAX_HEADERS, requestProperties.getAccessControlAllowHeaders());
			hres.addHeader(REMOTE_AJAX_CREDENTIALS, "true");
		}
		if (requestProperties.getBodyByParamEanble() && !StringUtils.isEmpty(requestProperties.getBodyByParamMethods())) {
			if (requestProperties.getBodyByParamMethods().contains(reqMethod)) {
				ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(hreq);
				chain.doFilter(requestWrapper, res);
				return;
			}
		}
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}