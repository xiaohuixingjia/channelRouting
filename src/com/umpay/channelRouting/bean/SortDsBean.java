package com.umpay.channelRouting.bean;

import java.io.Serializable;

/**
 * 对数据源进行排序配置的bean对象
 * 
 * @author xuxiaojia
 */
public class SortDsBean implements Comparable<SortDsBean>, Serializable {

	private static final long serialVersionUID = 8162815602329541736L;
	/* 排名 */
	private int sort;
	/* 数据源ID */
	private String dsId;
	/* 权重 */
	private int weight;
	/* 是否启用下一个通道 */
	private boolean useNext;

	@Override
	public int compareTo(SortDsBean o) {
		return this.sort - o.getSort();
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getDsId() {
		return dsId;
	}

	public void setDsId(String dsId) {
		this.dsId = dsId;
	}

	public SortDsBean(int sort, String dsId, int weight, boolean useNext) {
		this.sort = sort;
		this.dsId = dsId;
		this.weight = weight;
		this.useNext = useNext;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public SortDsBean() {
		super();
	}

	/**
	 * 排序累加
	 * 
	 * @return 返回相加后的结果值
	 */
	public int sortAdd() {
		return this.sort += 10;
	}

	public boolean canUseNext() {
		return useNext;
	}

	public void setUseNext(boolean useNext) {
		this.useNext = useNext;
	}

	@Override
	public String toString() {
		return "SortDsBean [sort=" + sort + ", dsId=" + dsId + ", weight=" + weight + ", useNext=" + useNext + "]";
	}

}
