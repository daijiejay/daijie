package org.daijie.shiro.filter;

import org.apache.shiro.web.filter.PathMatchingFilter;
import org.daijie.shiro.authc.Auth;
import org.daijie.shiro.exception.NotAuthorizedException;
import org.daijie.shiro.exception.UserExpireException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 请求拦截器
 * 用户是否登录，如果没有登录返回默认登录失败数据
 * 验证角色权限
 * @author daijie
 * @since 2017年9月3日
 */
public class SecurityFilter extends PathMatchingFilter {

	@Override
	public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		boolean secutity = true;
		if (Auth.isLogin()) {
			String[] rolesArray = (String[]) mappedValue;
			if (rolesArray == null || rolesArray.length == 0) {
				return secutity;
			}
			secutity = Auth.hasAnyRoles(rolesArray);
			if (!secutity) {
				throw new NotAuthorizedException();
			}
		} else {
			secutity = false;
			throw new UserExpireException();
		}
		return secutity;
	}
}
