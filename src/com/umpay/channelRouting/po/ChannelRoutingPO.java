package com.umpay.channelRouting.po;

import java.io.Serializable;

/**
 * 产品核查信息表
 * 
 * @author xuxiaojia
 */
public class ChannelRoutingPO implements Serializable {
	private static final long serialVersionUID = -7765262598574608064L;
	/**
	 * 运营商 未识别
	 */
	public static final int OPERATOR_TYPE_UNRECOGNIZED = 0;
	/**
	 * 运营商 移动
	 */
	public static final int OPERATOR_TYPE_YIDONG = 1;
	/**
	 * 运营商 联通
	 */
	public static final int OPERATOR_TYPE_LIANTONG = 2;
	/**
	 * 运营商 电信
	 */
	public static final int OPERATOR_TYPE_DIANXIN = 3;
	/**
	 * 当前通道未查得时可以用下一个通道
	 */
	public static final int CHANNEL_CAN_USE_NEXT = 2;
	/**
	 * 当前通道未查得时不可以用下一个通道
	 */
	public static final int CHANNEL_CAN_NOT_USE_NEXT = 4;
	/* 产品包编码 */
	private String productSuitesCode;
	/* 商户编码 */
	private String merCode;
	/* 运营商类型 */
	private Integer operatorType;
	/* 数据源ID */
	private String dsId;
	/* 排名 */
	private Integer sort;
	/* 权重 */
	private Integer weight;
	/* 是否使用下一个通道 */
	private Integer useNext;

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public ChannelRoutingPO() {
		super();
	}

	public String getProductSuitesCode() {
		return productSuitesCode;
	}

	public void setProductSuitesCode(String productSuitesCode) {
		this.productSuitesCode = productSuitesCode;
	}

	public String getMerCode() {
		return merCode;
	}

	public void setMerCode(String merCode) {
		this.merCode = merCode;
	}

	public Integer getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(Integer operatorType) {
		this.operatorType = operatorType;
	}

	public String getDsId() {
		return dsId;
	}

	public void setDsId(String dsId) {
		this.dsId = dsId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getUseNext() {
		return useNext;
	}

	@Override
	public String toString() {
		return "ChannelRoutingPO [productSuitesCode=" + productSuitesCode + ", merCode=" + merCode + ", operatorType="
				+ operatorType + ", dsId=" + dsId + ", sort=" + sort + ", weight=" + weight + ", useNext=" + useNext
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dsId == null) ? 0 : dsId.hashCode());
		result = prime * result + ((merCode == null) ? 0 : merCode.hashCode());
		result = prime * result + ((operatorType == null) ? 0 : operatorType.hashCode());
		result = prime * result + ((productSuitesCode == null) ? 0 : productSuitesCode.hashCode());
		result = prime * result + ((sort == null) ? 0 : sort.hashCode());
		result = prime * result + ((useNext == null) ? 0 : useNext.hashCode());
		result = prime * result + ((weight == null) ? 0 : weight.hashCode());
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
		ChannelRoutingPO other = (ChannelRoutingPO) obj;
		if (dsId == null) {
			if (other.dsId != null)
				return false;
		} else if (!dsId.equals(other.dsId))
			return false;
		if (merCode == null) {
			if (other.merCode != null)
				return false;
		} else if (!merCode.equals(other.merCode))
			return false;
		if (operatorType == null) {
			if (other.operatorType != null)
				return false;
		} else if (!operatorType.equals(other.operatorType))
			return false;
		if (productSuitesCode == null) {
			if (other.productSuitesCode != null)
				return false;
		} else if (!productSuitesCode.equals(other.productSuitesCode))
			return false;
		if (sort == null) {
			if (other.sort != null)
				return false;
		} else if (!sort.equals(other.sort))
			return false;
		if (useNext == null) {
			if (other.useNext != null)
				return false;
		} else if (!useNext.equals(other.useNext))
			return false;
		if (weight == null) {
			if (other.weight != null)
				return false;
		} else if (!weight.equals(other.weight))
			return false;
		return true;
	}

	public void setUseNext(Integer useNext) {
		this.useNext = useNext;
	}

	public String keyString() {
		return " " + productSuitesCode + "," + merCode + "," + operatorType;
	}

}
