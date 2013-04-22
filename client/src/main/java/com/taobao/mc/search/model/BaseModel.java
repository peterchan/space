package com.taobao.mc.search.model;

import org.apache.solr.client.solrj.beans.Field;

/**
 * <pre>
 * desc: 
 * created: Mar 6, 2013 5:04:31 PM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */
public abstract class BaseModel {

	@Field
	protected String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
