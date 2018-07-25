package com.umpay.channelRouting.reload;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.umpay.channelRouting.center.service.impl.ChannelInfo;

/**
 * 重载线程
 * 
 * @author xuxiaojia
 */
public class ReloadService {
	private static final Logger log = LoggerFactory.getLogger(ReloadService.class);
	private static final String START_TIME_INFO = " 当前时间  ";
	private static final String RELOAD_INFO = " 开始重载  ";
	private static final String RELOAD_OK = " 重载配置信息成功  ";
	private static final String RELOAD_ERROR = " 重载出错  ";
	private static final String RELOAD_TIME_CONSUMING = " 重载耗时  ";
	private static final String IS_RELOAD = "正在重载通道路由配置信息到内存";
	// 是否正在重载标识
	private AtomicBoolean isReload = new AtomicBoolean(false);
	/* 需要重载的对象 */
	private ChannelInfo channelInfo;

	public void setChannelInfo(ChannelInfo channelInfo) {
		this.channelInfo = channelInfo;
	}

	public String reload() {
		// 如果正在重载
		if (isReload.getAndSet(true)) {
			return IS_RELOAD;
		}
		try {
			long startTime = System.currentTimeMillis();
			log.info(START_TIME_INFO + new Date().toString() + RELOAD_INFO);
			channelInfo.reload();
			long endTime = System.currentTimeMillis();
			log.info(RELOAD_OK + RELOAD_TIME_CONSUMING + (endTime - startTime));
			// 重载完成，将重载标识改为未重载
			isReload.set(false);
			return RELOAD_OK + RELOAD_TIME_CONSUMING + (endTime - startTime);
		} catch (Exception e) {
			// TODO 重载出现异常则不更改isReload标识，阻止重新重载，让其访问备份通道，等待开发人员查看异常信息
			log.error(RELOAD_ERROR, e);
			return RELOAD_ERROR + e.getMessage();
		} 
		
	}

}
