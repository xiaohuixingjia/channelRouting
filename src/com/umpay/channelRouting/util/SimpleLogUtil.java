package com.umpay.channelRouting.util;

import java.util.List;

import org.slf4j.LoggerFactory;

public class SimpleLogUtil {

	private List<String> logs;

	public void setLogs(List<String> logs) {
		this.logs = logs;
	}


	public void printTimerLog() {
		String str = "  ";
		for (String string : logs) {
			LoggerFactory.getLogger(string).info(str);
		}
	}
}
