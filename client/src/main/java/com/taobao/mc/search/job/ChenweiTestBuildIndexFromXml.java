package com.taobao.mc.search.job;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.common.SolrInputDocument;

/**
 * @author peter.chenw
 * @version ����ʱ�䣺2013-4-16 ����10:50:41 ��˵��
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
			doc1.addField("OcrText", "����������SOLR��XML�ļ�");
			SolrInputDocument doc2 = new SolrInputDocument();
			doc2.addField("id", "TEST-000004");
			doc2.addField("Catalogs_title", "TEST 000004");
			doc2.addField(
					"OcrText",
					"solrj ����Ƴ�һ������չ�Ŀ�ܣ�������solr�������ύ���󣬲����ջ�Ӧ�� �����Ѿ�����ͨ�õ�һЩ�����װ����solrServer�����ˡ� Adding Data to Solr ������Ҫ���һ��server��ʵ����");
			SolrInputDocument doc3 = new SolrInputDocument();
			doc3.addField("id", "TEST000006");
			doc3.addField("Catalogs_title", "TEST 000006");
			doc3.addField("OcrText", "������solr�������󣬿���ͨ�����������в�ѯ��");

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
