package org.daijie.core.util.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie管理工具
 * @author daijie
 * @date 2017年6月21日
 */
public class CookieUtil {
	
	/**
	 * 获取
	 * @param request
	 * @param key
	 * @return
	 */
	public static String get(HttpServletRequest request, String key) {
		Cookie cookie[] = request.getCookies();
		if (cookie == null || cookie.length == 0) {
			return null;
		}
		for (int i = 0; i < cookie.length; i++) {
			if (cookie[i].getName().equals(key)) {
				return cookie[i].getValue();
			}
		}
		return null;
	}

	/**
	 * 设置
	 * @param response
	 * @param key
	 * @param value
	 * @param time
	 * @return
	 */
	public static HttpServletResponse set(HttpServletResponse response, String key, String value, Integer time) {
		Cookie cookie = new Cookie(key, value);
		if (time != null) {
			cookie.setMaxAge(time);
		}
		cookie.setPath("/");
		response.addCookie(cookie);
		return response;
	}
}
