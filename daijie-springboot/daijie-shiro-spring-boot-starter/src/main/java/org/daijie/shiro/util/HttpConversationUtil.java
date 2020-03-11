package org.daijie.shiro.util;

import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.security.token.SSOToken;
import org.daijie.common.http.SpringHttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 获取http会话数据的工具类
 * 
 * @author daijie
 * @since 2017年6月5日
 */
public class HttpConversationUtil {
	
	private static Logger logger = LoggerFactory.getLogger(HttpConversationUtil.class);

	/**
	 * 获取当前请求会话IP城市地址
	 * @return String
	 */	
	public String getIPAddress(){
		try {
			return MacAddressUtil.ip2Location(SpringHttpUtil.getIP());
		} catch (Exception e) {
			logger.error("IP Address not found!");
			return "IP Address not found!";
		}
	}

	/**
	 * 获取当前请求会话token
	 * @param tokenName 存储token的key
	 * @return String
	 */
	public static String getToken(String tokenName){
		if(SpringHttpUtil.getRequest() != null){
			SSOToken ssoToken = null;
			try {
				ssoToken = SSOHelper.getSSOToken(SpringHttpUtil.getRequest());
			} catch (Exception e) {
			}
			if(ssoToken != null && !StringUtils.isEmpty(ssoToken.getIssuer())){
				return ssoToken.getIssuer();
			}else if(!StringUtils.isEmpty(SpringHttpUtil.getRequest().getAttribute(tokenName))){
				return SpringHttpUtil.getRequest().getAttribute(tokenName).toString();
			}else if(!StringUtils.isEmpty(SpringHttpUtil.getRequest().getParameter(tokenName))){
				return SpringHttpUtil.getRequest().getParameter(tokenName);
			}else if(!StringUtils.isEmpty(SpringHttpUtil.getRequest().getHeader(tokenName))){
				return SpringHttpUtil.getRequest().getHeader(tokenName);
			}else if(!StringUtils.isEmpty(CookieUtil.get(tokenName))){
				return CookieUtil.get(tokenName);
			}
		}
		return null;
	}
}
