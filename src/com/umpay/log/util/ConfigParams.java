package com.umpay.log.util;

public class ConfigParams{
	
	public static PropertiesUtil propertiesReader = PropertiesUtil.getInstance("config.properties");

	
	public static String getProp(String key){
		return propertiesReader.getConfigItem(key);
	}
	
	/**
	 * 是否消费日志到kafka
	 * @return
	 */
	public static boolean useConsumeLog(){
		return "true".equals(getProp("useConsumeLog"));
	}
}

