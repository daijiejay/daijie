package org.daijie.core.util.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie管理工具
 * @author daijie
 * @since 2017年6月21日
 */
public class CookieUtil {
	
	/**
	 * 获取
	 * @param key 键
	 * @return String 值
	 */
	public static String get(String key) {
		Cookie cookie[] = HttpConversationUtil.getRequest().getCookies();
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
	 * @param key 键
	 * @param value 值
	 * @param time 超时时间
	 * @return HttpServletResponse
	 */
	public static HttpServletResponse set(String key, String value, Integer time) {
		HttpServletResponse response = HttpConversationUtil.getResponse();
		Cookie cookie = new Cookie(key, value);
		if (time != null) {
			cookie.setMaxAge(time);
		}
		cookie.setPath("/");
		response.addCookie(cookie);
		return response;
	}
}
