package com.umpay.channelRouting.center.service;

import java.util.List;

import com.umpay.channelRouting.center.WeightConfDSlist;
import com.umpay.channelRouting.exception.ChannelBaseException;
import com.umpay.channelRouting.po.ChannelRoutingPO;
/**
 * 获取通道信息的抽象接口
 * @author xuxiaojia
 */
public interface GetChannelInfoService {

	/**
	 * 根据通道路由信息对象获取可以访问的数据源集合
	 * 
	 * @param key
	 * @return
	 */
	public List<WeightConfDSlist> getDataSource(ChannelRoutingPO channelRouting) throws ChannelBaseException;

}
