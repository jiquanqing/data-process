package com.qjq.data.process.util.tree;

import static com.qjq.data.process.util.UtilObj.getPropValue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 树形数据结构帮助类
 * @since 0.1.0
 */
public class UtilTree {
	
	/**
	 * 构造符合js需要的树结构
	 * @param elements 对象集合
	 * @param pidField 父id字段
	 * @param idField id字段
	 * @param textField 显示名字段
	 * @return 
	 * @return <pre>{
	 * 	'父节点id' : {
	 * 		'子节点id': '子节点显示名称'
	 * 	}
	 * }</pre>
	 */
	public static <T> Map<Object, Map<Object, String>> toTreeForUI(Collection<T> elements, String pidField, String idField, String textField) {
		Map<Object, Map<Object, String>> result = new HashMap<>();
		for (T ele : elements) {
			Object pid = getPropValue(ele, pidField), id = getPropValue(ele, pidField);
			String text = getPropValue(ele, textField);
			Map<Object, String> children = result.get(pid);
			if (children == null) {
				children = new HashMap<>();
				result.put(pid, children);
			}
			children.put(id, text);
		}
		return result;
	}
	
}
