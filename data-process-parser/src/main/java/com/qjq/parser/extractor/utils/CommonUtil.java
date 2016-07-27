package com.qjq.parser.extractor.utils;

import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;

import com.qjq.parser.extractor.exception.WrappedException;

/**
 * provide more fuctions to JDK Util calss
 * 
 * @author qingjiquan
 * 
 */
public class CommonUtil {
	/*
	 * exchange map's key & value. should key & value are one to one. special
	 * for segment tag encode.
	 */
	static public Map<String, String> exchangeMapKeyValue(Map<String, Character> sourceMap) {
		Map<String, String> exchangeMap = new HashMap<String, String>();

		for (String key : sourceMap.keySet()) {
			Object value = sourceMap.get(key);
			exchangeMap.put(value.toString(), key);
		}
		return exchangeMap;
	}

	static public String getUidFromUrl(String url) {
		byte[] source = url.getBytes();

		char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
		        'e', 'f'};
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest();

			char str[] = new char[32];

			int k = 0;
			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}

			StringBuilder result = new StringBuilder();

			for (int i = 0; i < 16; i++) {
				char v1 = new Character(str[i]);
				char v2 = new Character(str[16 + i]);

				int n1 = Integer.parseInt("" + v1, 16);
				int n2 = Integer.parseInt("" + v2, 16);

				int n = n1 ^ n2;
				String s = Integer.toString(n, 16);
				result.append(s);
			}
			return result.toString();

		} catch (Exception e) {
			throw new WrappedException(e);
		}
	}

	static public String getPageQueryFromUrl(String url) {
		if (url.length() < 10) {
			return null;
		}

		url = url.substring(10);

		int ind = url.lastIndexOf("/");
		if (ind <= 0) {
			return null;
		}
		if (ind == url.length() - 1) {
			return null;
		}
		String pageQuery = url.substring(ind + 1);

		return pageQuery;
	}

	static public String getPageNameFromUrl(String url) {
		String pageQuery = getPageQueryFromUrl(url);
		if (pageQuery == null) {
			return null;
		}
		int ind = pageQuery.indexOf("?");
		if (ind >= 0) {
			return pageQuery.substring(0, ind);
		} else {
			return pageQuery;
		}
	}

	public static String getHostName(String url) {
		if (url.length() > 9) {
			int idx = url.indexOf("//", 0);
			if (idx > 0) {
				int idxe = url.indexOf("/", idx + 2);
				if (idxe > 0) {
					return url.substring(idx + 2, idxe);
				} else {
					return url.substring(idx + 2);
				}
			}
		}
		return "";
	}
	public static boolean matchStr(String pattern, String str) {
		if (pattern == null || str == null) {
			return false;
		}
		return Pattern.compile(pattern).matcher(str).matches();
	}

	public static String removeHuanHang(String text) {
		if (text == null)
			return "";
		return text.replace(String.valueOf((char) 160), " ").replaceAll("\\s", "");
	}
	public static String removeInvalidChar(String str) {
		return str.replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5.，,。？“”]+", "");
	}

	public static String removeHeadBlank(String text) {
		if (text == null) {
			return text;
		}
		text = text.replaceAll("\\s", " ");
		for (int i = 0, len = text.length(); i < len; i++) {
			if (text.charAt(i) != ' ') {
				return text.substring(i);
			}
		}
		return "";
	}
	public static String removeInvaildUnicode(String str) {
		for (int i = 0; i < str.length(); i++) {

		}
		return str;
	}

}
