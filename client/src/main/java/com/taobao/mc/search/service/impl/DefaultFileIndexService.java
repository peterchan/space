package com.taobao.mc.search.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.mc.search.constants.CoreNames;
import com.taobao.mc.search.model.BaseModel;
import com.taobao.mc.search.param.IndexQuery;
import com.taobao.mc.search.param.IndexQuery.Order;
import com.taobao.mc.search.param.QueryIndexResult;
import com.taobao.mc.search.param.WriteIndexResult;
import com.taobao.mc.search.service.FileIndexService;
import com.taobao.mc.search.service.helper.AppendHelper;

/**
 * <pre>
 * desc: 
 * created: Mar 5, 2013 2:53:37 PM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */
public class DefaultFileIndexService extends AbstractProtistFileIndexService implements FileIndexService {
	private static final Logger logger = LoggerFactory.getLogger(DefaultFileIndexService.class);
	private static final int BATCH_APPEND_COUNT = 10000;

	@Override
	public WriteIndexResult writeIndex(List docs, CoreNames names) {

		WriteIndexResult result = validateParam(docs, names, BATCH_APPEND_COUNT);
		if (result != null) {
			return result;
		}

		SolrServer writeServer = super.getWriteServer(names);
		result = new WriteIndexResult();
		UpdateResponse addBeans = null;
		try {
			addBeans = writeServer.addBeans(docs);
			writeServer.commit();
		} catch (SolrServerException e) {
			logger.error("Write file info docs exception.", e);
			result.setErrMsg(e.getMessage());
		} catch (IOException e) {
			logger.error("Write file info docs exception.", e);
			result.setErrMsg(e.getMessage());
		}

		if (addBeans != null) {
			result.setSuccess(addBeans.getStatus() == 0 || addBeans.getStatus() == 200);
			result.setqTime(addBeans.getQTime());
		}

		return result;
	}

	@Override
	public QueryIndexResult queryIndex(IndexQuery query, CoreNames names) {

		if (names == null) {
			return new QueryIndexResult("CoreNames can't be empty");
		}

		return query(query, super.getReadServer(names));
	}

	private QueryIndexResult query(IndexQuery indexQuery, SolrServer solrServer) {
		SolrQuery query = new SolrQuery();
		query.setQuery(StringUtils.isBlank(indexQuery.getqStr()) ? "*:*" : indexQuery.getqStr());

		if (StringUtils.isNotBlank(indexQuery.getFq())) {
			query.addFilterQuery(indexQuery.getFq());
		}

		query.setIncludeScore(indexQuery.isNeedCore());
		query.setStart(indexQuery.getStart());
		query.setRows(indexQuery.getRownum() > 1000 ? 1000 : indexQuery.getRownum());

		if (StringUtils.isNotBlank(indexQuery.getSort())) {
			query.addSortField(indexQuery.getSort(), indexQuery.getOrder() == Order.asc ? SolrQuery.ORDER.asc
					: SolrQuery.ORDER.desc);
		} else {
			// query.addSortField("Score", SolrQuery.ORDER.desc);
			// query.addSortField("id", SolrQuery.ORDER.asc);
		}

		long currentTimeMillis = System.currentTimeMillis();
		QueryResponse queryRsp = null;
		try {
			queryRsp = solrServer.query(query);
		} catch (SolrServerException e) {
			logger.error("query docs exception.", e);
		}

		long currentTimeMillis2 = System.currentTimeMillis();

		logger.error("Query Time-consuming" + (currentTimeMillis2 - currentTimeMillis));
		System.out.println("time: " + (currentTimeMillis2 - currentTimeMillis));

		QueryIndexResult result = new QueryIndexResult();

		if (queryRsp != null && queryRsp.getResults() != null && queryRsp.getResults().size() > 0) {
			Iterator<SolrDocument> iterator = queryRsp.getResults().iterator();
			result.setNumFound(queryRsp.getResults().getNumFound());
			result.setStart(queryRsp.getResults().getStart());
			result.setMaxScore(queryRsp.getResults().getMaxScore());
			result.setStatus(queryRsp.getStatus());
			result.setqTime(result.getqTime());
			result.setSuccess(true);

			List<Map<String, Collection<Object>>> docs = new LinkedList<Map<String, Collection<Object>>>();
			while (iterator.hasNext()) {
				SolrDocument next = iterator.next();
				if (next != null) {
					docs.add(next.getFieldValuesMap());
				}
			}

			result.setDocs(docs);
		} else {
			result.setNumFound(0);
			result.setErrMsg("Response is empty.");
			result.setStart(indexQuery.getStart());
		}

		return result;
	}

	@Override
	public <T extends BaseModel> WriteIndexResult append(List<T> docs, CoreNames names, AppendHelper<T> helper) {
		WriteIndexResult result = validateParam(docs, names, BATCH_APPEND_COUNT);
		if (result != null) {
			return result;
		}

		if (helper == null) {
			return new WriteIndexResult(400, "AppendHelper can't be null.");
		}

		List<T> writeDoc = new ArrayList<T>(docs.size());

		// 记录当前批次循环的index
		int loopIndex = 0;
		// 记录剩余的数量
		int leftCount = docs.size();
		int batchAppendCount = leftCount > BATCH_APPEND_COUNT ? BATCH_APPEND_COUNT : leftCount;

		StringBuilder queryStr = new StringBuilder();
		for (T t : docs) {
			leftCount--;
			loopIndex++;
			if (t != null) {
				String id = t.getId();
				// id为空忽略
				if (StringUtils.isBlank(id)) {
					continue;
				}

				if (loopIndex == batchAppendCount || leftCount == 0) {
					queryStr.append("id:" + id);
					IndexQuery query = new IndexQuery();
					query.setqStr(queryStr.toString());
					query.setRownum(batchAppendCount);
					QueryIndexResult queryIndex = this.queryIndex(query, names);
					if (queryIndex.isSuccess() && queryIndex.getNumFound() > 0) {
						List<Map<String, Collection<Object>>> tempDoc = queryIndex.getDocs();
						for (Map<String, Collection<Object>> tMap : tempDoc) {
							writeDoc.add(helper.append(t, tempDoc.get(0)));
						}
					}
					// 清空
					queryStr.delete(0, queryStr.length());
					loopIndex = 0;
				} else {
					queryStr.append("id:" + id + " OR ");
				}
			}
		}

		return this.writeIndex(writeDoc, names);
	}

	private WriteIndexResult validateParam(List docs, CoreNames names, int maxLength) {
		if (docs == null || docs.size() == 0) {
			return new WriteIndexResult(400, "docs can't be empty");
		}

		if (docs.size() > maxLength) {
			return new WriteIndexResult(400, "docs is over " + maxLength);
		}

		if (names == null) {
			return new WriteIndexResult(400, "CoreNames can't be empty");
		}

		return null;
	}
}
