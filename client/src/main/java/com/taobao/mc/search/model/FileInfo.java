package com.taobao.mc.search.model;

import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

/**
 * <pre>
 * desc: 
 * created: 2013-2-26 ÉÏÎç10:01:49
 * author: xiaofeng.zhouxf
 * todo: 
 * history:
 * </pre>
 */
public class FileInfo extends BaseModel implements java.io.Serializable {
	private static final long serialVersionUID = -4756512899756006981L;

	@Field
	private long fid;
	@Field
	private String url;
	@Field
	private long bizId;
	@Field
	private long userId;
	@Field
	private String name;
	private int status;
	@Field
	private long dirId;
	@Field
	private long size;
	@Field
	private int type;
	@Field
	private Date fileModified;
	private int deleted;
	@Field
	private Date gmtCreate;
	@Field
	private Date gmtModified;
	@Field
	private String feature;
	@Field
	private String md5;
	@Field
	private String tag;

	public long getFid() {
		return fid;
	}

	public void setFid(long fid) {
		this.id = String.valueOf(fid);
		this.fid = fid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getBizId() {
		return bizId;
	}

	public void setBizId(long bizId) {
		this.bizId = bizId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getDirId() {
		return dirId;
	}

	public void setDirId(long dirId) {
		this.dirId = dirId;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public Date getFileModified() {
		return fileModified;
	}

	public void setFileModified(Date fileModified) {
		this.fileModified = fileModified;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
