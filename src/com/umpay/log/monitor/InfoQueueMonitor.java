package com.umpay.log.monitor;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.umpay.log.infoQueue.InfoQueue;
import com.umpay.log.util.ConfigParams;
import com.umpay.log.util.DateUtil;

/**
 * 日志队列监控
 * 
 * @author xuxiaojia
 */
public class InfoQueueMonitor {
	private final static Logger log = LoggerFactory.getLogger("InfoQueueMonitor");
	public static AtomicLong consumeNum = new AtomicLong(0);
	private static Date curDate = new Date();
	public void execute() {
		//再确认了使用消费日志到kafka的情况下，启动队列监控
		if(ConfigParams.useConsumeLog()){
			long freeMemory = Runtime.getRuntime().freeMemory();
			long fmMb = freeMemory / 1024 / 1024;
			log.info("当前剩余内存" + fmMb + "MB,日志待消费队列长度:" + InfoQueue.getInstance().size()+" 当天已生产：" + consumeNum.get());
			if (DateUtil.isNotSameDay(curDate, new Date())) {
				log.info("当天总共消费："+consumeNum.getAndSet(0));
				curDate = new Date();
			}
		}
	}
}
