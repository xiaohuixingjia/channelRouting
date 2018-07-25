package com.umpay.channelRouting.visit;

import java.util.Map;

import com.umpay.channelRouting.center.ConfigDataSource;

/**
 * 访问并获取结果信息接口定义
 * @author xuxiaojia
 */
public interface Visit4getResu {
	/**
	 * 访问配置数据源获取结果
	 * @param xmlMap  商户访问的xml信息
	 * @param configDataSource  访问的数据源信息
	 * @return
	 */
	public String getResuFromDs(Map<String, String> xmlMap,ConfigDataSource configDataSource);
}
