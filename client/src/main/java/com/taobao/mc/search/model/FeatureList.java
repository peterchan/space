package com.taobao.mc.search.model;

import org.apache.solr.client.solrj.beans.Field;

public class FeatureList extends BaseModel implements java.io.Serializable {
	private static final long serialVersionUID = -4472989995031793544L;

	@Field
	private String[] fid;

	public FeatureList() {
	}

	public String[] getFid() {
		return fid;
	}

	public void setFid(String[] fid) {
		this.fid = fid;
	}

}