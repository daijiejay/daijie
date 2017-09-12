package org.daijie.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;
import org.daijie.core.controller.exception.ApiException;
import org.daijie.shiro.authc.ShiroConstants;
import org.daijie.shiro.session.ShiroRedisSession.Redis;

/**
 * 请求拦截器
 * 用户是否登录，如果没有登录返回默认登录失败数据
 * 登录状态时重定向到指定的服务,指定的服务在请求header里设置service-name
 * @author daijie
 * @date 2017年9月3日
 */
public class SecurityFilter extends UserFilter {

	@Override
	public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		boolean status = super.onPreHandle(request, response, mappedValue);
		if(Redis.getSession() == null || status){
			throw new ApiException("请先登录");
		}
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String uri = httpServletRequest.getRequestURI();
		String method = httpServletRequest.getMethod();
		String service = httpServletRequest.getHeader("service-name");
		if(service == null){
			throw new ApiException("请求header中没有参数'service-name'");
		}
		Redis.setAttribute(ShiroConstants.REDIRECT_URI, uri);
		Redis.setAttribute(ShiroConstants.REDIRECT_METHOD, method);
		redirectToSecurity(request, response, service);
		return false;
	}
	
	/**
	 * 重定向service服务
	 * @param request
	 * @param response
	 * @param redirect
	 * @throws IOException
	 */
	protected void redirectToSecurity(ServletRequest request, ServletResponse response, String redirect) throws IOException {
        WebUtils.issueRedirect(request, response, redirect);
    }

}
