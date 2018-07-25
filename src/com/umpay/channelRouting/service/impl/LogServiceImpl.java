package com.umpay.channelRouting.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.umpay.channelRouting.proxyservice.Constant;
import com.umpay.channelRouting.service.LogService;
import com.umpay.channelRouting.util.HttpMap;

public class LogServiceImpl implements LogService{
	private static LogService instance = null;
	private LogServiceImpl(){}
	public static LogService getInstance(){
		if(instance == null)instance = new LogServiceImpl();
		return instance;
	}
	
	private ThreadLocal<String> logPrefix = new ThreadLocal<String>();
	private ThreadLocal<String> funcode = new ThreadLocal<String>();
	private ThreadLocal<String> merid = new ThreadLocal<String>();
	private ThreadLocal<String> mobileid = new ThreadLocal<String>();
	/* 报文查得标志 */
	private ThreadLocal<String> isFound = new ThreadLocal<String>();
	
	
	private static Logger hbasetimeout_simple_log = LoggerFactory.getLogger("hbasetimeout_simple");	
	
	@Override
	public void printHbaseTimeOut(long time, String tableName,String queryCriteria) {
		hbasetimeout_simple_log.info(logPrefix.get()+","+tableName+","+queryCriteria+","+time+"ms");
	}
	
	@Override
	public void init(Map<String, String> xmlMap) {
		String transid = xmlMap.get(HttpMap.TRANSID);
		merid .set( xmlMap.get(HttpMap.MERID));
		funcode.set(xmlMap.get(HttpMap.FUNCODE));
		mobileid.set(xmlMap.get(HttpMap.MOBILEID));
		logPrefix.set(transid+","+merid.get()+","+funcode.get());
		//默认未查得
		isFound.set(Constant.NOT_FOUND);
	}
	
	@Override
	public String getLogPrefix() {
		if(this.logPrefix.get() == null)return "";
		return "["+this.logPrefix.get()+"]";
	}
	
	@Override
	public String getFuncode() {
		return funcode.get();
	}
	
	@Override
	public String getMerid() {
		return merid.get();
	}
	@Override
	public String getMobileid() {
		return mobileid.get();
	}
	/**
	 * 设置当前线程的查得标志
	 * @param flag
	 */
	public void setIsFoundFlag(String flag){
		this.isFound.set(flag);
	}
	
	/**
	 * 获取当前线程的查得标志
	 * @return
	 */
	public String getIsFound(){
		return this.isFound.get();
	}
	
}
