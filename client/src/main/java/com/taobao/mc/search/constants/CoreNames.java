package com.taobao.mc.search.constants;

/**
 * <pre>
 * desc: 
 * created: Mar 5, 2013 2:32:42 PM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */
public enum CoreNames {
	/**
	 * default
	 */
	FILE_INDEX("core1"),
	FILE_FEATURE("core2"),
	FILE_FEATURES("core3");

	private String coreName;

	private CoreNames(String coreName) {
		this.coreName = coreName;
	}

	public String getCoreName() {
		return coreName;
	}

}
