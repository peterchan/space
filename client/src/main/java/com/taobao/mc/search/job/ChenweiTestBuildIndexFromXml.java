package com.taobao.mc.search.job;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.common.SolrInputDocument;

/**
 * @author peter.chenw
 * @version 创建时间：2013-4-16 上午10:50:41 类说明
 */
public class ChenweiTestBuildIndexFromXml {

	public static void main(String[] args) // throws Exception {
	{
		try {
			HttpSolrServer server = new HttpSolrServer(
					"http://localhost:8080/search-web/core4/");
			server.setParser(new XMLResponseParser());
			SolrInputDocument doc1 = new SolrInputDocument();
			doc1.addField("id", "TEST-000005");
			doc1.addField("Catalogs_title", "TEST-000001");
			doc1.addField("OcrText", "陈威测试下SOLR的XML文件");
			SolrInputDocument doc2 = new SolrInputDocument();
			doc2.addField("id", "TEST-000004");
			doc2.addField("Catalogs_title", "TEST 000004");
			doc2.addField(
					"OcrText",
					"solrj 被设计成一个可扩展的框架，用以向solr服务器提交请求，并接收回应。 我们已经将最通用的一些命令封装在了solrServer类中了。 Adding Data to Solr 首先需要获得一个server的实例，");
			SolrInputDocument doc3 = new SolrInputDocument();
			doc3.addField("id", "TEST000006");
			doc3.addField("Catalogs_title", "TEST 000006");
			doc3.addField("OcrText", "建立好solr的索引后，可以通过管理界面进行查询。");

			Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
			docs.add(doc1);
			docs.add(doc2);
			docs.add(doc3);
			server.add(docs);
			server.commit();
			System.out.println("Finish!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
