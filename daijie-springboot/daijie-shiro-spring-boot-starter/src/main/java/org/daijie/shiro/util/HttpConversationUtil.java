package org.daijie.shiro.util;

import com.baomidou.kisso.SSOHelper;
import com.baomidou.kisso.security.token.SSOToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取http会话数据的工具类
 * 
 * @author daijie
 * @since 2017年6月5日
 */
public class HttpConversationUtil {
	
	private static Logger logger = LoggerFactory.getLogger(HttpConversationUtil.class);
	
	/**
	 * 获取当前请求会话
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getRequest(){
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	/**
	 * 获取当前响应会话
	 * @return HttpServletResponse
	 */
	public static HttpServletResponse getResponse(){
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
	}

	/**
	 * 获取当前请求会话IP
	 * @return String
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
	 * @return String
	 */	
	public String getIPAddress(){
		try {
			return MacAddressUtil.ip2Location(getIP());
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
		if(getRequest() != null){
			SSOToken ssoToken = null;
			try {
				ssoToken = SSOHelper.getSSOToken(getRequest());
			} catch (Exception e) {
			}
			if(ssoToken != null && !StringUtils.isEmpty(ssoToken.getIssuer())){
				return ssoToken.getIssuer();
			}else if(!StringUtils.isEmpty(getRequest().getAttribute(tokenName))){
				return getRequest().getAttribute(tokenName).toString();
			}else if(!StringUtils.isEmpty(getRequest().getParameter(tokenName))){
				return getRequest().getParameter(tokenName);
			}else if(!StringUtils.isEmpty(getRequest().getHeader(tokenName))){
				return getRequest().getHeader(tokenName);
			}else if(!StringUtils.isEmpty(CookieUtil.get(tokenName))){
				return CookieUtil.get(tokenName);
			}
		}
		return null;
	}
	
	/**
	 * 获取请求Body转换为json字符串
	 * 
	 * @return String
	 */
	public static String getBodyString() {
		StringBuilder sb = new StringBuilder();
		InputStream inputStream = null;
		BufferedReader reader = null;
		try {
			inputStream = getRequest().getInputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
			String line = "";
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			logger.info("未读取到请求body中数据");
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.info("未读取到请求body中数据");
				}
			}
		}
		return sb.toString();
	}
	
    /**
     * 得到所有的参数的值
     * @return Map
     */
    public static Map<String,Object> handelRequest(){
        Map<String,Object> obj = new HashMap<String,Object>();
        Enumeration<String> coll =  getRequest().getParameterNames();
        while(coll.hasMoreElements()){
            String key = coll.nextElement();
            obj.put(key,  getRequest().getParameter(key)==null?"":getRequest().getParameter(key) );
        }
        if(obj.get("pageSize")!=null || obj.get("pageNumber")!=null){
            int pageSize = Integer.valueOf(obj.get("pageSize").toString());
            int pageNumber = Integer.valueOf(obj.get("pageNumber").toString());
            int startIndex = (pageNumber - 1) * pageSize;
            obj.put("startIndex", startIndex);
            obj.put("pageSize", pageSize);
            obj.put("pageNumber", pageNumber);
        }
        return obj;
    }
}
