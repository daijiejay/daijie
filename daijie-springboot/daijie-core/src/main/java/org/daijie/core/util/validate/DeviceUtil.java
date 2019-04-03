package org.daijie.core.util.validate;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 检测访问设备
 * @author daijie
 * @since 2017年6月5日
 */
public class DeviceUtil {

	// \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),    
	// 字符串在编译时会被转码一次,所以是 "\\b"    
	// \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)    
	static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i"    
			+"|windows (phone|ce)|blackberry"    
			+"|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"    
			+"|laystation portable)|nokia|fennec|htc[-_]"    
			+"|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";    

	static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser"    
			+"|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";    

	//移动设备正则匹配：手机端、平板  
	static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);    
	static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);    

	/** 
	 * 检测是否是移动设备访问 
	 *  
	 * @param userAgent 浏览器标识 
	 * @return true:移动设备接入，false:pc端接入 
	 */  
	public static boolean check(String userAgent){    
		if(null == userAgent){    
			userAgent = "";    
		}    
		// 匹配    
		Matcher matcherPhone = phonePat.matcher(userAgent);    
		Matcher matcherTable = tablePat.matcher(userAgent);    
		if(matcherPhone.find() || matcherTable.find()){    
			return true;    
		} else {    
			return false;    
		}    
	}  

	/** 
	 * 检查访问方式是否为移动端 
	 *  
	 * @param request 请求对象
	 * @throws IOException 抛出异常
	 * @return boolean
	 */  
	public static boolean checkMobile(HttpServletRequest request) throws Exception{  
		boolean isFromMobile=false;  

		HttpSession session= request.getSession();  
		//检查是否已经记录访问方式（移动端或pc端）  
		if(null==session.getAttribute("ua")){  
			//获取ua，用来判断是否为移动端访问  
			String userAgent = request.getHeader( "USER-AGENT" ).toLowerCase();    
			if(null == userAgent){    
				userAgent = "";    
			}  
			isFromMobile = check(userAgent);  
			//判断是否为移动端访问  
			if(isFromMobile){
				isFromMobile = true;
			}  
		} 
		return isFromMobile;  
	} 
}
