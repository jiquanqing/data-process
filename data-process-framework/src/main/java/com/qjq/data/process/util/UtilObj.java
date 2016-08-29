/** Copyright 2013-2023 步步高商城. */
package com.qjq.data.process.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;

/**
 * 对象相关工具方法，处理反射
 * @since 0.1.0
 */
public class UtilObj {
	
	public static ClassLoader getClassLoader() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		return cl == null ? UtilObj.class.getClassLoader() : cl;
	}
	
	private static Field getField(Class<?> clazz, String fieldName) throws IllegalArgumentException {
		if (fieldName == null || fieldName.isEmpty()) throw new IllegalArgumentException(clazz + " not found fieldName=" + fieldName);
		if (clazz.isInterface()) throw new IllegalArgumentException(clazz + " not found fieldName=" + fieldName);
		
		for (Field field : clazz.getDeclaredFields()) {
			if (field.getName().equals(fieldName)) return field;
		}
		
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null && !superClass.isInterface()) return getField(superClass, fieldName);
		throw new IllegalArgumentException(clazz + " not found fieldName=" + fieldName);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getFieldValue(Object bean, String fieldName) {
		if (bean == null) return null;
		
		Class<?> clazz = bean.getClass();
		try {
			Field field = getField(clazz, fieldName);
			if (!field.isAccessible()) field.setAccessible(true);
			return (T) field.get(bean);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("在 " + bean + " 中找不到field：" + fieldName, e);
		}
	}
	public static void setFieldValue(Object bean, String fieldName, Object value) {
		if (bean == null) return;
		
		Class<?> clazz = bean.getClass();
		try {
			Field field = getField(clazz, fieldName);
			if (!field.isAccessible()) field.setAccessible(true);
			field.set(bean, value);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("在 " + bean + " 中找不到field：" + fieldName, e);
		}
	}

	public static Integer toInteger(Object value) {
		if (value == null) return null;
		if (value instanceof Integer) return (Integer) value;
		if (value instanceof Number) return ((Number) value).intValue();
		return Integer.parseInt(value.toString());
	}
	public static Long toLong(Object value) {
		if (value == null) return null;
		if (value instanceof Long) return (Long) value;
		if (value instanceof Number) return ((Number) value).longValue();
		String v = value.toString();
		if (v.isEmpty()) return null;
		return Long.parseLong(v);
	}
	public static BigDecimal toBigDecimal(Object value) {
		if (value == null) return null;
		if (value instanceof BigDecimal) return (BigDecimal) value;
		if (value instanceof Float) return BigDecimal.valueOf((Float) value);
		if (value instanceof Double) return BigDecimal.valueOf((Double) value);
		if (value instanceof Number) return BigDecimal.valueOf(((Number) value).longValue());
		return new BigDecimal(value.toString());
	}
	public static BigDecimal toBigDecimal(Object value, int scale, RoundingMode mode) {
		BigDecimal result = toBigDecimal(value);
		return result == null ? null : result.setScale(scale, mode);
	}

	/**
	 * 获取对象属性
	 * @return bean: null，返回null；bean：Map，返回Map.get；其他直接反射readMethod执行
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T getPropValue(Object bean, Object prop) {
		if (bean == null) return null;
		if (bean instanceof Map) return (T) ((Map) bean).get(prop);
		
		Class<?> clazz = bean.getClass();
		PropertyDescriptor propDesc = BeanUtils.getPropertyDescriptor(clazz, prop.toString());
//		if (propDesc == null) throw new IllegalArgumentException("Property[" + prop + "] not in " + bean);
		Method getter = propDesc.getReadMethod();
//		if (readMethod == null) throw new IllegalArgumentException("Property[" + prop + "] getter not in " + bean);
		if (!Modifier.isPublic(getter.getDeclaringClass().getModifiers())) getter.setAccessible(true);
		try {
			return (T) getter.invoke(bean);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new IllegalArgumentException("Property[" + prop + "] getter invoke in " + bean, e);
		}
	}
	@SuppressWarnings("unchecked")
	private static <T> T getValueByPath(Object root, int pos, Object[] paths) {
		if (paths.length == 0) return (T) root;
		final Object key = paths[pos], value = getPropValue(root, key);
		if (value == null) return null;
		if (pos == paths.length - 1) return (T) value;
		return getValueByPath(value, pos + 1, paths);
	}
	/**
	 * 从root对象根开始，获取属性值
	 * @param root 值开始根
	 * @param keyPaths 属性路径
	 * @return 结果值
	 */
	public static <T> T getValueByPath(Object root, Object... keyPaths) {
		return getValueByPath(root, 0, keyPaths);
	}

	/**
	 * 设置属性
	 * @param bean 非null
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void setPropValue(Object bean, Object prop, Object value) {
		if (bean instanceof Map) {
			((Map) bean).put(prop, value);
			return;
		}
		
		Class<?> clazz = bean.getClass();
		PropertyDescriptor propDesc = BeanUtils.getPropertyDescriptor(clazz, prop.toString());
//		if (propDesc == null) throw new IllegalArgumentException("Property[" + prop + "] not in " + clazz);
		Method setter = propDesc.getWriteMethod();
//		if (method == null) throw new IllegalArgumentException("Property[" + prop + "] setter not in " + clazz);
		if (!Modifier.isPublic(setter.getDeclaringClass().getModifiers())) setter.setAccessible(true);
		try {
			setter.invoke(bean);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new IllegalArgumentException("Property[" + prop + "] setter invoke in " + bean, e);
		}
	}
	/**
	 * @param pos 当前设置路径位置
	 * @param paths 路径key，可以是你所期望的任意对象
	 * @see #setValueByPath(Object, Object, Object...)
	 */
	private static void setValueByPath(Object root, Object value, final int pos, Object[] paths) {
		final Object key = paths[pos];
		if (pos == paths.length - 1) {
			setPropValue(root, key, value);
			return;
		}
		
		Object nextRoot = getPropValue(root, key);
		if (nextRoot == null) {
			nextRoot = new HashMap<>();
			setPropValue(root, key, nextRoot);
		}
		
		setValueByPath(nextRoot, value, pos + 1, paths);
	}
	/**
	 * 设置数据值
	 * @param root 数据根（非空）
	 * @param value 要存储的数据
	 * @param paths 数据的路径（非空）可以是你所期望的任意对象
	 */
	public static void setValueByPath(Object root, Object value, Object... paths) {
		setValueByPath(root, value, 0, paths);
	}

	/** 考虑了null的{@linkplain Object#equals(Object)} */
	public static boolean equals(Object obj1, Object obj2) {
		if (obj1 == obj2) return true;
		if (obj1 == null || obj2 == null) return false;
		return obj1.equals(obj2);
	}
	public static boolean equals(byte[] arr1, byte[] arr2) {
		if (arr1 == arr2) return true;
		if (arr1 == null || arr2 == null || arr1.length != arr2.length) return false;
		for (int i = 0; i < arr1.length; i++) {
			if (arr1[i] != arr2[i]) return false;
		}
		return true;
	}

	/** 对象比较 (null转换为无穷大) */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int compare(Object obj1, Object obj2) {
		if (obj1 == obj2) return 0;
		if (obj1 == null) return 1;
		if (obj2 == null) return -1;
		if (obj1 instanceof Comparable) return ((Comparable) obj1).compareTo(obj2);
		if (obj2 instanceof Comparable) return -((Comparable) obj2).compareTo(obj1);
		return obj1.toString().compareTo(obj2.toString());
	}
	
	public static boolean isTrue(Boolean value) {
		return Boolean.TRUE.equals(value);
	}
}
