package org.daijie.shiro.security.filter;

import com.netflix.zuul.ZuulFilter;
import org.daijie.shiro.session.ShiroRedisSession.Redis;
import org.daijie.swagger.result.Result;
import org.daijie.swagger.result.ResultCode;

/**
 * 权限拦截，安全控制，反向代理
 * @author daijie_jay
 * @since 2017年10月13日
 */
public class SecurityZuulFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		if(Redis.getSession() == null){
			return Result.build(null, "登录过期", Result.ERROR, ResultCode.CODE_300);
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
