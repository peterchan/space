package com.taobao.mc.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * <pre>
 * desc: 
 * created: Mar 21, 2013 10:47:46 AM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */
public class Test {

	public static void main(String[] args) throws IOException {
		// File file = new File("/home/xiangfeng/work/filesearch/client/target/part-00001");
		// List<String> readLines = FileUtils.readLines(file,
		// "GBK");
		//
		// for (String line : readLines) {
		// System.out.println(line);
		//
		// String[] split = line.split("");
		//
		// for (String s : split) {
		// System.out.println(s);
		// }
		// }

		List<String> list = new ArrayList<String>();
		list.add("test");

		System.out.println("1: " + list.get(0));
		List<String> list1 = list;
		list = new ArrayList<String>();
		System.out.println("2: " + list.size());
		System.out.println("2: " + list1.get(0));
	}
}
