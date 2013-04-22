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
 * desc: 文件搜索服务包装
 * created: Mar 5, 2013 1:26:05 PM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */

public interface FileIndexService {

	/**
	 * 写索引文件，最多1w个每次
	 * 
	 * @param docs
	 * @return
	 */
	public WriteIndexResult writeIndex(List docs, CoreNames names);

	/**
	 * 查询图片特征
	 * 
	 * @param query
	 * @return
	 */
	public QueryIndexResult queryIndex(IndexQuery query, CoreNames names);

	/**
	 * 根据docs的Id对某些字段的内容进行追加,如果记录不存在，则新增记录, 便利记录的操作，性能不好
	 * 
	 * @param docs
	 * @param names
	 * @param helper
	 *            提供自定义append类，用于对所需要的值进行append
	 * 
	 * @return
	 */
	public <T extends BaseModel> WriteIndexResult append(List<T> docs, CoreNames names, AppendHelper<T> helper);

}
