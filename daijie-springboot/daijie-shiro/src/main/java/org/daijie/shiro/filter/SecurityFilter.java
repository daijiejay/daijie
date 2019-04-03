package org.daijie.shiro.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.PathMatchingFilter;
import org.daijie.core.controller.enums.ResultCode;
import org.daijie.core.result.ApiResult;
import org.daijie.core.result.ModelResult;
import org.daijie.core.result.factory.ModelResultInitialFactory.Result;
import org.daijie.shiro.authc.Auth;

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
		ModelResult<Object> result = null;
		try {
			if (Auth.isLogin()) {
				String[] rolesArray = (String[]) mappedValue;
				if (rolesArray == null || rolesArray.length == 0) {
					return secutity;
				}
				secutity = Auth.hasAnyRoles(rolesArray);
				if (!secutity) {
					result = Result.build(null, ResultCode.CODE_400.getDescription(), ApiResult.ERROR, ResultCode.CODE_400);
				}
			} else {
				secutity = false;
				result = Result.build(null, ResultCode.CODE_300.getDescription(), ApiResult.ERROR, ResultCode.CODE_300);
			}
		} catch (Exception e) {
			result = Result.build(null, e.getMessage(), ApiResult.ERROR, ResultCode.CODE_500);
			secutity = false;
		}
		if (!secutity) {
			PrintWriter out = null;
			try{
				response.setContentType("application/json;charset=utf-8");
				response.setCharacterEncoding("UTF-8");
				out = response.getWriter();
				out.write(result.toJsonStr());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					out.close();
				}
			}
		}
		return secutity;
	}
}
