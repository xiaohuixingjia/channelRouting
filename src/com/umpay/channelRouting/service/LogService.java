package com.umpay.channelRouting.service;

import java.util.Map;

/**
 * wangwei
 */

public interface LogService {

	/**
	 * 打印hbase超时日志
	 */
	public void printHbaseTimeOut(long time, String tableName,
			String queryCriteria);

	/**
	 * 初始化日志前缀
	 * @param xmlMap
	 */
	public void init(Map<String, String> xmlMap);

	/**
	 * 获取日志前缀
	 * @return
	 */
	public String getLogPrefix();

	/**
	 * 获取Merid值
	 * @return String
	 */
	public String getMerid();

	/**
	 * 获取Funcode值
	 * @return String
	 */
	public String getFuncode();
	
	/**
	 * 获取手机号
	 * @return String
	 */
	public String getMobileid();	
	
	/**
	 * 设置当前线程的查得标志
	 * @param flag
	 */
	public void setIsFoundFlag(String flag);
	
	/**
	 * 获取当前线程的查得标志
	 * @return
	 */
	public String getIsFound();
}
