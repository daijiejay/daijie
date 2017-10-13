package org.daijie.shiro.filter;

import org.daijie.core.controller.exception.ApiException;
import org.daijie.shiro.session.ShiroRedisSession.Redis;

import com.netflix.zuul.ZuulFilter;

/**
 * 权限拦截，安全控制，反向代理
 * @author daijie_jay
 * @date 2017年10月13日
 */
public class SecurityZuulFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		if(Redis.getSession() == null){
			throw new ApiException("请先登录");
		}
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

}
