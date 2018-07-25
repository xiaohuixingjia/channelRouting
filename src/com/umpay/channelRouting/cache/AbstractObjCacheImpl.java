package com.umpay.channelRouting.cache;

import java.util.concurrent.TimeUnit;

public abstract class AbstractObjCacheImpl<K, V> implements CacheInterface<K, V> {
	/**
	 * 秒类型
	 */
	public static final int SECONDS_TYPE=1;
	/**
	 * 分类型
	 */
	public static final int MINUTES_TYPE=3;
	/**
	 * 小时类型
	 */
	public static final int HOURS_TYPE=5;
	/**
	 * 天类型
	 */
	public static final int DAYS_TYPE=7;
	/*
	 * 缓存时间
	 */
	private long catchTime;
	/*
	 * 缓存时间类型
	 */
	private int timeType;

	
	public AbstractObjCacheImpl(long catchTime, int timeType) {
		super();
		this.catchTime = catchTime;
		this.timeType = timeType;
	}

	/**
	 * 根据时间类型获取timeUnit枚举
	 * 
	 * @param type
	 *            事件类型
	 * @return
	 */
	public TimeUnit getTimeUnitByTimeType(int type) {
		switch (type) {
		case SECONDS_TYPE:
			return TimeUnit.SECONDS;
		case MINUTES_TYPE:
			return TimeUnit.MINUTES;
		case HOURS_TYPE:
			return TimeUnit.HOURS;
		case DAYS_TYPE:
			return TimeUnit.DAYS;
		default:
			return TimeUnit.SECONDS;
		}
	}

	@Override
	public boolean set(K k, V v) {
		return setValue(k, v, catchTime, getTimeUnitByTimeType(timeType));
	}

	@Override
	public V get(K k) {
		return getValue(k);
	}

	@Override
	public boolean set(K k, V v, long catchTime, TimeUnit timeUnit) {
		return setValue(k, v, catchTime, timeUnit);
	}

	/**
	 * 子类需要抽象实现的set方法
	 * 
	 * @param k
	 * @param v
	 * @return
	 */
	protected abstract boolean setValue(K k, V v, long catchTime, TimeUnit timeUnit);

	/**
	 * 子类需要抽象实现的get方法
	 * 
	 * @param k
	 * @return
	 */
	protected abstract V getValue(K k);
}
