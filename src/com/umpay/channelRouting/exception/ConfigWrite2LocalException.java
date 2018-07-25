package com.umpay.channelRouting.exception;
/**
 * 写配置信息到本地异常
* @author xuxiaojia
 */
public class ConfigWrite2LocalException extends Exception{

	private static final long serialVersionUID = -8764525906980360938L;
	
	private String errorMsg;

	public ConfigWrite2LocalException(String errorMsg) {
		super(errorMsg);
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}


}
