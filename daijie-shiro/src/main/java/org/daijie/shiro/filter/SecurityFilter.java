package org.daijie.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.PathMatchingFilter;
import org.daijie.shiro.session.ShiroRedisSession.Redis;

/**
 * 请求拦截器
 * 用户是否登录，如果没有登录返回默认登录失败数据
 * @author daijie
 * @date 2017年9月3日
 */
public class SecurityFilter extends PathMatchingFilter {

	@Override
	public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		if(Redis.getSession() == null){
			return false;
		}
		return true;
	}
}
