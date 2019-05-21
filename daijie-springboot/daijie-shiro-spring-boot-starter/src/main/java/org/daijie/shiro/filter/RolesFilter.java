package org.daijie.shiro.filter;

import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.daijie.shiro.session.ShiroRedisSession.Redis;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 角色请求权限拦截
 * @author daijie_jay
 * @since 2018年1月2日
 */
public class RolesFilter extends RolesAuthorizationFilter {

	@Override
	public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		boolean status = super.onPreHandle(request, response, mappedValue);
		if(Redis.getSession() == null || !status){
			return false;
		}
		return true;
	}
}
