package com.taobao.mc.search.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.taobao.mc.search.constants.CoreNames;

/**
 * <pre>
 * desc: 
 * created: 2013-2-17 下午03:41:31
 * author: xiaofeng.zhouxf
 * todo: 
 * history:
 * </pre>
 */
class ServerManage {

	private static Map<CoreNames, HttpSolrServer> httpServerCoreMap;
	private static Map<CoreNames, ConcurrentUpdateSolrServer> ConcurrentUpdateSolrServerMap;

	private static final String SERVER_URL = "http://localhost:8080/search-web/";
	private static final String CONFIG_NAME = "/fileSearchConf.properties";
	private static final String DEFAULT_CONFIG_FILE = "/default_search_server.properties";
	private static final String DEFAULT_CONFIG_NAME = "filesearch.server.url";

	private static final String HTTP_PATH_SEPARATOR = "/";

	private static String serverUrl = SERVER_URL;

	static {

		InputStream resourceAsStream = ServerManage.class.getResourceAsStream(CONFIG_NAME);
		if (resourceAsStream == null) {
			resourceAsStream = ServerManage.class.getResourceAsStream(DEFAULT_CONFIG_FILE);
		}
		if (resourceAsStream != null) {
			Properties prop = new Properties();
			try {
				prop.load(resourceAsStream);
				serverUrl = prop.getProperty(DEFAULT_CONFIG_NAME);
			} catch (IOException e) {
			}
		}

		serverUrl = (serverUrl.lastIndexOf(HTTP_PATH_SEPARATOR) != -1 ? serverUrl : serverUrl + HTTP_PATH_SEPARATOR);

		httpServerCoreMap = new ConcurrentHashMap<CoreNames, HttpSolrServer>();
		ConcurrentUpdateSolrServerMap = new ConcurrentHashMap<CoreNames, ConcurrentUpdateSolrServer>();
	}

	/**
	 * 用于读
	 * 
	 * @param core
	 * @return
	 */
	public static HttpSolrServer getHttpServer(CoreNames core) {
		if (core == null) {
			return null;
		}

		if (httpServerCoreMap.containsKey(core)) {
			return httpServerCoreMap.get(core);
		}
		httpServerCoreMap.put(core, new HttpSolrServer(serverUrl + core.getCoreName()));
		return httpServerCoreMap.get(core);
	}

	/**
	 * 用于写,实现中带有线程池
	 * 
	 * @param core
	 * @return
	 */
	public static SolrServer getConCurrentServer(CoreNames core) {
		if (core == null) {
			return null;
		}

		if (ConcurrentUpdateSolrServerMap.containsKey(core)) {
			return ConcurrentUpdateSolrServerMap.get(core);
		}
		ConcurrentUpdateSolrServerMap
				.put(core, new ConcurrentUpdateSolrServer(serverUrl + core.getCoreName(), 1000, 3));
		return ConcurrentUpdateSolrServerMap.get(core);

	}

	public static void main(String args[]) {
		new ServerManage();
	}
}
