package com.taobao.mc.search.service.impl;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;

import com.taobao.mc.search.constants.CoreNames;
import com.taobao.mc.search.model.Feature;
import com.taobao.mc.search.model.FeatureList;
import com.taobao.mc.search.model.FileInfo;
import com.taobao.mc.search.param.IndexQuery;
import com.taobao.mc.search.param.QueryIndexResult;
import com.taobao.mc.search.param.WriteIndexResult;
import com.taobao.mc.search.service.helper.FeatureListAppendHelper;

/**
 * <pre>
 * desc: 
 * created: Mar 5, 2013 5:00:39 PM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */
public class DefaultFileIndexServiceTest {
	private static final String filePath = "/media/xiangfeng/SF/F_disk/tmp_xiangfeng_20130225_limit_100W.csv";

	DefaultFileIndexService service = new DefaultFileIndexService();

	@Test
	public void testWriteFileInfo() throws IOException, SolrServerException, InterruptedException {
		List<FileInfo> docs = new ArrayList<FileInfo>(1);
		FileInfo info = new FileInfo();
		info.setId("4441_test");
		info.setFid(222);
		info.setDirId(111);
		info.setFeature("12312312");
		info.setUrl("123123");
		info.setBizId(111);
		info.setType(0);
		info.setUserId(12312);
		info.setName("15test");
		info.setFileModified(new Date());
		info.setGmtCreate(new Date());
		info.setGmtModified(new Date());

		docs.add(info);

		WriteIndexResult writeFileInfo = service.writeIndex(docs, CoreNames.FILE_INDEX);
		System.out.println(ToStringBuilder.reflectionToString(writeFileInfo));
		assertTrue(writeFileInfo.isSuccess());
	}

	@Test
	public void testWritePicFeature() {

		List<Feature> docs = new ArrayList<Feature>(1);

		Feature f = new Feature();
		f.setCenterId(11111l);
		f.setContent(new Float[] { 11.1f, 22.2f, 33.3f });
		f.setId("1111l");
		f.setPicId(111111l);
		docs.add(f);

		WriteIndexResult writePicFeature = service.writeIndex(docs, CoreNames.FILE_FEATURE);
		System.out.println(ToStringBuilder.reflectionToString(writePicFeature));
		assertTrue(writePicFeature.isSuccess());
	}

	@Test
	public void testWritePicFeatures() {

		List<FeatureList> docs = new ArrayList<FeatureList>(1);

		FeatureList f = new FeatureList();
		f.setId("1231232");
		f.setFid(new String[] { "11", "22" });
		docs.add(f);

		WriteIndexResult writePicFeature = service.writeIndex(docs, CoreNames.FILE_FEATURES);
		System.out.println(ToStringBuilder.reflectionToString(writePicFeature));
		assertTrue(writePicFeature.isSuccess());
	}

	@Test
	public void testQueryFileInfo() {
		IndexQuery query = new IndexQuery();
		query.setRownum(10);
		QueryIndexResult queryFileInfo = service.queryIndex(query, CoreNames.FILE_INDEX);
		// System.out.print(queryFileInfo.getStatus());
		// System.out.print(queryFileInfo.getNumFound());
		assertTrue(queryFileInfo.getNumFound() > 0);
	}

	@Test
	public void testQueryPicFeature() {
		IndexQuery query = new IndexQuery();
		query.setRownum(10);
		QueryIndexResult queryFileInfo = service.queryIndex(query, CoreNames.FILE_FEATURE);
		// System.out.print(queryFileInfo.getStatus());
		// System.out.print(queryFileInfo.getNumFound());
		assertTrue(queryFileInfo.getNumFound() > 0);
	}

	@Test
	public void testQueryPicFeatures() {
		IndexQuery query = new IndexQuery();
		query.setRownum(10);
		QueryIndexResult queryFileInfo = service.queryIndex(query, CoreNames.FILE_FEATURES);
		// System.out.print(queryFileInfo.getStatus());
		// System.out.print(queryFileInfo.getNumFound());
		assertTrue(queryFileInfo.getNumFound() > 0);
	}

	@Test
	public void testAppend() {

		List<FeatureList> docs = new ArrayList<FeatureList>(1);
		FeatureList f = new FeatureList();
		f.setId("1231232");
		f.setFid(new String[] { "77", "88" });
		docs.add(f);

		WriteIndexResult writePicFeature = service.append(docs, CoreNames.FILE_FEATURES,
				new FeatureListAppendHelper());
		System.out.println(ToStringBuilder.reflectionToString(writePicFeature));
		assertTrue(writePicFeature.isSuccess());
	}
}
