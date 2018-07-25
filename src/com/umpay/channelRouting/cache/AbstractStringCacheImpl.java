package com.umpay.channelRouting.cache;

/**
 * 缓存接口的抽象实现
 * 
 * @author xuxiaojia
 * @param <K>
 * @param <V>
 */
public abstract class AbstractStringCacheImpl  extends AbstractObjCacheImpl<String, String>{

	public AbstractStringCacheImpl(long catchTime, int timeType) {
		super(catchTime, timeType);
	}

}
