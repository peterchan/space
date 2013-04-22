package com.taobao.mc.search.service;

import org.apache.solr.client.solrj.SolrServer;

import com.taobao.mc.search.constants.CoreNames;

/**
 * <pre>
 * desc: ֱ��ʹ��solrj���в�ѯ
 * created: Mar 5, 2013 2:28:49 PM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */
public interface ProtistFileIndexService {

	/**
	 * ��ȡ����������
	 * 
	 * @param core
	 *            ѡ��������core�����Ϊnull����ΪĬ��FILE_INDEX
	 * @see com.taobao.mc.search.constants.CoreNames
	 */
	public SolrServer getReadServer(CoreNames core);

	/**
	 * ��ȡд��������
	 * 
	 * @param core
	 *            ѡ��������core�����Ϊnull����ΪĬ��FILE_INDEX
	 * @see @see com.taobao.mc.search.constants.CoreNames
	 */
	public SolrServer getWriteServer(CoreNames core);
}
