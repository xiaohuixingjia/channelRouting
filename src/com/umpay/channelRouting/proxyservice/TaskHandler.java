package com.umpay.channelRouting.proxyservice;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.umf.warnMsg.subject.abs.impl.SubjectImpl;
import com.umpay.channelRouting.center.ConfigDataSource;
import com.umpay.channelRouting.center.WeightConfDSlist;
import com.umpay.channelRouting.center.WeightConfigDS;
import com.umpay.channelRouting.center.service.impl.ChannelInfo;
import com.umpay.channelRouting.exception.NoDataSourceException;
import com.umpay.channelRouting.po.ChannelRoutingPO;
import com.umpay.channelRouting.po.DataSourcePO;
import com.umpay.channelRouting.service.impl.LogServiceImpl;
import com.umpay.channelRouting.util.HttpMap;
import com.umpay.channelRouting.util.JacksonUtil;
import com.umpay.channelRouting.util.MD5Utils;
import com.umpay.channelRouting.visit.Visit4getResu;

/**
 * 通道路由处理逻辑
 * 
 * @author xuxiaojia
 */
public class TaskHandler {
	private static final Logger log = LoggerFactory.getLogger(TaskHandler.class);
	private static final Logger channel_error_simple = LoggerFactory.getLogger("channel_error_simple");
	/* 获取一切信息的核心对象 */
	private ChannelInfo channelInfo;
	/* 访问数据源获取信息的策略 */
	private List<Visit4getResu> visit4getResus;
	/* 加密的请求参数信息 */
	public static ThreadLocal<String> rowLocal = new ThreadLocal<String>();
	/* 未加密的请求参数 */
	public static ThreadLocal<String> reqArgLocal = new ThreadLocal<String>();
	/* 通道路由异常计数 */
	public SubjectImpl channelErrorTally;

	public void setChannelErrorTally(SubjectImpl channelErrorTally) {
		this.channelErrorTally = channelErrorTally;
	}

	public ChannelInfo getChannelInfo() {
		return channelInfo;
	}

	public void setChannelInfo(ChannelInfo channelInfo) {
		this.channelInfo = channelInfo;
	}

	public void setVisit4getResus(List<Visit4getResu> visit4getResus) {
		this.visit4getResus = visit4getResus;
	}

	/**
	 * 获取请求的数据源信息
	 * 
	 * @param reqMap
	 *            商户请求的xml转换成的map集合
	 * @param srcXml
	 *            商户请求的xml报文
	 * @return
	 */
	public String execute(Map<String, String> xmlMap) {
		ChannelRoutingPO channelRouting = new ChannelRoutingPO();
		List<WeightConfDSlist> weightConfDSlists = null;
		try {
			// 1,将请求报文中包含的通道路由信息抽取出来
			xmlMap2ChannelRouting(xmlMap, channelRouting);
			// 2,根据数据源key获取可以查询的数据源集合
			weightConfDSlists = channelInfo.getDataSource(channelRouting);
			// 3,记录获取到的数据源信息与响应时间
			log.info(LogServiceImpl.getInstance().getLogPrefix() + "key信息：" + channelRouting.keyString() + " "
					+ LogInfoConstant.DATA_SOURCE_LIST_INFO + weightConfDSlists.toString());
			// 4,遍历数据源直至获取到数据源返回的信息
			String responseXml = getRespXml(xmlMap, weightConfDSlists);
			return responseXml;
		} catch (Exception e) {
			// 通道路由异常计数
			channelErrorTally.error();
			channel_error_simple.info(LogInfoConstant.CHANNEL_ERROR + LogServiceImpl.getInstance().getLogPrefix()
					+ " 异常信息：" + e.getMessage());
			// 记录异常信息
			log.error(LogServiceImpl.getInstance().getLogPrefix() + LogInfoConstant.REQ_XML_2_CHANNEL_ROUTING
					+ obj2json(channelRouting));
			log.error(LogServiceImpl.getInstance().getLogPrefix() + LogInfoConstant.DATA_SOURCE_LIST_INFO
					+ obj2json(weightConfDSlists));
			log.error(LogServiceImpl.getInstance().getLogPrefix() + LogInfoConstant.CHANNEL_ERROR, e);
		} finally {
			clearLocalFeild();
		}
		return "";
	}

	/**
	 * 清空本地线程的变量值
	 */
	private void clearLocalFeild() {
		rowLocal.remove();
		reqArgLocal.remove();
	}

	/**
	 * 将报文中的信息提取出 功能码和运营商类型放入通道路由对象
	 * 
	 * @param xmlMap
	 * @param channelRouting
	 */
	private void xmlMap2ChannelRouting(Map<String, String> xmlMap, ChannelRoutingPO channelRouting) {
		channelRouting.setProductSuitesCode(xmlMap.get(HttpMap.FUNCODE));
		channelRouting.setMerCode(xmlMap.get(HttpMap.MERID));
		String operatorTypeString = xmlMap.get(HttpMap.OPERATOR_TYPE);
		// 默认类型为未识别
		int operatorType = ChannelRoutingPO.OPERATOR_TYPE_UNRECOGNIZED;
		if (StringUtils.isNotEmpty(operatorTypeString)) {
			try {
				operatorType = Integer.parseInt(operatorTypeString);
			} catch (Exception e) {
				log.error(LogServiceImpl.getInstance().getLogPrefix() + "运营商类型转换为int型异常" + operatorTypeString
						+ ",采用运营商未识别类型", e);
			}
		}
		channelRouting.setOperatorType(operatorType);
	}

	/**
	 * 把一个对象转换为json串 如果传入对象为null 则返回null 如果转换中出现异常，则返回空字符串
	 * 
	 * @param obj
	 * @return
	 */
	private String obj2json(Object obj) {
		try {
			return obj == null ? null : JacksonUtil.obj2json(obj);
		} catch (Exception e) {
			log.error(LogServiceImpl.getInstance().getLogPrefix() + LogInfoConstant.OBJ_2_JSON_ERROR + obj, e);
		}
		return "";
	}

	/**
	 * 遍历获取到的数据源，访问第一个可以访问的数据源并将结果返回
	 * 
	 * @param srcXml--商户发送的原xml
	 * @param packDSlist--数据源列表
	 * @return
	 * @throws NoDataSourceException
	 */
	private String getRespXml(Map<String, String> xmlMap, List<WeightConfDSlist> weightConfDSlists)
			throws NoDataSourceException {
		String responseXml = null;
		int queryTimes = 1;
		// 便利排序集合获取数据源
		for (WeightConfDSlist weightConfDSlist : weightConfDSlists) {
			WeightConfigDS w = weightConfDSlist.removeAndGetFromList();
			ConfigDataSource configDataSource = w.getConfigDataSource();
			// 初始化key信息
			initLocalKey(xmlMap, configDataSource.getDataSourcePO());
			xmlMap.put(Constant.QUERY_TIMES, "" + queryTimes++);
			// 便利访问方式获取响应信息
			responseXml = forEachVisitList(xmlMap, configDataSource);
			if ( (StringUtils.isEmpty(responseXml) || configDataSource.getDataSourcePO().resuCanNotUse(responseXml.trim().replaceAll("\r\n", "")))
					&& w.getSortDsBean().canUseNext()) {
				log.info(LogServiceImpl.getInstance().getLogPrefix() + "当前通道："+configDataSource.getDataSourcePO().getId()
						+ configDataSource.getDataSourcePO().getName() + " 未查得数据，用下一个通道接着查");
				continue;
			}
			break;
		}
		return responseXml;
	}

	/**
	 * 便利不同的访问方式来获取响应信息
	 * 
	 * @param xmlMap
	 * @param configDataSource
	 * @return
	 */
	private String forEachVisitList(Map<String, String> xmlMap, ConfigDataSource configDataSource) {
		String resuFromDs = null;
		for (Visit4getResu visit4getResu : visit4getResus) {
			resuFromDs = visit4getResu.getResuFromDs(xmlMap, configDataSource);
			if (StringUtils.isNotEmpty(resuFromDs)) {
				break;
			}
		}
		return resuFromDs;
	}

	/**
	 * 初始化每一个当前请求报文对应数据源的从缓存和hbase查询的key信息
	 * 
	 * @param xmlMap
	 * @param dataSourcePO
	 */
	private void initLocalKey(Map<String, String> xmlMap, DataSourcePO dataSourcePO) {
		try {
			// 清空原值
			clearLocalFeild();
			// 生成新的key值放入本地线程变量
			if (CollectionUtils.isNotEmpty(dataSourcePO.getKeyElems())) {
				StringBuilder rowBuilder = new StringBuilder();
				StringBuilder reqArgBuilder = new StringBuilder();
				log.info(LogServiceImpl.getInstance().getLogPrefix() + "加密的元素顺序:" + dataSourcePO.getKeyNames()
						+ ",加密次数：" + dataSourcePO.getEncryptTime());
				for (int i = 0; i < dataSourcePO.getKeyElems().size(); i++) {
					if (i > 0) {
						rowBuilder.append(Constant.LOG_SEPARATOR);
						reqArgBuilder.append(Constant.LOG_PARAM_SEPARATOR);
					}
					rowBuilder.append(EncryptedData(xmlMap.get(dataSourcePO.getKeyElems().get(i)),
							dataSourcePO.getEncryptTime()));
					reqArgBuilder.append(xmlMap.get(dataSourcePO.getKeyElems().get(i)));
				}
				log.info(LogServiceImpl.getInstance().getLogPrefix() + "加密字符串为：" + reqArgBuilder.toString() + " 加密结果："
						+ rowBuilder.toString());
				rowLocal.set(rowBuilder.toString());
				reqArgLocal.set(reqArgBuilder.toString());
			}
		} catch (Exception e) {
			log.info(LogServiceImpl.getInstance().getLogPrefix() + "加密信息出现异常", e);
		}

	}

	/**
	 * 按照加密次数进行加密
	 * 
	 * @param string
	 * @param encryptTime
	 * @return
	 */
	private String EncryptedData(String string, Integer encryptTime) {
		for (int i = 0; i < encryptTime; i++) {
			string = MD5Utils.getMD5Str(string);
		}
		return string;
	}

}
