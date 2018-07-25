package com.umpay.log.bean;

import java.util.Date;

/**
 * 日志信息
 * 
 * @author xuxiaojia
 */
public class LogInfo {
	/**
	 * 邮箱的 @ 分隔符
	 */
	public static final String SEPARATOR = "@";
	/* 日志生成的系统时间 */
	private Date date;
	/* 日志信息 */
	private String info;
	/* 日志名称 */
	private String logName;
	public LogInfo() {
		super();
	}

	public LogInfo(Date date, String info, String logName) {
		super();
		this.date = date;
		this.info = info;
		this.logName = logName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getLogName() {
		return logName;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

	/**
	 * 简要信息 日志文件名称@日志信息
	 * 
	 * @return
	 */
	public String toSimpleStr() {
		return this.logName + SEPARATOR + this.info;
	}
}
