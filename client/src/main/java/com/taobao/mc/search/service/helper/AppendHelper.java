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
	 * �Զ���append
	 * 
	 * @param newT
	 *            �����Ķ���
	 * @param map
	 *            �����ֶ�������ֵ�Ķ�Ӧ��ϵ
	 * @return
	 */
	public T append(T newT, Map<String, Collection<Object>> map);
}
