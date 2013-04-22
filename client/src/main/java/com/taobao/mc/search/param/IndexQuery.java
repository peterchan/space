package com.taobao.mc.search.param;

import java.io.Serializable;

/**
 * <pre>
 * desc: 查询索引
 * created: Mar 5, 2013 2:18:58 PM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */
public class IndexQuery implements Serializable {
	private static final long serialVersionUID = -8826839640735629114L;

	/**
	 * 查询条件
	 */
	private String qStr;

	/**
	 * 过滤查询条件
	 */
	private String fq;

	/**
	 * 查询记录开始位置
	 */
	private int start;

	/**
	 * 返回多个条记录
	 */
	private int rownum;

	/**
	 * 是否需要返回评分
	 */
	private boolean needCore;

	/**
	 * 排序信息，例如：sum(if(exists(TS_1),product(abs(sub(TS_1,300)),0.3),0),if(exists(TS_11),product(abs(sub(TS_11,300)),0.3),
	 * 0))
	 */
	private String sort;

	/**
	 * 排序方式
	 */
	private Order order = Order.desc;

	public String getqStr() {
		return qStr;
	}

	/**
	 * 查询条件
	 * 
	 * @param qStr
	 */
	public void setqStr(String qStr) {
		this.qStr = qStr;
	}

	public String getFq() {
		return fq;
	}

	/**
	 * 过滤查询条件
	 * 
	 * @param fq
	 */
	public void setFq(String fq) {
		this.fq = fq;
	}

	public int getStart() {
		return start;
	}

	/**
	 * 查询记录开始位置,用于翻页
	 * 
	 * @param start
	 */
	public void setStart(int start) {
		this.start = start;
	}

	public int getRownum() {
		return rownum;
	}

	/**
	 * 返回多个条记录
	 * 
	 * @param rownum
	 */
	public void setRownum(int rownum) {
		this.rownum = rownum;
	}

	public boolean isNeedCore() {
		return needCore;
	}

	/**
	 * 是否需要返回评分,设置为true，可以在返回结果中得到评分
	 * 
	 * @param needCore
	 */
	public void setNeedCore(boolean needCore) {
		this.needCore = needCore;
	}

	public String getSort() {
		return sort;
	}

	/**
	 * 排序信息，例如：sum(if(exists(TS_1),product(abs(sub(TS_1,300)),0.3),0),if(exists(TS_11),product(abs(sub(TS_11,300)),0.3),
	 * 0))
	 * 
	 * @param sort
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}

	public Order getOrder() {
		return order;
	}

	/**
	 * 排序方式,asc or desc
	 * 
	 * @param order
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	// ===============================
	public enum Order {
		desc, asc;
	}

}
