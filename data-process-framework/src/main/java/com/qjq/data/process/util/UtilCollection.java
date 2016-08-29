package com.qjq.data.process.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * 集合相关工具类
 * @since 0.1.0
 */
public class UtilCollection {
	
	public static boolean notEmpty(Collection<?> coll) {
		return coll != null && !coll.isEmpty();
	}
	public static boolean isEmpty(Collection<?> coll) {
		return coll == null || coll.isEmpty();
	}
	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}
	
	public static Map<String, String> toMap(Properties props) {
		Map<String, String> result = new HashMap<>();
		for (Entry<Object, Object> entry : props.entrySet()) {
			result.put((String) entry.getKey(), (String) entry.getValue());
		}
		return result;
	}
	/**
	 * 命令行参数转化为map的key=value
	 * @param args 无=的value自动为null
	 */
	public static Map<String, String> toMap(String... args) {
		Map<String, String> map = new HashMap<>(args.length);
		for (String arg : args) {
			String name = arg, value = null;
			int pos = arg.indexOf('=');
			if (pos != -1) {
				name = arg.substring(0, pos);
				value = arg.substring(pos + 1);
			}
			map.put(name, value);
		}
		return map;
	}
	/**
	 * 命令行参数转化为map的key=value
	 * @param args 无=的value自动为null
	 */
	public static List<String[]> toList(String... args) {
		List<String[]> list = new ArrayList<>(args.length);
		for (String arg : args) {
			String name = arg, value = null;
			int pos = arg.indexOf('=');
			if (pos != -1) {
				name = arg.substring(0, pos);
				value = arg.substring(pos + 1);
			}
			list.add(new String[] {name, value});
		}
		return list;
	}
	
	/** 删除map中value==null的entry */
	public static <K, V> Map<K, V> trim(Map<K, V> map) {
		for (Iterator<Entry<K, V>> iter = map.entrySet().iterator(); iter.hasNext();) {
			Entry<K, V> entry = iter.next();
			if (entry.getValue() == null) iter.remove();
		}
		return map;
	}
	/** 删除map中value==null的entry或者空集合 */
	@SuppressWarnings("rawtypes")
	public static <K, V> Map<K, V> trimIncludeEmptyCollect(Map<K, V> map) {
		for (Iterator<Entry<K, V>> iter = map.entrySet().iterator(); iter.hasNext();) {
			Entry<K, V> entry = iter.next();
			V value = entry.getValue();
			if (value == null) {
				iter.remove();
			} else if (value instanceof Collection) {
				if (((Collection) value).isEmpty()) iter.remove();
			} else if (value instanceof Map) {
				if (((Map) value).isEmpty()) iter.remove();
			}
		}
		return map;
	}

	public static <K, V> Long getLong(Map<K, V> map, K key) {
		return map == null ? null : UtilObj.toLong(map.get(key));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Integer incInt(Map map, Object key, int offset) {
		Integer value = (Integer) map.get(key);
		value = value == null ? offset : value + offset;
		map.put(key, value);
		return value;
	}

	public static void removeAll(Collection<?> coll, Collection<?> excludes) {
		if (excludes == null || excludes.isEmpty()) return;
		if (!(excludes instanceof Set)) excludes = new HashSet<>(excludes);
		
		for (Iterator<?> iter = coll.iterator(); iter.hasNext();) {
			if (excludes.contains(iter.next())) iter.remove();
		}
	}
	public static <T> T get(List<? extends T> list, int index) {
		return list != null && index < list.size() ? list.get(index) : null;
	}
	public static <T> T get(T[] array, int index) {
		return array != null && index < array.length ? array[index] : null;
	}
	/**
	 * 添加value到List中
	 * @param root 根对象
	 * @param value 添加的值
	 * @param clazz 集合不存在时使用的实例化类
	 * @param keyPaths list所在的路径
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addIntoCollection(Map root, Object value, Class<? extends Collection> clazz, Object... keyPaths) {
		Collection collection = UtilObj.getValueByPath(root, keyPaths);
		if (collection == null) {
			try {
				collection = clazz == null ? new ArrayList<>(): clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new IllegalArgumentException(e);
			}
			UtilObj.setValueByPath(root, collection, keyPaths);
		}
		collection.add(value);
	}
	/**
	 * 添加value到List中
	 * @param root 根对象
	 * @param value 添加的值
	 * @param clazz list不存在时使用的实例化类
	 * @param keyPaths list所在的路径
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addIntoList(Map root, Object value, Class<? extends List> clazz, Object... keyPaths) {
		List list = UtilObj.getValueByPath(root, keyPaths);
		if (list == null) {
			try {
				list = clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new IllegalArgumentException(e);
			}
			UtilObj.setValueByPath(root, list, keyPaths);
		}
		list.add(value);
	}
	
	/**
	 * 子集
	 * @param list 集合
	 * @param fromIndex 开始位置(include)
	 * @param toIndex 结束位置（exclude），如果超过集合大小自动切换到末尾
	 * @see List#subList(int, int) 
	 */
	public static <T> List<T> subList(List<T> list, int fromIndex, int toIndex) {
		if (list == null) return list;
		if (fromIndex == 0 && toIndex >= list.size()) return list;
		if (toIndex > list.size()) toIndex = list.size();
		return list.subList(fromIndex, toIndex);
	}
	
	@SafeVarargs
	public static <T> List<T> asList(T... array) {
		return new ArrayList<>(Arrays.asList(array));
	}
}
