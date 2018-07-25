package com.umpay.channelRouting.exception;

/**
 * 通道路由的自定义父类异常
 * 
 * @author xuxiaojia
 */
public class ChannelBaseException extends Exception {

	private static final long serialVersionUID = -6630049572235763902L;

	private String errorMsg;

	public ChannelBaseException(String errorMsg) {
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
