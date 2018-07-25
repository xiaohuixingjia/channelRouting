package com.umpay.log.proxy;

import java.util.HashMap;
import java.util.Map;

import com.umpay.log.util.ConfigParams;

/**
 * 代理日志工厂 将记录的日志记录文件的同时发送到一个消息队列中
 * 
 * @author xuxiaojia
 */
public class ProxyLogFactory {
	private static Map<String, ProxyLogger> logMap = new HashMap<String, ProxyLogger>();

	public static ProxyLogger getLogger(String name) {
		return getLogger(name, ConfigParams.useConsumeLog());
	}

	public static ProxyLogger getLogger(String name, boolean unifyLog) {
		if (!logMap.containsKey(name)) {
			createLog2map(name, unifyLog);
		}
		return logMap.get(name);
	}

	private synchronized static void createLog2map(String name, boolean unifyLog) {
		if (logMap.containsKey(name)) {
			return;
		}
		if (unifyLog) {
			// 消费到本地和队列
			logMap.put(name, new LocalAndQueueLogger(name));
		} else {
			// 消费到本地
			logMap.put(name, new LocalLogger(name));
		}
	}
}
