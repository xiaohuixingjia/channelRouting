package com.umpay.log.proxy;
/**
 * 只消费到本地
* @author xuxiaojia
 */
public class LocalLogger extends ProxyLogger{

	public LocalLogger(String name) {
		super(name);
	}

	@Override
	public void info(String info) {
		log.info(info);
	}

}
