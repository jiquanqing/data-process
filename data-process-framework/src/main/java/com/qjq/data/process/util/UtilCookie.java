package com.qjq.data.process.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Cookie工具类
 * 
 * @since 0.1.0
 */
public class UtilCookie {
	
	public static void addCookie(HttpServletResponse response, String name, String value, String domain, int maxAge) {
		addCookie(response, name, value, domain, maxAge, false);
	}

	public static void addCookie(HttpServletResponse response, String name, String value, String domain, int maxAge, boolean isHttpOnly) {
		value = UtilString.encodeURL(value);
		Cookie cookie = new Cookie(name, value);
		cookie.setDomain(domain);
		cookie.setPath("/");
		cookie.setMaxAge(maxAge);
		cookie.setHttpOnly(isHttpOnly);
		response.addCookie(cookie);
	}

	public static Cookie getCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName)) {
					return cookie;
				}
			}
		}
		return null;
	}

	public static String getCookieValue(HttpServletRequest request, String cookieName) {
		if (cookieName != null) {
			Cookie cookie = getCookie(request, cookieName);
			if (cookie != null) {
				return UtilString.decodeURL(cookie.getValue());
			}
		}
		return null;
	}

	public static boolean deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String domain) {
		if (cookieName != null) {
			Cookie cookie = getCookie(request, cookieName);
			if (cookie != null) {
				cookie.setDomain(domain);
				cookie.setPath("/");
				cookie.setMaxAge(0);
				response.addCookie(cookie);
				return true;
			}
		}
		return false;
	}

}
