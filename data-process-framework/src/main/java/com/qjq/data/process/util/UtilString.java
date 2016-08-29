/** Copyright 2013-2023 步步高商城. */
package com.qjq.data.process.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @since 0.1.0
 */
public class UtilString {
	private static final Random RANDOM = new Random();
	public static final Set<String> CHINA_PHONES = new TreeSet<>();
	private static final Character[] RANDOM_CHARS;
	private static final char SEPARATOR = '_';
	public static final int md5Len = md5("empty").length();
	
	static {
		List<Character> chars = new ArrayList<>();
		for (int i = 'a'; i <= 'z'; i++) { // 去掉字母l和o
			if (i != 'l' && i != 'o') chars.add((char) i);
		}
		for (int i = 'A'; i <= 'Z'; i++) { // 去掉字母l和o
			if (i != 'L' && i != 'O') chars.add((char) i);
		}
		for (int i = '2'; i <= '9'; i++) { //去掉数字0和1，
			chars.add((char) i);
		}
		
		Collections.shuffle(chars, RANDOM);
		RANDOM_CHARS = new Character[chars.size()];
		chars.toArray(RANDOM_CHARS);
		
		
		CHINA_PHONES.addAll(UtilString.split("130,131,132,145,155,156,185,186", ','));	//联通
		CHINA_PHONES.addAll(UtilString.split("134,135,136,137,138,139,150,151,152,157,158,159,147,182,183,184,187,188", ','));	//移动
		CHINA_PHONES.addAll(UtilString.split("133,153,180,181,189", ','));	//电信
		CHINA_PHONES.addAll(UtilString.split("140,141,142,143,144,146,148,149,154", ','));	//未知号段
		CHINA_PHONES.addAll(UtilString.split("176,177,178", ',')); // 4G号段
		CHINA_PHONES.add("170");
	}
	
	private static String fetchDigit(String text, int start) {
		if (text == null) return "";
		
		StringBuilder buf = new StringBuilder();
		boolean begin = false;
		for (int len = text.length(); start < len; start++) {
			char ch = text.charAt(start);
			if (ch >= '0' && ch <= '9') {
				begin = true;
				buf.append(ch);
			} else if (begin) {
				break;
			}
		}
		return buf.toString();
	}
	public static Integer fetchInt(String text, int start) {
		String value = fetchDigit(text, start);
		return value.length() > 0 ? Integer.valueOf(value.toString()) : null;
	}
	public static Long fetchLong(String text, int start) {
		String value = fetchDigit(text, start);
		return value.length() > 0 ? Long.valueOf(value.toString()) : null;
	}

	public static String toString(Object value) {
		return value == null ? null : value.toString();
	}
	public static String toString(Object value, Object defaultValue) {
		return value == null ? toString(defaultValue) : value.toString();
	}
	
	public static boolean isEmpty(Object value, boolean trim) {
		if (value == null) return true;
		String text = value instanceof String ? (String) value : value.toString();
		return (trim ? text.trim() : text).isEmpty();
	}
	public static boolean notEmpty(Object value, boolean trim) {
		return !isEmpty(value, trim);
	}
	/** 不执行trim */
	public static boolean isEmpty(Object value) {
		return value == null || (value instanceof String ? (String) value : value.toString()).isEmpty();
	}
	/** 不执行trim */
	public static boolean notEmpty(Object value) {
		return !isEmpty(value);
	}
	public static String trim(String value) {
		return value == null ? null : value.trim();
	}
	/**
	 * @param emptyToNull trim后，如果是空串，将返回null
	 */
	public static String trim(String value, boolean emptyToNull) {
		if (value == null) return null;
		value = value.trim();
		return emptyToNull && value.isEmpty() ? null : value;
	}
	
    /**
     * 切分后自动执行trim
     * @see #split(String, char, boolean, Integer)
     */
   	public static List<String> split(String text, final char splitChar) {
   		return split(text, splitChar, true, text.length() / 16);
   	}
    /**
	 * 非正则表达式方式split，大量计算时可以节省cpu<br>
	 * 如果做缓存时，注意String的特性——共享一个char数组，潜在的jvm内存泄漏 
	 * @param text	原始文本
	 * @param splitChar 分割字符
	 * @param initialCapacity 初始化List容量大小
	 * @return 切分后字符串
	 */
	public static List<String> split(String text, final char splitChar, boolean trim, Integer initialCapacity) {
		List<String> result = (initialCapacity == null || initialCapacity < 10) ? new ArrayList<String>()
				: new ArrayList<String>(initialCapacity);
		int start = 0, len = text.length();
		for (int i = 0; i < len; i++) {
			if (splitChar == text.charAt(i)) {
				String tmp = start == i ? "" : text.substring(start, i);
				result.add(trim ? tmp.trim() : tmp);
				start = i + 1;
			}
		}
		if (start <= len) {
			String tmp = text.substring(start);
			result.add(trim ? tmp.trim() : tmp);
		}
		return result;
	}
	
	/** 耗时只有{@linkplain java.math.BigInteger#toString(int) new BigInteger(bytes).toString(16)}的40% */
	public static String toHexString(byte[] bytes) {
    	StringBuilder buf = new StringBuilder(bytes.length * 2);
    	for (int i = 0, len = bytes.length, value; i < len; i++) {
    		value = bytes[i] & 0xff;
    		if (value < 0x10) buf.append('0');
			buf.append(Integer.toHexString(value));
		}
        return buf.toString();
	}
	private static int toInt(char ch) {
		return ch - (ch > '9' ? 'a' - 10 : '0');
	}
	/** 耗时只有{@linkplain java.math.BigInteger#toByteArray() new BigInteger(hex, 16).toByteArray()}的15% */
	public static byte[] toBytesFromHex(String hex) {
		int len = hex.length();
		
		byte[] bytes = new byte[(len + 1)/2];
		int pos = 0, i = 0;
		if ((hex.length() & 1) == 1) {
			bytes[pos++] = (byte) toInt(hex.charAt(i++));
		}
		
		for (; i < len;) {
			bytes[pos++] = (byte) (toInt(hex.charAt(i++)) << 4 | toInt(hex.charAt(i++)));
		}
		return bytes;
	}
	
	/**
     * 将输入字符串经过MD5处理后返回
     * @param values 待处理字符串
     * @return MD5之后的结果
     */
    public static String md5(String... values) {
    	return md5(Arrays.asList(values));
    }
    /** 将输入字符串经过MD5处理后返回 */
    public static String md5(Collection<String> values) {
    	MessageDigest messageDigest = getMessageDigest("MD5");
    	for (String value : values) {
    		if (value != null) messageDigest.update(value.getBytes(UtilSys.UTF_8));
    	}
        return toHexString(messageDigest.digest());
    }
    /** @param algorithm 例如：MD5 */
    public static MessageDigest getMessageDigest(String algorithm) {
    	try {
			return MessageDigest.getInstance(algorithm.toUpperCase());
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("MD5 provider not exist!");
    	}
    }

    public static byte[] doFinal(String algorithm, byte[] key, int opmode, byte[] input) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    	algorithm = trim(algorithm);
    	if (algorithm == null) throw new IllegalArgumentException("加密算法不能为空");
    	algorithm = algorithm.toUpperCase();
    	if ("AES".equals(algorithm) && (key == null || key.length != 16)) throw new IllegalArgumentException("AES私钥长度必须是16");
    	
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(opmode, new SecretKeySpec(key, algorithm));
		return cipher.doFinal(input);
    }
    /**
     * AES加密
     * @param key 私钥key
     * @param text 待加密字符串
     * @return 加密后的16进制数字串
     */
	public static String encodeAES(byte[] key, String text) throws IllegalArgumentException {
		try {
			byte[] result = doFinal("AES", key, Cipher.ENCRYPT_MODE, text.getBytes(UtilSys.UTF_8));
			return toHexString(result);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			throw new IllegalArgumentException(e);
		}
	}
	/**
     * AES解密
     * @param key 私钥key
     * @param hex 待解密16进制串
     */
	public static String decodeAES(byte[] key, String hex) {
		try {
			byte[] result = doFinal("AES", key, Cipher.DECRYPT_MODE, toBytesFromHex(hex));
			return new String(result, UtilSys.UTF_8);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static String encodeURL(Object value) {
		if (value == null) return "";
		try {
			return URLEncoder.encode(value.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}
	public static String decodeURL(Object value) {
		if (value == null) return null;
		try {
			return URLDecoder.decode(value.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public static Properties toProps(String text) {
		Properties props = new Properties();
		try (InputStream in = new ByteArrayInputStream(text.getBytes(UtilSys.UTF_8))) {
			props.load(in);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return props;
	}

	/** 扩展text到指定长度的字节, 长度不足循环补充，长度超过的从头开始执行(相加) */
	public static byte[] extendToByte(String text, int len) {
		byte[] target = new byte[len];
		if (isEmpty(text)) return target;
		
		char[] chars = text.toCharArray();
		int srcLen = chars.length;
		
		if (len <= srcLen) {
			for (int srcPos = 0, targetPos = 0; srcPos < srcLen; srcPos++, targetPos++) {
				if (targetPos >= len) targetPos = 0;
				target[targetPos] += chars[srcPos];
			}
		} else {
			for (int targetPos = 0, srcPos = 0; targetPos < len; targetPos++, srcPos++) {
				if (srcPos >= srcLen) srcPos = 0;
				target[targetPos] += chars[srcPos];
			}
		}
		return target;
	}

	public static String urlAppendPath(String url, String path) {
		if (isEmpty(path)) return url;
		
		if (!url.isEmpty() && url.charAt(url.length() - 1) == '/') url = url.substring(0, url.length() - 1);
		return url + (path.charAt(0) == '/' ? path : ('/' + path));
	}
	
	
	/** Returns val represented by the specified number of hex digits. */
    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }
    /** uuid方法，结果中没有-符号，耗时只有replaceAll的30% */
	public static String uuid() {
		UUID uuid = UUID.randomUUID();
		long mostSigBits = uuid.getMostSignificantBits(), leastSigBits = uuid.getLeastSignificantBits();
		return digits(mostSigBits >> 32, 8)+ digits(mostSigBits >> 16, 4)
				+ digits(mostSigBits, 4) + digits(leastSigBits >> 48, 4) + digits(leastSigBits, 12);
        
	}
	
	public static String replaceAll(String text, char splitChar, String target) {
		boolean empty = target == null || target.isEmpty();
		StringBuilder buf = new StringBuilder(text.length() + (empty ? 0 : 128 * target.length()));
		for (char ch : text.toCharArray()) {
			if (ch == splitChar) {
				if (!empty) buf.append(target);
			} else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}
	
	public static String toQueryString(Object bean) {
		if (bean == null) return "";
		
		Map<?, ?> map = bean instanceof Map ? (Map<?, ?>) bean : UtilJson.toMap(bean);
		if (map.isEmpty()) return "";
		
		StringBuilder buf = new StringBuilder(1024);
		for (Entry<?, ?> entry : map.entrySet()) {
			buf.append(encodeURL(entry.getKey())).append('=').append(encodeURL(entry.getValue())).append('&');
		}
		return buf.substring(0, buf.length() - 1);
	}
	
	/**
	 * 断言此号码是中国的手机号码
	 * @param phone 手机号允许+86和86
	 * @return 中国手机号码（11位）
	 */
	public static String assertPhoneNumber(String phone) {
		if (phone == null) throw new IllegalArgumentException("请输入有效的手机号");
		String result = phone.trim();
		if (result.startsWith("+86")) {
			result = result.substring(3);
		} else if (result.startsWith("86")) {
			result = result.substring(2);
		}
//		if (result.length() == 11 && CHINA_PHONES.contains(result.substring(0, 3))) return result;
		if (result.length() == 11 && result.charAt(0) == '1') return result;
		throw new IllegalArgumentException("请输入有效的中国手机号，当前号码：" + phone);
	}
	
	/** 生成随机码(不包含数字0和1) */
	public static String randomCode(int len) {
		StringBuilder buf = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			buf.append(RANDOM_CHARS[RANDOM.nextInt(RANDOM_CHARS.length)]);
		}
		return buf.toString();
	}
	
	public static String toUnderlineName(String str) {
		if (str == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		boolean upperCase = false;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			boolean nextUpperCase = true;
			if (i < (str.length() - 1)) {
				nextUpperCase = Character.isUpperCase(str.charAt(i + 1));
			}
			if ((i >= 0) && Character.isUpperCase(c)) {
				if (!upperCase || !nextUpperCase) {
					if (i > 0)
						sb.append(SEPARATOR);
				}
				upperCase = true;
			} else {
				upperCase = false;
			}
			sb.append(Character.toLowerCase(c));
		}
		return sb.toString();
	}

	public static String toCamelCase(String str) {
		if (str == null) {
			return null;
		}
		str = str.toLowerCase();
		StringBuilder sb = new StringBuilder(str.length());
		boolean upperCase = false;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == SEPARATOR) {
				upperCase = true;
			} else if (upperCase) {
				sb.append(Character.toUpperCase(c));
				upperCase = false;
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	/** 输出第一个ch1或ch2出现的位置 */
	public static int indexOf(String text, char ch1, char ch2) {
		if (text == null || text.isEmpty()) return -1;
		int pos1 = text.indexOf(ch1), pos2 = text.indexOf(ch2);
		if (pos1 == -1) return pos2;
		if (pos2 == -1) return pos1;
		return pos1 > pos2 ? pos2 : pos1;
	}
	
	/**
	 * 替换字符为指定的字符
	 * @param text 原字符串
	 * @param start 开始位置（包含）
	 * @param end 结束位置（不包含）
	 * @param replacement 指定填充的字符
	 * @return 结果串
	 */
	public static String replace(String text, int start, int end, char replacement) {
		if (text == null || text.isEmpty()) return text;
		if (end > text.length()) end = text.length();
		StringBuilder buf = new StringBuilder(text);
		for (int i = start; i < end; i++) {
			buf.setCharAt(i, replacement);
		}
		return buf.toString();
	}
	
	public static String substring(String text, int beginIndex, int endIndex) {
		if (text == null) return null;
		if (beginIndex < 0) beginIndex = 0;
		if (beginIndex > text.length() - 1) return "";
		if (endIndex > text.length()) endIndex = text.length();
		return text.substring(beginIndex, endIndex);
	}
	
	/** parent路径后面追加子路径，如果有任何一个为null返回空 */
	public static String appendPath(String parent, String child) {
		if (parent == null || child == null) return null;
		if (parent.isEmpty()) return child;
		if (child.isEmpty()) return parent;
		
		if (parent.endsWith("/")) {
			return parent + (child.startsWith("/") ? child.substring(1) : child);
		} else {
			return parent + (child.startsWith("/") ? child : "/" + child);
		}
	}

	/** 数字字符串 */
	public static boolean isDigits(String text) {
		if (isEmpty(text)) return false;
		for (int i = 0, len = text.length(); i < len; i++) {
			char ch = text.charAt(i);
			if (ch < '0' || ch > '9') return false; 
		}
		return true;
	}
	/** 手机号码判断 */
	public static boolean isMobile(String mobile) {
		return isDigits(mobile) && mobile.length() == 11 && CHINA_PHONES.contains(mobile.substring(0, 3));
	}
	/** 邮箱判断 */
	public static boolean isEmail(String email) {
		return notEmpty(email) && Pattern.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", email);
	}
	
	public static String toPrivacy(String text) {
		if (UtilString.isEmpty(text)) return null;
		return replace(text, text.length() / 3, text.length() * 2 / 3, '*');
	}
	
	public static String toString(Collection<?> collection, int maxLen) {
		if (collection == null) return null;
		
		StringBuilder buf = new StringBuilder(12 * maxLen);
		buf.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
			buf.append(iterator.next()).append(", ");
		}
		if (buf.length() > 1) buf.deleteCharAt(buf.length() - 1);
		buf.append("]");
		return buf.toString();
	}
}
