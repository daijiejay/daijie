package org.daijie.core.util;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 * @author daijie
 * @date 2017年6月5日
 * 获取http请求数据的工具类
 * 
 */
public class HttpRequestUtil {
	
	private static Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);

	private final static String TOKEN_NAME = "token";
	
	/**
	 * 获取当前请求会话
	 * @return
	 */
	public static HttpServletRequest getRequest(){
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * 获取当前请求会话IP
	 * @return
	 */
	public static String getIP(){
		String ip = getRequest().getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = getRequest().getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = getRequest().getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = getRequest().getRemoteAddr();
	    }
	    return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}

	/**
	 * 获取当前请求会话IP城市地址
	 * @return
	 */	
	public String getIPAddress(){
		try {
			return MacAddressHelper.ip2Location(getIP());
		} catch (Exception e) {
			logger.error("IP Address not found!");
			return "IP Address not found!";
		}
	}

	/**
	 * 获取当前请求会话token
	 * @return
	 */
	public static String getToken(){
		if(getRequest() != null){
			if(!StringUtils.isEmpty(getRequest().getAttribute(TOKEN_NAME))){
				return getRequest().getAttribute(TOKEN_NAME).toString();
			}else if(!StringUtils.isEmpty(getRequest().getParameter(TOKEN_NAME))){
				return getRequest().getParameter(TOKEN_NAME);
			}else if(!StringUtils.isEmpty(getRequest().getHeader(TOKEN_NAME))){
				return getRequest().getHeader(TOKEN_NAME);
			}
		}
		return null;
	}
}
