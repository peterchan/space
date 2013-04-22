package com.taobao.mc.search.service;

import java.util.List;

import com.taobao.mc.search.constants.CoreNames;
import com.taobao.mc.search.model.BaseModel;
import com.taobao.mc.search.param.IndexQuery;
import com.taobao.mc.search.param.QueryIndexResult;
import com.taobao.mc.search.param.WriteIndexResult;
import com.taobao.mc.search.service.helper.AppendHelper;

/**
 * <pre>
 * desc: �ļ����������װ
 * created: Mar 5, 2013 1:26:05 PM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */

public interface FileIndexService {

	/**
	 * д�����ļ������1w��ÿ��
	 * 
	 * @param docs
	 * @return
	 */
	public WriteIndexResult writeIndex(List docs, CoreNames names);

	/**
	 * ��ѯͼƬ����
	 * 
	 * @param query
	 * @return
	 */
	public QueryIndexResult queryIndex(IndexQuery query, CoreNames names);

	/**
	 * ����docs��Id��ĳЩ�ֶε����ݽ���׷��,�����¼�����ڣ���������¼, ������¼�Ĳ��������ܲ���
	 * 
	 * @param docs
	 * @param names
	 * @param helper
	 *            �ṩ�Զ���append�࣬���ڶ�����Ҫ��ֵ����append
	 * 
	 * @return
	 */
	public <T extends BaseModel> WriteIndexResult append(List<T> docs, CoreNames names, AppendHelper<T> helper);

}
