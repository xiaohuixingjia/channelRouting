package com.umpay.channelRouting.exception;

import com.umpay.channelRouting.po.ChannelRoutingPO;

/**
 * 无数据源异常
 * 
 * @author xuxiaojia
 */
public class NoDataSourceException extends ChannelBaseException {
	private static final long serialVersionUID = 5562660088765535900L;

	private static final String ERROR_MSG = "没有找到对应的数据源";
	/* 通道信息 */
	private ChannelRoutingPO channelRoutingPO;

	public NoDataSourceException(ChannelRoutingPO channelRoutingPO) {
		super(ERROR_MSG + " 对应key信息 " + channelRoutingPO.keyString());
		this.channelRoutingPO = channelRoutingPO;
	}

	public ChannelRoutingPO getChannelRoutingPO() {
		return channelRoutingPO;
	}

}
