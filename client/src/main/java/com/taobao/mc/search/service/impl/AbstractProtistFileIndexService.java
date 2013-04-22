package com.taobao.mc.search.service.impl;

import org.apache.solr.client.solrj.SolrServer;

import com.taobao.mc.search.constants.CoreNames;
import com.taobao.mc.search.service.ProtistFileIndexService;

/**
 * <pre>
 * desc: 
 * created: Mar 5, 2013 3:04:56 PM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */
public abstract class AbstractProtistFileIndexService implements ProtistFileIndexService {

	@Override
	public SolrServer getReadServer(CoreNames core) {
		return ServerManage.getHttpServer(core);
	}

	@Override
	public SolrServer getWriteServer(CoreNames core) {
		return ServerManage.getConCurrentServer(core);
	}

}
