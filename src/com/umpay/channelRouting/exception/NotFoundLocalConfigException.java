package com.umpay.channelRouting.exception;

/**
 * 未找到本地文件配置异常
 * 
 * @author xuxiaojia
 */
public class NotFoundLocalConfigException extends Exception {

	private static final long serialVersionUID = 4227497090169976209L;
	private String errorMsg;

	public NotFoundLocalConfigException(String errorMsg) {
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
