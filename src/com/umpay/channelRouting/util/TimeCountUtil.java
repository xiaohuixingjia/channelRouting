package com.umpay.channelRouting.util;

/**
 * 计算相应时间的常用工具类
 * 
 * @author xuxiaojia
 */
public class TimeCountUtil {
	/* 记录起始时间的线程变量 */
	private static final ThreadLocal<Long> startTime = new ThreadLocal<Long>();

	/**
	 * 设置起始时间
	 */
	public static void setStartTime() {
		startTime.set(System.currentTimeMillis());
	}

	/**
	 * 获取现在时间对应起始时间的耗时
	 * 
	 * @return
	 */
	public static long getTimeConsuming() {
		if (startTime.get() != null) {
			return System.currentTimeMillis() - startTime.get();
		} else {
			return 0L;
		}
	}

	/**
	 * 获取当前时间与传入时间的耗时
	 * 
	 * @param startTime2
	 * @return
	 */
	public static long getTimeConsuming(long startTime2) {
		return System.currentTimeMillis() - startTime2;
	}
}
