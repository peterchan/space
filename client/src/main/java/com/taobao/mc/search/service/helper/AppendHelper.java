package com.taobao.mc.search.service.helper;

import java.util.Collection;
import java.util.Map;

/**
 * <pre>
 * desc: 
 * created: Mar 6, 2013 5:01:23 PM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */
public interface AppendHelper<T> {

	/**
	 * 自定义append
	 * 
	 * @param newT
	 *            新增的对象
	 * @param map
	 *            包含字段名词与值的对应关系
	 * @return
	 */
	public T append(T newT, Map<String, Collection<Object>> map);
}
