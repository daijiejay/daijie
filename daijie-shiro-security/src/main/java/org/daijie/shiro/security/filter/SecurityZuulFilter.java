package org.daijie.shiro.security.filter;

import org.daijie.core.controller.enums.ResultCode;
import org.daijie.core.factory.specific.ModelResultInitialFactory.Result;
import org.daijie.core.result.ApiResult;
import org.daijie.shiro.session.ShiroRedisSession.Redis;

import com.netflix.zuul.ZuulFilter;

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
			return Result.build(null, "登录过期", ApiResult.ERROR, ResultCode.CODE_300);
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
