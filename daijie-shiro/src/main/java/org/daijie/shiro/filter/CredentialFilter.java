package org.daijie.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.daijie.core.util.encrypt.RSAUtil;
import org.daijie.core.util.http.HttpConversationUtil;
import org.daijie.shiro.authc.ShiroConstants;
import org.daijie.shiro.session.ShiroRedisSession.Redis;

/**
 * 初始匹配凭证
 * @author daijie
 * @date 2017年7月1日
 */
public class CredentialFilter extends PathMatchingFilter {

	@Override
	protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) {
		if(Redis.getSession() == null){
			Subject subject = SecurityUtils.getSubject();
			request.setAttribute(HttpConversationUtil.TOKEN_NAME, subject.getSession().getId());
		}
		RSAUtil.init();
		Redis.setAttribute(ShiroConstants.RSA_PUBLIC_KEY + Redis.getSession().getId(), RSAUtil.getPubKey());
		Redis.setAttribute(ShiroConstants.RSA_PRIVATE_KEY + Redis.getSession().getId(), RSAUtil.getPriKey());
		return true;
	}

}
