package com.taobao.mc.search.job;

import java.io.IOException;
import java.util.Date;

import org.apache.solr.client.solrj.SolrServerException;

/**
 * <pre>
 * desc: 
 * created: Mar 6, 2013 10:55:06 AM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */
public class BuildIndexFromFileTaskTest {
	private static final String yunduan_filePath = "/media/xiangfeng/SF/F_disk/tmp_xiangfeng_20130225_limit_100W.csv";
	private static final String yunti_filePath = "/media/xiangfeng/SF/out"; // /media/xiangfeng/SF/out

	public static void testYunDuanBuildIndex() throws IOException, SolrServerException, InterruptedException {
		long s1 = System.currentTimeMillis();
		BuildIndexFromFileTask task = new BuildIndexFromFileTask();
		boolean buildIndex = task.buildIndex(yunduan_filePath, 10000, 0);
		// 需要等待一会.....
		// Thread.sleep(10000);

		long s2 = System.currentTimeMillis();

		System.out.println("执行时间: " + (s2 - s1) / 1000);
		// assertTrue(buildIndex);
	}

	public static void testYunTiBuildIndex() throws IOException, SolrServerException, InterruptedException {
		long s1 = System.currentTimeMillis();
		BuildIndexFromFileTask task = new BuildIndexFromFileTask();
		boolean buildIndex = task.buildIndex(yunti_filePath, 1000000, 1);
		// 需要等待一会.....
		// Thread.sleep(10000);

		long s2 = System.currentTimeMillis();

		System.out.println("执行时间: " + buildIndex + (s2 - s1) / 1000);
		// assertTrue(buildIndex);
	}

	public static void main(String[] args) throws IOException, SolrServerException, InterruptedException {
		BuildIndexFromFileTaskTest.testYunTiBuildIndex();
	}
}
