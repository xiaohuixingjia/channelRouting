package com.umpay.log.proxy;

import java.util.Date;

import com.umpay.log.bean.LogInfo;
import com.umpay.log.infoQueue.InfoQueue;

/**
 * 消费到本地和队列里
 * 
 * @author xuxiaojia
 */
public class LocalAndQueueLogger extends ProxyLogger {

	public LocalAndQueueLogger(String name) {
		super(name);
	}

	@Override
	public void info(String info) {
		log.info(info);
		InfoQueue.getInstance().offer(new LogInfo(new Date(), info, this.logName));
	}

}
