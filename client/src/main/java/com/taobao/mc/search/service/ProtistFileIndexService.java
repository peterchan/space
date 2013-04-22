package com.taobao.mc.search.service;

import org.apache.solr.client.solrj.SolrServer;

import com.taobao.mc.search.constants.CoreNames;

/**
 * <pre>
 * desc: 直接使用solrj进行查询
 * created: Mar 5, 2013 2:28:49 PM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */
public interface ProtistFileIndexService {

	/**
	 * 获取读索引服务
	 * 
	 * @param core
	 *            选择索引的core，如果为null，则为默认FILE_INDEX
	 * @see com.taobao.mc.search.constants.CoreNames
	 */
	public SolrServer getReadServer(CoreNames core);

	/**
	 * 获取写索引服务
	 * 
	 * @param core
	 *            选择索引的core，如果为null，则为默认FILE_INDEX
	 * @see @see com.taobao.mc.search.constants.CoreNames
	 */
	public SolrServer getWriteServer(CoreNames core);
}
