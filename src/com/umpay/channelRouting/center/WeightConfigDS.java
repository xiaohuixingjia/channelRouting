package com.umpay.channelRouting.center;

import com.umpay.channelRouting.bean.SortDsBean;

public class WeightConfigDS {
	private SortDsBean sortDsBean;
	private ConfigDataSource configDataSource;
	public SortDsBean getSortDsBean() {
		return sortDsBean;
	}
	public void setSortDsBean(SortDsBean sortDsBean) {
		this.sortDsBean = sortDsBean;
	}
	public ConfigDataSource getConfigDataSource() {
		return configDataSource;
	}
	public void setConfigDataSource(ConfigDataSource configDataSource) {
		this.configDataSource = configDataSource;
	}
	@Override
	public String toString() {
		return "WeightConfigDS [sortDsBean=" + sortDsBean + ", configDataSource=" + configDataSource + "]";
	}
	public int getWeight() {
		return this.sortDsBean.getWeight();
	}
	public WeightConfigDS(SortDsBean sortDsBean, ConfigDataSource configDataSource) {
		super();
		this.sortDsBean = sortDsBean;
		this.configDataSource = configDataSource;
	}
	
	
}
