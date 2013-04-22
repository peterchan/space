package com.taobao.mc.search.model;

import org.apache.solr.client.solrj.beans.Field;

public class Feature extends BaseModel implements java.io.Serializable {
	private static final long serialVersionUID = -4472989995031793544L;

	@Field
	private Long picId;

	@Field
	private Float[] content;
	@Field
	private Long centerId = -1L;

	public Feature() {
	}

	public Feature(Long id, Long picId, Long centerId, Float[] content) {
		this.picId = picId;
		this.id = id.toString();

		this.centerId = centerId;
		this.content = content;
	}

	public Feature(Long id, Long picId, Float[] content) {
		this.picId = picId;
		this.id = id.toString();
		this.content = content;
	}

	public int getDimension() {
		return this.content.length;
	}

	public Long getPicId() {
		return picId;
	}

	public void setPicId(Long picId) {
		this.picId = picId;
	}

	public Float[] getContent() {
		return content;
	}

	public void setContent(Float[] content) {
		this.content = content;
	}

	public Long getCenterId() {
		return centerId;
	}

	public void setCenterId(Long centerId) {
		this.centerId = centerId;
	}

	public void setId(String id) {
		this.id = id;
	}

}