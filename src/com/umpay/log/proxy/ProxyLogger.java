package com.umpay.log.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 代理日志
 * 
 * @author xuxiaojia
 */
public abstract class ProxyLogger {
	protected Logger log;
	protected String logName;
	public ProxyLogger(String name) {
		this.log = LoggerFactory.getLogger(name);
		this.logName=name;
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public abstract void info(String info);

	public void debug(String info) {
		log.debug(info);
	}
	public void error(String info) {
		log.error(info);
	}

}
