package com.taobao.mc.search.param;

import java.io.Serializable;

/**
 * <pre>
 * desc: ��ѯ����
 * created: Mar 5, 2013 2:18:58 PM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */
public class IndexQuery implements Serializable {
	private static final long serialVersionUID = -8826839640735629114L;

	/**
	 * ��ѯ����
	 */
	private String qStr;

	/**
	 * ���˲�ѯ����
	 */
	private String fq;

	/**
	 * ��ѯ��¼��ʼλ��
	 */
	private int start;

	/**
	 * ���ض������¼
	 */
	private int rownum;

	/**
	 * �Ƿ���Ҫ��������
	 */
	private boolean needCore;

	/**
	 * ������Ϣ�����磺sum(if(exists(TS_1),product(abs(sub(TS_1,300)),0.3),0),if(exists(TS_11),product(abs(sub(TS_11,300)),0.3),
	 * 0))
	 */
	private String sort;

	/**
	 * ����ʽ
	 */
	private Order order = Order.desc;

	public String getqStr() {
		return qStr;
	}

	/**
	 * ��ѯ����
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
	 * ���˲�ѯ����
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
	 * ��ѯ��¼��ʼλ��,���ڷ�ҳ
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
	 * ���ض������¼
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
	 * �Ƿ���Ҫ��������,����Ϊtrue�������ڷ��ؽ���еõ�����
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
	 * ������Ϣ�����磺sum(if(exists(TS_1),product(abs(sub(TS_1,300)),0.3),0),if(exists(TS_11),product(abs(sub(TS_11,300)),0.3),
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
	 * ����ʽ,asc or desc
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
