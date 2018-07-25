package com.umpay.channelRouting.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.umpay.channelRouting.po.ChannelRoutingPO;
import com.umpay.channelRouting.po.DataSourcePO;
/**
 * 数据库连接接口
 * @author xuxiaojia
 */
public interface ChannelRoutingDao {
	/**
	 * 获取所有产品核查信息
	 * 
	 * @return
	 */
	public List<ChannelRoutingPO> getAllChannelRoutingPO();

	/**
	 * 获取所有的数据源信息
	 * 
	 * @return
	 */
	public List<DataSourcePO> getAllDataSourcePO();

	/**
	 * 根据ID更新当前数据源状态
	 * 
	 * @param state--
	 *            数据源状态
	 * @param id
	 *            -- 数据源ID
	 */
	public void updDSState(@Param("state") Integer state, @Param("id") String id);
}
