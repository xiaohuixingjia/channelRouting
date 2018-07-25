package com.umpay.channelRouting.bean;

import java.io.Serializable;

/**
 * 数据源切回配置
 * 
 * @author xuxiaojia
 */
public class CutBackDSConfBean implements Serializable {
	private static final long serialVersionUID = -4950660287427722584L;
	/**
	 * 一秒
	 */
	public static final int ONE_SECOND = 1000;
	/* 从停止访问到默认访问的间隔时间 单位为秒 */
	private Long cutBackTime;
	/* 默认请求报文的存放路径 */
	private String pathOfTestMessage;
	/* 默认请求报文的前缀名称 */
	private String prefixOfTestMessage;
	/* 默认请求报文的尾缀名称 */
	private String suffixOfTestMessage;
	/* 是否启用 */
	private boolean enable;

	/**
	 * 获取配置的延迟时间 单位为毫秒
	 * 
	 * @return
	 */
	public long getDelayTime() {
		return this.cutBackTime * ONE_SECOND;
	}

	public Long getCutBackTime() {
		return cutBackTime;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setCutBackTime(Long cutBackTime) {
		this.cutBackTime = cutBackTime;
	}

	public String getPathOfTestMessage() {
		return pathOfTestMessage;
	}

	public void setPathOfTestMessage(String pathOfTestMessage) {
		this.pathOfTestMessage = pathOfTestMessage;
	}

	public String getPrefixOfTestMessage() {
		return prefixOfTestMessage;
	}

	public void setPrefixOfTestMessage(String prefixOfTestMessage) {
		this.prefixOfTestMessage = prefixOfTestMessage;
	}

	public String getSuffixOfTestMessage() {
		return suffixOfTestMessage;
	}

	public void setSuffixOfTestMessage(String suffixOfTestMessage) {
		this.suffixOfTestMessage = suffixOfTestMessage;
	}

	public CutBackDSConfBean() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cutBackTime == null) ? 0 : cutBackTime.hashCode());
		result = prime * result + (enable ? 1231 : 1237);
		result = prime * result + ((pathOfTestMessage == null) ? 0 : pathOfTestMessage.hashCode());
		result = prime * result + ((prefixOfTestMessage == null) ? 0 : prefixOfTestMessage.hashCode());
		result = prime * result + ((suffixOfTestMessage == null) ? 0 : suffixOfTestMessage.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CutBackDSConfBean other = (CutBackDSConfBean) obj;
		if (cutBackTime == null) {
			if (other.cutBackTime != null)
				return false;
		} else if (!cutBackTime.equals(other.cutBackTime))
			return false;
		if (enable != other.enable)
			return false;
		if (pathOfTestMessage == null) {
			if (other.pathOfTestMessage != null)
				return false;
		} else if (!pathOfTestMessage.equals(other.pathOfTestMessage))
			return false;
		if (prefixOfTestMessage == null) {
			if (other.prefixOfTestMessage != null)
				return false;
		} else if (!prefixOfTestMessage.equals(other.prefixOfTestMessage))
			return false;
		if (suffixOfTestMessage == null) {
			if (other.suffixOfTestMessage != null)
				return false;
		} else if (!suffixOfTestMessage.equals(other.suffixOfTestMessage))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CutBackDSConfBean [cutBackTime=" + cutBackTime + ", pathOfTestMessage=" + pathOfTestMessage
				+ ", prefixOfTestMessage=" + prefixOfTestMessage + ", suffixOfTestMessage=" + suffixOfTestMessage
				+ ", isEnable=" + enable + "]";
	}

}
