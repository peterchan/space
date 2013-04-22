package com.taobao.mc.search.param;

import java.io.Serializable;

/**
 * <pre>
 * desc: 
 * created: Mar 5, 2013 2:04:15 PM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */
public class WriteIndexResult implements Serializable {

	private static final long serialVersionUID = 3773084997179229830L;

	private boolean isSuccess;
	private int qTime;
	private String errMsg;
	private int code;

	public WriteIndexResult() {
		super();
	}

	public WriteIndexResult(int code, String errMsg) {
		super();
		this.code = code;
		this.errMsg = errMsg;
	}

	public WriteIndexResult(boolean isSuccess, int qTime, String errMsg, int code) {
		super();
		this.isSuccess = isSuccess;
		this.qTime = qTime;
		this.errMsg = errMsg;
		this.code = code;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
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

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
