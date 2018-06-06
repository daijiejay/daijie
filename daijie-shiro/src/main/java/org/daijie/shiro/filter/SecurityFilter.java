package org.daijie.shiro.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.PathMatchingFilter;
import org.daijie.core.controller.enums.ResultCode;
import org.daijie.core.result.ApiResult;
import org.daijie.core.result.factory.ModelResultInitialFactory.Result;
import org.daijie.shiro.session.ShiroRedisSession.Redis;

/**
 * 请求拦截器
 * 用户是否登录，如果没有登录返回默认登录失败数据
 * @author daijie
 * @since 2017年9月3日
 */
public class SecurityFilter extends PathMatchingFilter {

	@Override
	public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		if(Redis.getSession() == null){
			PrintWriter out = null;
			try{
				response.setContentType("application/json;charset=utf-8");
				response.setCharacterEncoding("UTF-8");
				out = response.getWriter();
				out.write(Result.build(null, ResultCode.CODE_400.getDescription(), ApiResult.ERROR, ResultCode.CODE_400).toJsonStr());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					out.close();
				}
			}
			return false;
		}
		return true;
	}
}
