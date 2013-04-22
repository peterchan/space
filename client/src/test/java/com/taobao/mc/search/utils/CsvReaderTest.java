package com.taobao.mc.search.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

/**
 * <pre>
 * desc: 
 * created: Mar 25, 2013 2:42:52 PM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */
public class CsvReaderTest {
	private final static String path = "/media/xiangfeng/SF/part-01999";

	@Test
	public void testCsvReaderStringChar() throws IOException {
		CsvReader reader = new CsvReader(path, '\t', Charset.forName("UTF-8"));
		reader.setSafetySwitch(false);
		Set<String> ids = new HashSet<String>();
		int i = 0;
		while (reader.readRecord()) {
			// System.out.println(Arrays.toString(reader.getValues()));

			String[] values = reader.getValues();
			// System.out.println(values[0]);
			if (!ids.add(values[1])) {
				System.out.println("duplicate id: " + values[1]);
			}

			i++;
		}
		System.out.println(i + " :: " + ids.size());
	}
}
