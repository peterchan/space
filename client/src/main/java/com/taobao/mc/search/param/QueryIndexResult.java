package com.taobao.mc.search.param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * desc: 
 * created: Mar 5, 2013 2:06:02 PM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */
public class QueryIndexResult implements Serializable {

	private static final long serialVersionUID = 8037451720501945053L;

	/**
	 * map key为索引名词,评分字段名:score
	 * 
	 * @see com.taobao.mc.search.constants.FileIndexColumn
	 */
	private List<Map<String, Collection<Object>>> docs;

	/**
	 * 400
	 */
	private int status;

	private long numFound = 0;

	private long start = 0;

	private Float maxScore = null;

	private int qTime;

	private String errMsg;

	private boolean isSuccess;

	public QueryIndexResult(String errMsg) {
		super();
		this.errMsg = errMsg;
	}

	public QueryIndexResult(List<Map<String, Collection<Object>>> docs, int status, long numFound, long start,
			Float maxScore, int qTime, String errMsg, boolean isSuccess) {
		super();
		this.docs = docs;
		this.status = status;
		this.numFound = numFound;
		this.start = start;
		this.maxScore = maxScore;
		this.qTime = qTime;
		this.errMsg = errMsg;
		this.isSuccess = isSuccess;
	}

	public QueryIndexResult() {
		super();
	}

	/**
	 * @see com.taobao.mc.search.constants.FileIndexColumn
	 * @return
	 */
	public List<Map<String, Collection<Object>>> getDocs() {
		return docs;
	}

	public void setDocs(List<Map<String, Collection<Object>>> docs) {
		this.docs = docs;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getNumFound() {
		return numFound;
	}

	public void setNumFound(long numFound) {
		this.numFound = numFound;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public Float getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(Float maxScore) {
		this.maxScore = maxScore;
	}

	public int getqTime() {
		return qTime;
	}

	public void setqTime(int qTime) {
		this.qTime = qTime;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

}
