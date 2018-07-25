package com.umpay.channelRouting.center;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.umf.warnMsg.service.WXWarnService;
import com.umpay.channelRouting.bean.CutBackDSConfBean;
import com.umpay.channelRouting.bean.DSThresholdConfBean;
import com.umpay.channelRouting.dao.ChannelRoutingDao;
import com.umpay.channelRouting.po.DataSourcePO;
import com.umpay.channelRouting.util.FileReadUtil;
import com.umpay.channelRouting.util.SpringUtil;
import com.umpay.log.util.PropertiesUtil;

/**
 * 将数据源与阀值配置和回切配置合并
 * 
 * @author xuxiaojia
 */
public class ConfigDataSource implements Serializable {
	private static final Logger log = LoggerFactory.getLogger(ConfigDataSource.class);
	private static final long serialVersionUID = -5958290580515393493L;
	/* 数据源 */
	private DataSourcePO dataSourcePO;
	/* 阀值配置 */
	private DSThresholdConfBean thresholdConf;
	/* 数据源切回配置 */
	private CutBackDSConfBean cutBackDSConf;
	/* 当前阀值 */
	private AtomicInteger currThreshold;
	/* 连续未查得阀值 */
	private AtomicInteger notGetThreshold=new AtomicInteger();
	/* 默认的请求报文 */
	private String testMessage;
	/* 该数据源是否可用 默认为可以 */
	private AtomicBoolean canUse = new AtomicBoolean(true);
	/* 该数据源的排序是否需要增加 */
	private AtomicBoolean sortAdd = new AtomicBoolean(false);

	public DataSourcePO getDataSourcePO() {
		return dataSourcePO;
	}

	public void setDataSourcePO(DataSourcePO dataSourcePO) {
		this.dataSourcePO = dataSourcePO;
	}

	public String getTestMessage() {
		return testMessage;
	}

	public void setTestMessage(String testMessage) {
		this.testMessage = testMessage;
	}

	public CutBackDSConfBean getCutBackDSConf() {
		return cutBackDSConf;
	}

	public void setCutBackDSConf(CutBackDSConfBean cutBackDSConf) {
		this.cutBackDSConf = cutBackDSConf;
	}

	public DSThresholdConfBean getThresholdConf() {
		return thresholdConf;
	}

	public void setThresholdConf(DSThresholdConfBean thresholdConf) {
		this.thresholdConf = thresholdConf;
	}

	/**
	 * 初始化当前阀值为配置的初始值
	 */
	public void initCurrThreshold() {
		currThreshold = new AtomicInteger(thresholdConf.getInitThreshold());
	}

	/**
	 * 初始化该数据源的测试报文信息
	 */
	public void initTestMessage() {
		try {
			this.testMessage = FileReadUtil.getContentFromFile(getTestFileName());
			log.info(this.dataSourcePO.toString() + "默认发送报文：" + this.testMessage);
		} catch (Exception e) {
			log.error(this.dataSourcePO.toString() + "默认发送报文加载失败", e);
//			MsgWarnService.sendMsg(
//					this.getDataSourcePO().getId() + this.getDataSourcePO().getName() + "默认发送报文加载失败" + e.getMessage());
			WXWarnService.sendMsg(
					this.getDataSourcePO().getId() + this.getDataSourcePO().getName() + "默认发送报文加载失败" + e.getMessage());
		}
	}

	/**
	 * 获取对应数据源的默认报文文件全路径
	 * 
	 * @return
	 */
	private String getTestFileName() {
		return this.getClass()
				.getResource(this.cutBackDSConf.getPathOfTestMessage() + this.cutBackDSConf.getPrefixOfTestMessage()
						+ this.getDataSourcePO().getId() + this.cutBackDSConf.getSuffixOfTestMessage())
				.getPath();
	}

	/**
	 * 根据配置信息进行初始化的方法
	 */
	public void init() {
		// 初始化当前阀值
		initCurrThreshold();
		// 启用切入切出配置
		if (this.cutBackDSConf.isEnable()) {
			// 从文件中加载测试报文
			initTestMessage();
		}
	}

	public ConfigDataSource() {
		super();
	}

	/**
	 * 获取当前数据源是否可用标志
	 * 
	 * @return
	 */
	public boolean getCanUseValue() {
		return canUse.get();
	}

	/**
	 * 如果传入为空 则默认为true
	 * 
	 * 给此数据源设置是否可用 true--可用 false--不可用
	 * 
	 * @param flag
	 *            是否可用标志
	 */
	public void setDSstate(int dsState) {
		AsynTimerPool asynTimerVisitDS = AsynTimerPool.getInstance();
		if (DataSourcePO.DS_CAN_USE_2_STRING == dsState) {
			// 设置该数据源可以访问
			canUse.set(DataSourcePO.DS_CAN_USE_2_BOOLEAN);
			// 删除该数据源所对应的定时器
			asynTimerVisitDS.removeAndStopAsynTimer(this);
		} else {
			// 设置该数据源不可用
			canUse.set(DataSourcePO.DS_CAN_NOT_USE_2_BOOLEAN);
			// 启用切入切出配置
			if (this.cutBackDSConf.isEnable()) {
				// 为该数据源增加异步访问定时器
				asynTimerVisitDS.addTimer(this);
			}
		}
	}

	/**
	 * 设置该数据源状态并将该状态同步到数据库
	 * 
	 * @param dsState--数据源状态
	 */
	public void setDSstateAndSync2dataBase(int dsState) {
		// 设置该数据源状态
		setDSstate(dsState);
		// 同步该状态到数据库
		updState(dsState);
	}

	/**
	 * 更新状态到数据库
	 * 
	 * @param dsState
	 */
	private void updState(int dsState) {
		try {
			ChannelRoutingDao dao = SpringUtil.getInstance().getContext().getBean(ChannelRoutingDao.class);
			dao.updDSState( dsState, this.dataSourcePO.getId());
		} catch (Exception e) {
			String msg = this.getDataSourcePO().getName() + "数据源状态更新异常：" + dsState;
			log.error(msg, e);
//			MsgWarnService.sendMsg(msg);
			WXWarnService.sendMsg(msg);
		}
	}

	@Override
	public String toString() {
		return dataSourcePO.toString() + "当前阀值" + getCurrThresholdValue();
	}

	/**
	 * 获取当前该数据源的阀值
	 * 
	 * @return
	 */
	public int getCurrThresholdValue() {
		return this.currThreshold.get();
	}

	/**
	 * 失败--增加阀值 并判断预警等操作
	 */
	public int fail(int errorStep) {
		if(isNoWarn()){
			return 0;
		}
		int currThresValue = increaseCurrThreashold(errorStep);
		// 大于等于预警值则发送预警短信
		if (currThresValue >= this.thresholdConf.getWarnThreshold()) {
//			MsgWarnService.sendMsg(this.getDataSourcePO().getName() + "达到预警阀值,当前阀值为："
//					+ currThresValue);
			WXWarnService.sendMsg(this.getDataSourcePO().getName() + "达到预警阀值,当前阀值为："
					+ currThresValue);
		}
		// 大于等于停止阀值则设置当前数据源为不可用状态
		if (currThresValue >= this.thresholdConf.getStopThreshold()) {
			// 初始阀值
			initCurrThreshold();
			// 启用切入切出配置情况下
			if (this.cutBackDSConf.isEnable()) {
				setDSstateAndSync2dataBase(DataSourcePO.DS_CAN_NOT_USE_2_STRING);
//				MsgWarnService.sendMsg(this.getDataSourcePO().getName()
//						+ "达到最大阀值以停止该数据源的访问,并设置自动回切机制");
				WXWarnService.sendMsg(this.getDataSourcePO().getName()
						+ "达到最大阀值以停止该数据源的访问,并设置自动回切机制");
			} else {
				// 如果没有启用切入切出配置则设置此变量为true等待下次通道查询增加该数据源的排名
				sortAdd.set(true);
//				MsgWarnService.sendMsg(this.getDataSourcePO().getName()
//						+ "达到最大阀值,启用排序累加策略对当前数据源的通道进行累加");
				WXWarnService.sendMsg(this.getDataSourcePO().getName()
						+ "达到最大阀值,启用排序累加策略对当前数据源的通道进行累加");
			}
		}
		return currThresValue;
	}

	private boolean  isNoWarn() {
		try {
			String no_warning_ds_ids = PropertiesUtil.getInstance("config.properties").getConfigItem("no_warning_ds_ids");
			if(StringUtils.isNotEmpty(no_warning_ds_ids)&&Arrays.asList(no_warning_ds_ids.split(",")).contains(this.dataSourcePO.getId().trim())){
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 设置数据源可用 并同步到数据库
	 */
	public void DScanUseAndSync2dataBase() {
		setDSstateAndSync2dataBase(DataSourcePO.DS_CAN_USE_2_STRING);
	}

	/**
	 * 设置数据源不可用 并同步到数据库
	 */
	public void DScanNotUseAndSync2dataBase() {
		setDSstateAndSync2dataBase(DataSourcePO.DS_CAN_NOT_USE_2_STRING);
	}

	/**
	 * 成功--减少切换阀值和连续未查得阀值
	 */
	public int success() {
		// 如果当前阀值大于初始阀值 则减少成功时配置的应减阀值
		if (currThresMoreThanInitThres()) {
			return increaseCurrThreashold(this.thresholdConf.getSuccessStep());
		}
		if(this.notGetThreshold.get()>0){
			this.notGetThreshold.addAndGet(this.thresholdConf.getSuccessStep());
		}
		return getCurrThresholdValue();
	}

	/**
	 * 判断当前阀值是否大于初始阀值
	 * 
	 * @return
	 */
	public boolean currThresMoreThanInitThres() {
		return getCurrThresholdValue() > this.thresholdConf.getInitThreshold();
	}

	/**
	 * 增加阀值
	 * 
	 * @param num
	 *            增加的数
	 * @return
	 */
	private int increaseCurrThreashold(int num) {
		return this.currThreshold.addAndGet(num);
	}

	/**
	 * 判断当前数据源是否需要累加排序
	 * 
	 * @return
	 */
	public boolean needAddSort() {
		return this.sortAdd.getAndSet(false);
	}

	/**
	 * 未查得
	 */
	public int notGet(){
		if(isNoWarn()){
			return 0;
		}
		int currThresValue = this.notGetThreshold.incrementAndGet();
		// 大于等于预警值则发送预警短信
		if (currThresValue >= this.thresholdConf.getNotGetWarnThreshold()) {
			this.notGetThreshold.set(0);
			WXWarnService.sendMsg(this.getDataSourcePO().getName() + "连续未查得已达"
					+ currThresValue);
		}
		return currThresValue;
	}
}
