package com.qjq.data.process.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 存储时使用json的数据模型封装，访问使用{@linkplain HashMap}
 * 请不要设置datas = null
 * @since 0.1.0
 */
public class MapWrap implements Serializable {
	private static final long serialVersionUID = 1L;
	private Map<String, Object> datas;
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + datas;
	}
	public Map<String, Object> getDatas() {
		return datas;
	}
	public void setDatas(Map<String, Object> datas) {
		this.datas = datas;
	}
	
	
	public MapWrap() {
		datas = new LinkedHashMap<>();
	}
	public MapWrap(Map<String, Object> datas) {
		this.datas = datas;
	}
	
	
	/**
	 * 专供存储协议
	 * @return json存储格式
	 */
	public String getData0() {
		return datas.isEmpty() ? null : UtilJson.writeValueAsString(datas);
	}
	/**
	 * 专供存储协议
	 * @param json 从存储中读取的数据
	 */
	public void setData0(String json) {
		Map<String, Object> tmp = new HashMap<>();
		if (json != null) {
			json = json.trim();
			if (json.startsWith("{") && json.endsWith("}")) {
				tmp = UtilJson.toMap(json);
			} else if (!json.isEmpty()) {
				tmp.put("_value", json);
			}
		}
		datas.clear();
		datas.putAll(tmp);
	}
	
	public boolean isEmpty() {
		return datas.isEmpty();
	}
	public int size() {
		return datas.size();
	}
	public MapWrap put(String key, Object value) {
		datas.put(key, value);
		return this;
	}
	public MapWrap putValueByPath(Object value, Object... keyPaths) {
		UtilObj.setValueByPath(datas, value, keyPaths);
		return this;
	}
	public <T> T get(Object... keyPaths) {
		return UtilObj.getValueByPath(datas, keyPaths);
	}
	public Long getLong(Object... keyPaths) {
		return UtilObj.toLong(get(keyPaths));
	}
	public Integer getInteger(Object... keyPaths) {
		return UtilObj.toInteger(get(keyPaths));
	}
	public void putAll(Map<String, Object> map) {
		datas.putAll(map);
	}
	public void remove(String key) {
		datas.remove(key);
	}
	
	
	@SuppressWarnings({ "unchecked" })
	public static MapWrap toMapWrap(Object value) {
		if (value == null) return null;
		if (value instanceof MapWrap) return (MapWrap) value;
		if (value instanceof Map) return new MapWrap((Map<String, Object>) value);
		return new MapWrap(UtilJson.toMap(value));
	}
}
