package org.daijie.core.util;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HttpRequestUtil {
	
	private static Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);

	private final static String TOKEN_NAME = "token";
	
	public static HttpServletRequest getRequest(){
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}

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
	
	public String getIPAddress(){
		try {
			return MacAddressHelper.ip2Location(getIP());
		} catch (Exception e) {
			logger.error("IP Address not found!");
			return "IP Address not found!";
		}
	}
	
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
