package com.taobao.mc.search.utils;

import java.util.Arrays;

/**
 * <pre>
 * desc: 
 * created: Mar 7, 2013 9:59:16 AM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */
public class CommonTools {

	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
}
