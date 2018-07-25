package com.umpay.channelRouting.center.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.umpay.channelRouting.bean.CutBackDSConfBean;
import com.umpay.channelRouting.bean.DSThresholdConfBean;
import com.umpay.channelRouting.bean.SortDsBean;
import com.umpay.channelRouting.center.ConfigDataSource;
import com.umpay.channelRouting.center.LocalConfig;
import com.umpay.channelRouting.center.WeightConfDSlist;
import com.umpay.channelRouting.center.WeightConfigDS;
import com.umpay.channelRouting.center.service.GetChannelInfoService;
import com.umpay.channelRouting.center.tree.ChannelRountingNode;
import com.umpay.channelRouting.dao.ChannelRoutingDao;
import com.umpay.channelRouting.exception.NoDataSourceException;
import com.umpay.channelRouting.po.ChannelRoutingPO;
import com.umpay.channelRouting.po.DataSourcePO;
import com.umpay.channelRouting.service.impl.LogServiceImpl;

/**
 * 通道信息封装的类
 * 
 * @author xuxiaojia
 */
public class ChannelMapInfo implements Serializable, GetChannelInfoService {
	private static final Logger log = LoggerFactory.getLogger(ChannelMapInfo.class);
	private static final long serialVersionUID = -3999096148521744294L;
	/* 数据源map key为数据源ID */
	private Map<String, ConfigDataSource> sourceMap;
	/* 通道路由树 */
	private ChannelRountingNode chrTree;
	/* 阀值配置 */
	private DSThresholdConfBean thresholdConf;
	/* 数据源切回配置 */
	private CutBackDSConfBean cutBackDSConf;
	/* 数据库通信者 */
	@Autowired
	private ChannelRoutingDao dao;
	/* 本地配置信息 */
	private LocalConfig config;
	public void setConfig(LocalConfig config) {
		this.config = config;
	}

	public DSThresholdConfBean getThresholdConf() {
		return thresholdConf;
	}

	public void setThresholdConf(DSThresholdConfBean thresholdConf) {
		this.thresholdConf = thresholdConf;
	}

	public CutBackDSConfBean getCutBackDSConf() {
		return cutBackDSConf;
	}

	public void setCutBackDSConf(CutBackDSConfBean cutBackDSConf) {
		this.cutBackDSConf = cutBackDSConf;
	}

	/**
	 * 初始化数据到map集合中 不加载缓存文件中的配置
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		init(false);
	}

	/**
	 * 初始化数据到map集合中
	 * 
	 * @param flag
	 *            是否在连接不上数据库的情况下加载本地配置 true -加载 false - 不加载
	 * @throws Exception
	 */
	public void init(boolean flag) throws Exception {

		sourceMap = new HashMap<String, ConfigDataSource>();
		chrTree = ChannelRountingNode.createRootNode();
		// 1,加载所有的通道路由信息
		List<ChannelRoutingPO> channelRoutingList = null;
		// 2,加载所有的数据源信息
		List<DataSourcePO> dataSourcePOList = null;
		try {
			channelRoutingList = dao.getAllChannelRoutingPO();
			dataSourcePOList = dao.getAllDataSourcePO();
		} catch (Exception e) {
			if (flag) {
				log.error("查询数据库加载配置信息异常,使用本地缓存文件",e);
				channelRoutingList=config.getConfig(ChannelRoutingPO.class);
				dataSourcePOList=config.getConfig(DataSourcePO.class);
			} else {
				throw e;
			}
		}
		// 将配置信息存入本地文件中
		config.writeConfig2localFile(channelRoutingList, config.getConfigFilePath(ChannelRoutingPO.class));
		config.writeConfig2localFile(dataSourcePOList, config.getConfigFilePath(DataSourcePO.class));
		// 3,遍历包装数据源，将阀值配置和数据源回切配置放入
		List<ConfigDataSource> configDSlist = DSpolist2configDSlist(dataSourcePOList);
		// 4,将数据源信息放入内存中
		initSourceMap(configDSlist);
		// 5,路由信息放入内存中
		chrTree.createTree(channelRoutingList);
	}

	/**
	 * 遍历包装数据源集合 将将阀值配置和数据源回切配置信息放入各个对象中 并根据配置信息初始化一些数据
	 * 
	 * @param packDSlist
	 *            包装数据源集合
	 */
	private List<ConfigDataSource> DSpolist2configDSlist(List<DataSourcePO> dataSourcePOList) {
		List<ConfigDataSource> configDSlist = new ArrayList<ConfigDataSource>();
		// hbase数据源集群名称
		String mainHbaseDs = null;
		String minorDatasource = null;
		// TODO 不再访问hbase
		// String zkConfig = ConfigParams.getProp("dataSource");
		// String[] cfgs = zkConfig.split(Constant.SEP_SEMICOLON);
		// for (int i = 0; i < cfgs.length; i++) {
		// String[] dss = cfgs[i].split(Constant.SEP_COLON);
		// if(i==0){
		// mainHbaseDs=dss[0];
		// }else{
		// minorDatasource=dss[0];
		// }
		// }
		// 遍历数据源包装阀值配置
		for (DataSourcePO dsPO : dataSourcePOList) {
			dsPO.init(mainHbaseDs, minorDatasource);
			ConfigDataSource configDS = new ConfigDataSource();
			configDS.setCutBackDSConf(cutBackDSConf);
			configDS.setThresholdConf(thresholdConf);
			configDS.setDataSourcePO(dsPO);
			configDS.setDSstate(dsPO.getState());
			configDS.init();
			configDSlist.add(configDS);
		}
		return configDSlist;
	}

	private void initSourceMap(List<ConfigDataSource> configDSlist) {
		for (ConfigDataSource configDS : configDSlist) {
			sourceMap.put(configDS.getDataSourcePO().getId(), configDS);
		}
	}

	/**
	 * 根据数据源key获取可以访问的数据源集合
	 * 
	 * @param key
	 * @return
	 * @throws NoDataSourceException
	 */
	public List<WeightConfDSlist> getDataSource(ChannelRoutingPO channelRouting) throws NoDataSourceException {
		List<SortDsBean> leafListBychr = chrTree.getLeafListBychr(channelRouting);
		channelAddCheck(leafListBychr);
		// 获取排名对象的集合
		List<SortDsBean> sortDsList = new ArrayList<SortDsBean>();
		sortDsList.addAll(leafListBychr);
		// 按排名排序
		Collections.sort(sortDsList);
		log.info(LogServiceImpl.getInstance().getLogPrefix() + "获得的排序后的数据源排名为：" + sortDsList.toString());
		// 生成可用数据源集合返回
		LinkedList<WeightConfDSlist> weightList = gererWeightList(sortDsList);
		// 判断可用数据源集合是否为空
		if (CollectionUtils.isEmpty(weightList)) {
			throw new NoDataSourceException(channelRouting);
		}
		return weightList;
	}

	/**
	 * 判断通道是否需要增加排序
	 */
	private void channelAddCheck(List<SortDsBean> leafListBychr) {
		// 便利获取到的通道判断数据源是否需要累加排序
		for (SortDsBean sortDsBean : leafListBychr) {
			ConfigDataSource configDataSource = sourceMap.get(sortDsBean.getDsId());
			if (configDataSource.getCanUseValue() && configDataSource.needAddSort()) {
				// 排序累加
				int sortAdd = sortDsBean.sortAdd();
				log.info(sortDsBean.getDsId() + "数据源需要增加排序，增加后的排序结果是：" + sortAdd);
			}
		}
	}

	/**
	 * 生成可用集合
	 * 
	 * @param sortDsList
	 *            通道集合
	 * @return
	 */
	private LinkedList<WeightConfDSlist> gererWeightList(List<SortDsBean> sortDsList) {
		Integer currSort = null;
		LinkedList<WeightConfDSlist> weightList = new LinkedList<WeightConfDSlist>();
		for (SortDsBean sortDsBean : sortDsList) {
			if (sourceMap.get(sortDsBean.getDsId()).getCanUseValue()) {
				if (currSort == null || currSort != sortDsBean.getSort()) {
					weightList.add(new WeightConfDSlist());
					currSort = sortDsBean.getSort();
				}
				weightList.getLast().add(new WeightConfigDS(sortDsBean, sourceMap.get(sortDsBean.getDsId())));
			} else {
				log.info(LogServiceImpl.getInstance().getLogPrefix() + "id为：" + sortDsBean.getDsId() + "数据源不可用,阀值为："
						+ sourceMap.get(sortDsBean.getDsId()).getCurrThresholdValue());
			}
		}
		return weightList;
	}

	public ChannelMapInfo() {
		super();
	}

	/**
	 * 创建一个新的对象并将自己的属性赋予新的对象中
	 * 
	 * @return
	 */
	public ChannelMapInfo cloneOneSlef() {
		ChannelMapInfo mapInfo = new ChannelMapInfo();
		mapInfo.sourceMap = new HashMap<String, ConfigDataSource>();
		mapInfo.sourceMap.putAll(this.sourceMap);
		mapInfo.chrTree = this.chrTree;
		return mapInfo;
	}

	/**
	 * 获取当前内存中的所有的数据源ID的集合
	 * 
	 * @return
	 */
	public Set<String> getAllDsId() {
		return this.sourceMap.keySet();
	}
	
}
