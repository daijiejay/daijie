package org.daijie.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.daijie.shiro.session.ShiroRedisSession.Redis;

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
