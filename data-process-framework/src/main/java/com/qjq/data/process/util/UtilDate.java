package com.qjq.data.process.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * pattern 必须存在且正确 (TIP: 为什么？调用者应该自己在做什么.)<br>
 * 时区：ll /usr/share/zoneinfo/
 * @since 2012-8-7
 * @version $Revision: $
 */
public class UtilDate {
	public static final TimeZone TZ_SHANG_HAI = TimeZone.getTimeZone("Asia/Shanghai"),
			TZ_AMERICA_NEW_YORK = TimeZone.getTimeZone("America/New_York");
	/** "yyyy-MM-dd HH:mm:ss.SSS" */
	public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final long uplimitTime = parseDate("1975-01-01").getTime();
	
	public static Date parseDate(String date, String pattern) {
		if (date == null || date.isEmpty()) return null;
		try {
			return new SimpleDateFormat(pattern.substring(0, date.length())).parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException(pattern + " " + date, e);
		}
	}
	/** 日期格式： {@linkplain #DEFAULT_PATTERN} */
	public static Date parseDate(String date) {
		return parseDate(date, DEFAULT_PATTERN);
	}
	public static String format(Date date, String pattern) {
		return date == null ? null : new SimpleDateFormat(pattern).format(date);
	}
	/** {@linkplain #DEFAULT_PATTERN} */
	public static String format(Date date) {
		return format(date, DEFAULT_PATTERN);
	}
	public static String format(Long date) {
		return format(new Date(date), DEFAULT_PATTERN);
	}
	
	/** 转化为可读的持续时间格式 */
	public static String prettyTime(Number start, Number end) {
		if (start == null && end == null) return null;
		if (start == null) return "null --> " + format(new Date(end.longValue()), DEFAULT_PATTERN);
		if (end == null) return format(new Date(start.longValue()), DEFAULT_PATTERN) + " --> null";
		
		long time = end.longValue() - start.longValue();
		return time < 0 ? "-" + prettyTime(-time) : prettyTime(time);
	}
	/** 转化为可读的持续时间格式 */
	public static String prettyTime(Number detTime) {
		if (detTime == null) return null;
		
		long time = detTime.longValue();
		StringBuilder buf = new StringBuilder(128);
		int tmp = (int) (time / (24 * 3600 * 1000));
		if (tmp > 0) buf.append(tmp).append('天');
		time %= (24 * 3600 * 1000);
		if (time == 0) return buf.length() == 0 ? "0秒" : buf.toString();
		
		tmp = (int) (time / (3600 * 1000));
		if (tmp > 0) buf.append(tmp).append('时');
		time %= (3600 * 1000);
		if (time == 0) return buf.length() == 0 ? "0秒" : buf.toString();
		
		tmp = (int) (time / (60 * 1000));
		if (tmp > 0) buf.append(tmp).append('分');
		time %= (60 * 1000);
		if (time == 0) return buf.length() == 0 ? "0秒" : buf.toString();
		
		buf.append(time/1000f).append('秒');
		return buf.toString();
	}
	
	public static String formatOfNow(String pattern) {
		return format(new Date(), pattern);
	}
	
	/** add日期的字段值
	 * @param date
	 * @param field {@linkplain Calendar}
	 * @param diff
	 * @return 目标日期
	 */
	public static Date addByField(Date date, int field, int diff) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int value = calendar.get(field) + diff;
		calendar.set(field, value);
		return calendar.getTime();
	}
	
	/** @return 当前时间对齐到秒 */
	public static long currentTimeBySecond() {
		long ms = System.currentTimeMillis();
		return ms / 1000 * 1000;
	}
	
	public static DateFormat getHttpLastModifiedFormat() {
		SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.UK);
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		return format;
	}
	public static Date parseHttpLastModified(String lastModified) throws ParseException {
		return getHttpLastModifiedFormat().parse(lastModified);
	}
	public static String formatHttpLastModified(Date date) {
		return getHttpLastModifiedFormat().format(date);
	}
	public static Date getBeginOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime() / 1000 * 1000);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	public static Date getEndOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime() / 1000 * 1000);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 59);
		return calendar.getTime();
	}
	public static Date getBeginOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime() / 1000 * 1000);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	/** 秒的时间转换为正确的毫秒表示的 */
	public static Date toDateFromPhp(Date date) {
		if (date == null) return null;
		long time = date.getTime();
		if (time > uplimitTime) return date;
		return new Date(time * 1000);
	}
	/** 秒的时间转换为正确的毫秒表示的 */
	public static Long toDateFromPhp(Long time) {
		if (time == null) return null;
		if (time > uplimitTime) return time;
		return time * 1000;
	}
}