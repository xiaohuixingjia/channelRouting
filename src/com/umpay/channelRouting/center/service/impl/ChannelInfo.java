package com.umpay.channelRouting.center.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.umpay.channelRouting.center.AsynTimerPool;
import com.umpay.channelRouting.center.WeightConfDSlist;
import com.umpay.channelRouting.center.service.GetChannelInfoService;
import com.umpay.channelRouting.exception.ChannelBaseException;
import com.umpay.channelRouting.po.ChannelRoutingPO;
import com.umpay.channelRouting.service.impl.LogServiceImpl;

/**
 * 获取通道信息的核心类
 * 
 * @author xuxiaojia
 */
public class ChannelInfo implements GetChannelInfoService, Serializable {
	private static final long serialVersionUID = -5732631512394227788L;
	private static final Logger log = LoggerFactory.getLogger(ChannelInfo.class);
	/* 实时的通道信息bean */
	private ChannelMapInfo ruleTimemapInfo;
	/* 重载信息时的备用通道信息bean */
	private ChannelMapInfo reserveMapInfo;

	public ChannelMapInfo getRuleTimemapInfo() {
		return ruleTimemapInfo;
	}

	public void setRuleTimemapInfo(ChannelMapInfo ruleTimemapInfo) {
		this.ruleTimemapInfo = ruleTimemapInfo;
	}

	/*
	 * 用于判断是否读取ruleTimemapInfo的线程安全的对象 true 代表读取ruleTimemapInfo
	 * false则读取reserveMapInfo
	 */
	private AtomicBoolean canReadRuntime = new AtomicBoolean(true);

	/**
	 * 线程休息 mills毫秒
	 * 
	 * @param mills
	 */
	private void sleep(long mills) {
		try {
			Thread.sleep(mills);
		} catch (InterruptedException e) {
			log.error(LogServiceImpl.getInstance().getLogPrefix(), e);
		}
	}

	/**
	 * 重载数据库信息到内存
	 * 
	 * @throws Exception
	 */
	public void reload() throws Exception {
		// 备份当前通道信息
		reserveMapInfo = ruleTimemapInfo.cloneOneSlef();
		// 设置当前通道信息为不可读
		canReadRuntime.set(false);
		// 休息一秒让所有现有的请求线程走完对 ruleTimemapInfo 的使用
		sleep(1000);
		// 重新加载数据库中的配置
		ruleTimemapInfo.init();
		// 设置当前通道信息为可读
		canReadRuntime.set(true);
		// 删除异步访问数据源池中内存中存在的但是数据库中不存在的（防止数据库物理删除数据源）
		rmUnnecessaryDs();
		// 休息一秒让所有现有的请求线程走完对 reserveMapInfo 的使用
		sleep(1000);
	}

	/**
	 * 删除多余的异步访问的数据源线程
	 */
	private void rmUnnecessaryDs() {
		AsynTimerPool instance = AsynTimerPool.getInstance();
		Set<String> ruleTimeDsIds = ruleTimemapInfo.getAllDsId();
		Set<String> timerTaskDsIds = instance.getAllTimerTask4dsId();
		for (String id : timerTaskDsIds) {
			if (!ruleTimeDsIds.contains(id)) {
				instance.removeAndStopAsynTimer(id);
			}
		}
	}

	@Override
	public List<WeightConfDSlist> getDataSource(ChannelRoutingPO channelRouting) throws ChannelBaseException {
		if (canReadRuntime.get()) {
			log.info(LogServiceImpl.getInstance().getLogPrefix()+"访问实时通道");
			return ruleTimemapInfo.getDataSource(channelRouting);
		} else {
			log.info(LogServiceImpl.getInstance().getLogPrefix()+"访问备份通道");
			return reserveMapInfo.getDataSource(channelRouting);
		}
	}

}
