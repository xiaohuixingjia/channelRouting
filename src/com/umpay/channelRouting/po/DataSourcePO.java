package com.umpay.channelRouting.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.umpay.channelRouting.bean.HbaseInfoBean;
import com.umpay.channelRouting.proxyservice.Constant;

/**
 * 数据源表
 * 
 * @author xuxiaojia
 */
public class DataSourcePO implements Serializable {
	private static final long serialVersionUID = 7401545519282507362L;
	/**
	 * 数据源状态为可用
	 */
	public static final int DS_CAN_USE_2_STRING = 2;
	/**
	 * 数据源状态为不可用
	 */
	public static final int DS_CAN_NOT_USE_2_STRING = 4;
	/**
	 * 当前数据源可用
	 */
	public static final boolean DS_CAN_USE_2_BOOLEAN = true;
	/**
	 * 当前数据源不可用
	 */
	public static final boolean DS_CAN_NOT_USE_2_BOOLEAN = false;
	/* 数据源ID */
	private String id;
	/* 数据源名称 */
	private String name;
	/* 数据源路径 */
	private String url;
	/* 当前数据源状态 */
	private Integer state;
	/* 供应商ID */
	private String supplierId;
	/* 是否使用habse 2使用，4不使用 */
	private Integer hbaseState;
	/* 扫描的表 */
	private String hbaseTable;
	/* 请求hbase时的queryType */
	private String hbaseResult;
	/* rowKey值集合 逗号分隔 */
	private String keyNames;
	/* value值加密次数 */
	private Integer encryptTime;
	/* 是否先查缓存 */
	private Integer cacheState;
	/* 返回结果不入缓存的配置 */
	private String noCacheResu;
	/* 不入缓存的结果集 */
	private List<String> noCacheResuList;
	/* rowkey取值的的key标签集合 */
	private List<String> keyElems;
	/* hbase的查询信息 */
	private HbaseInfoBean hbaseInfo;

	public Integer getCacheState() {
		return cacheState;
	}

	public void setCacheState(Integer cacheState) {
		this.cacheState = cacheState;
	}

	public String getNoCacheResu() {
		return noCacheResu;
	}

	public void setNoCacheResu(String noCacheResu) {
		this.noCacheResu = noCacheResu;
	}

	public List<String> getNoCacheResuList() {
		return noCacheResuList;
	}

	public void setNoCacheResuList(List<String> noCacheResuList) {
		this.noCacheResuList = noCacheResuList;
	}

	public HbaseInfoBean getHbaseInfo() {
		return hbaseInfo;
	}

	public void setHbaseInfo(HbaseInfoBean hbaseInfo) {
		this.hbaseInfo = hbaseInfo;
	}

	public List<String> getKeyElems() {
		return keyElems;
	}

	public void setKeyElems(List<String> keyElems) {
		this.keyElems = keyElems;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getHbaseState() {
		return hbaseState;
	}

	public void setHbaseState(Integer hbaseState) {
		this.hbaseState = hbaseState;
	}

	public String getHbaseTable() {
		return hbaseTable;
	}

	public void setHbaseTable(String hbaseTable) {
		this.hbaseTable = hbaseTable;
	}

	public String getHbaseResult() {
		return hbaseResult;
	}

	public void setHbaseResult(String hbaseResult) {
		this.hbaseResult = hbaseResult;
	}

	public String getKeyNames() {
		return keyNames;
	}

	public void setKeyNames(String keyNames) {
		this.keyNames = keyNames;
	}

	public Integer getEncryptTime() {
		return encryptTime;
	}

	public void setEncryptTime(Integer encryptTime) {
		this.encryptTime = encryptTime;
	}

	public String toSimpleInfo() {
		return "id="+this.id+",name=" + this.name + ",url=" + this.url + ",hbaseState=" + this.hbaseState + ",table=" + this.hbaseTable
				+ ",cacheState=" + this.cacheState;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cacheState == null) ? 0 : cacheState.hashCode());
		result = prime * result + ((encryptTime == null) ? 0 : encryptTime.hashCode());
		result = prime * result + ((hbaseInfo == null) ? 0 : hbaseInfo.hashCode());
		result = prime * result + ((hbaseResult == null) ? 0 : hbaseResult.hashCode());
		result = prime * result + ((hbaseState == null) ? 0 : hbaseState.hashCode());
		result = prime * result + ((hbaseTable == null) ? 0 : hbaseTable.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((keyElems == null) ? 0 : keyElems.hashCode());
		result = prime * result + ((keyNames == null) ? 0 : keyNames.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((noCacheResu == null) ? 0 : noCacheResu.hashCode());
		result = prime * result + ((noCacheResuList == null) ? 0 : noCacheResuList.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((supplierId == null) ? 0 : supplierId.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		DataSourcePO other = (DataSourcePO) obj;
		if (cacheState == null) {
			if (other.cacheState != null)
				return false;
		} else if (!cacheState.equals(other.cacheState))
			return false;
		if (encryptTime == null) {
			if (other.encryptTime != null)
				return false;
		} else if (!encryptTime.equals(other.encryptTime))
			return false;
		if (hbaseInfo == null) {
			if (other.hbaseInfo != null)
				return false;
		} else if (!hbaseInfo.equals(other.hbaseInfo))
			return false;
		if (hbaseResult == null) {
			if (other.hbaseResult != null)
				return false;
		} else if (!hbaseResult.equals(other.hbaseResult))
			return false;
		if (hbaseState == null) {
			if (other.hbaseState != null)
				return false;
		} else if (!hbaseState.equals(other.hbaseState))
			return false;
		if (hbaseTable == null) {
			if (other.hbaseTable != null)
				return false;
		} else if (!hbaseTable.equals(other.hbaseTable))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (keyElems == null) {
			if (other.keyElems != null)
				return false;
		} else if (!keyElems.equals(other.keyElems))
			return false;
		if (keyNames == null) {
			if (other.keyNames != null)
				return false;
		} else if (!keyNames.equals(other.keyNames))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (noCacheResu == null) {
			if (other.noCacheResu != null)
				return false;
		} else if (!noCacheResu.equals(other.noCacheResu))
			return false;
		if (noCacheResuList == null) {
			if (other.noCacheResuList != null)
				return false;
		} else if (!noCacheResuList.equals(other.noCacheResuList))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (supplierId == null) {
			if (other.supplierId != null)
				return false;
		} else if (!supplierId.equals(other.supplierId))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DataSourcePO [id=" + id + ", name=" + name + ", url=" + url + ", state=" + state + ", supplierId="
				+ supplierId + ", hbaseState=" + hbaseState + ", hbaseTable=" + hbaseTable + ", hbaseResult="
				+ hbaseResult + ", keyNames=" + keyNames + ", encryptTime=" + encryptTime + ", cacheState=" + cacheState
				+ ", noCacheResu=" + noCacheResu + ", noCacheResuList=" + noCacheResuList + ", keyElems=" + keyElems
				+ ", hbaseInfo=" + hbaseInfo + "]";
	}

	/**
	 * 是否可以使用hbase查询
	 * 
	 * @return
	 */
	public boolean canUseHbase() {
		return this.hbaseState != null && this.hbaseState == DS_CAN_USE_2_STRING;
	}
	/**
	 * 是否可以使用缓存中的数据
	 * 
	 * @return
	 */
	public boolean canUseCache() {
		return this.cacheState != null && this.cacheState == DS_CAN_USE_2_STRING;
		
	}

	public void init(String mainHbaseDs, String minorDatasource) {
		//  初始化hbase查询状态
		if (StringUtils.isEmpty(hbaseTable) || StringUtils.isEmpty(hbaseResult) || StringUtils.isEmpty(keyNames)
				|| encryptTime == null ) {
			this.hbaseState = DS_CAN_NOT_USE_2_STRING;
		}
		if (canUseHbase()) {
//			this.hbaseInfo = new HbaseInfoBean(mainHbaseDs, minorDatasource, TableTemplet.map.get(this.hbaseTable));
			this.hbaseInfo = new HbaseInfoBean(mainHbaseDs, minorDatasource);
		}
		//初始化缓存的查询状态
		this.noCacheResuList = new ArrayList<String>();
		if(StringUtils.isEmpty(keyNames)|| encryptTime == null ){
			this.cacheState=DS_CAN_USE_2_STRING;
		}
		// 初始化数据源的访问要素信息
		if(StringUtils.isNotEmpty(keyNames)){
			String[] keySplit = keyNames.split(Constant.LOG_SEPARATOR);
			this.keyElems = new ArrayList<String>();
			for (String string : keySplit) {
				keyElems.add(string);
			}
		}
		// 初始化数据源的不能入缓存的结果集合
		String[] split = noCacheResu.split(Constant.LOG_PARAM_SEPARATOR);
		for (String string : split) {
			if(StringUtils.isNotEmpty(string)){
				noCacheResuList.add(string);
			}
		}
		this.name=this.name.trim();
	}
	
	/**
	 * 结果是否可以入缓存
	 * @param resu  查得结果
	 * @return
	 */
	public boolean resuCanUse(String resu){
		if(CollectionUtils.isEmpty(noCacheResuList)){
			return true;
		}
		for (String str : noCacheResuList) {
			if(str.equals(resu)){
				return false;
			}
		}
		return true;
	}
	/**
	 * 结果是否可以入缓存
	 * @param resu  查得结果
	 * @return
	 */
	public boolean resuCanNotUse(String resu){
		return !resuCanUse(resu);
	}
	
}
