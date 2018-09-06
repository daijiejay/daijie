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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

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
	
	private final AbstractHandlerMethodMapping<RequestMappingInfo> objHandlerMethodMapping;// = requestMappingHandlerMapping
	
	@SuppressWarnings("unchecked")
	public ParametersFilter(HttpRequestProperties requestProperties, AbstractHandlerMapping requestMappingHandlerMapping) {
		this.requestProperties = requestProperties;
		this.objHandlerMethodMapping = (AbstractHandlerMethodMapping<RequestMappingInfo>) requestMappingHandlerMapping;
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
		if (RequestContextHolder.getRequestAttributes() == null) {
			RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(hreq));
		}
		if (this.requestProperties.getRemoteAjaxEanble()) {
			String originUrl = hreq.getHeader("Origin");
			if (this.requestProperties.getAccessControlAllowOrigin().length == 1 
					&& this.requestProperties.getAccessControlAllowOrigin()[0].equals("*")) {
				hres.setHeader(REMOTE_AJAX_ORIGIN, this.requestProperties.getAccessControlAllowOrigin()[0]);
			} else if (originUrl != null && originUrl.equals("null")) {
				hres.setHeader(REMOTE_AJAX_ORIGIN, originUrl);
			}else {
				for (String url : this.requestProperties.getAccessControlAllowOrigin()) {
					if (originUrl != null && originUrl.contains(url)) {
						hres.setHeader(REMOTE_AJAX_ORIGIN, originUrl);
					}
				}
			}
			hres.setHeader(REMOTE_AJAX_METHODS, this.requestProperties.getAccessControlAllowMethods());
			hres.setHeader(REMOTE_AJAX_HEADERS, this.requestProperties.getAccessControlAllowHeaders());
			hres.setHeader(REMOTE_AJAX_CREDENTIALS, "true");
		}
		if (this.requestProperties.getBodyByParamEanble() && !StringUtils.isEmpty(this.requestProperties.getBodyByParamMethods())) {
			if (this.requestProperties.getBodyByParamMethods().contains(reqMethod)) {
				ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(hreq, this.objHandlerMethodMapping);
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