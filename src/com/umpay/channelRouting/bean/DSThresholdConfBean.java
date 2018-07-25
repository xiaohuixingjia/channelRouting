package com.umpay.channelRouting.bean;

import java.io.Serializable;

/**
 * 判断数据源不可用阀值配置
 * 
 * @author xuxiaojia
 */
public class DSThresholdConfBean implements Serializable {
	private static final long serialVersionUID = -7645611657740207822L;
	/* 初始阀值--通道切换使用 */
	private int initThreshold;
	/* 异常增加值--通道切换使用 */
	private int errorStep;
	/* 未查得增加值--通道切换使用 */
	private int notGetStep;
	/* 成功减少值--通道切换使用 */
	private int successStep;
	/* 停止阀值--通道切换使用 */
	private int stopThreshold;
	/* 告警阀值--通道切换使用 */
	private int warnThreshold;
	/* 连续未查得最大告警值--连续未查得告警 */
	private int notGetWarnThreshold;

	public int getNotGetWarnThreshold() {
		return notGetWarnThreshold;
	}

	public void setNotGetWarnThreshold(int notGetWarnThreshold) {
		this.notGetWarnThreshold = notGetWarnThreshold;
	}

	public int getInitThreshold() {
		return initThreshold;
	}

	public void setInitThreshold(int initThreshold) {
		this.initThreshold = initThreshold;
	}

	public int getNotGetStep() {
		return notGetStep;
	}

	public void setNotGetStep(int notGetStep) {
		this.notGetStep = notGetStep;
	}

	public int getStopThreshold() {
		return stopThreshold;
	}

	public void setStopThreshold(int stopThreshold) {
		this.stopThreshold = stopThreshold;
	}

	public int getWarnThreshold() {
		return warnThreshold;
	}

	public void setWarnThreshold(int warnThreshold) {
		this.warnThreshold = warnThreshold;
	}

	public DSThresholdConfBean() {
		super();
	}

	public int getErrorStep() {
		return errorStep;
	}

	public void setErrorStep(int errorStep) {
		this.errorStep = errorStep;
	}

	public int getSuccessStep() {
		return successStep;
	}

	public void setSuccessStep(int successStep) {
		this.successStep = successStep;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + errorStep;
		result = prime * result + initThreshold;
		result = prime * result + stopThreshold;
		result = prime * result + successStep;
		result = prime * result + warnThreshold;
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
		DSThresholdConfBean other = (DSThresholdConfBean) obj;
		if (errorStep != other.errorStep)
			return false;
		if (initThreshold != other.initThreshold)
			return false;
		if (stopThreshold != other.stopThreshold)
			return false;
		if (successStep != other.successStep)
			return false;
		if (warnThreshold != other.warnThreshold)
			return false;
		return true;
	}

}
