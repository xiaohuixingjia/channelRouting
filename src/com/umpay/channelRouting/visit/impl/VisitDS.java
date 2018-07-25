package com.umpay.channelRouting.visit.impl;

import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.umpay.channelRouting.cache.AbstractStringCacheImpl;
import com.umpay.channelRouting.center.ConfigDataSource;
import com.umpay.channelRouting.po.DataSourcePO;
import com.umpay.channelRouting.proxyservice.Constant;
import com.umpay.channelRouting.proxyservice.LogInfoConstant;
import com.umpay.channelRouting.proxyservice.TaskHandler;
import com.umpay.channelRouting.service.impl.LogServiceImpl;
import com.umpay.channelRouting.util.CHRHttpUtil;
import com.umpay.channelRouting.util.CastUtil;
import com.umpay.channelRouting.util.XmlUtils;
import com.umpay.channelRouting.visit.AbstractVisit;

public class VisitDS extends AbstractVisit {
	/* 缓存调用 */
	private AbstractStringCacheImpl redisCacheImpl;
	private static final Logger channel_routing_ds_simple = LoggerFactory.getLogger("channel_routing_ds_simple");

	public void setRedisCacheImpl(AbstractStringCacheImpl redisCacheImpl) {
		this.redisCacheImpl = redisCacheImpl;
	}

	public VisitDS(String visitTypeName) {
		super(visitTypeName);
	}

	@Override
	protected boolean canVisit(ConfigDataSource configDataSource) {
		return true;
	}

	@Override
	protected String visit(Map<String, String> xmlMap, ConfigDataSource configDS) {
		String key = getRedisKey(xmlMap);
		String responseXml = null;
		// 记录通道路由访问的简要日志
		DataSourcePO dataSourcePO = configDS.getDataSourcePO();
		channel_routing_ds_simple.info(LogServiceImpl.getInstance().getLogPrefix() + Constant.LOG_SEPARATOR + "第"
				+ xmlMap.get(Constant.QUERY_TIMES) + "次查询" + Constant.LOG_SEPARATOR + dataSourcePO.toSimpleInfo());
		try {
			String readTime = xmlMap.get(Constant.READ_TIME);
			responseXml = CHRHttpUtil.post(dataSourcePO.getUrl(), XmlUtils.mapToXml(xmlMap, "request"),CastUtil.castInt(readTime,CHRHttpUtil.READ_TIME_OUT));
			// 发送请求
			// 记录数据源返回报文
			log.info(LogServiceImpl.getInstance().getLogPrefix() + "发送的数据源为：" + dataSourcePO.toString()
					+ LogInfoConstant.RESP_XML + responseXml);
			// 处置结果
			dealResu(responseXml, xmlMap, configDS,key);
		} catch (Exception e) {
			redisCacheImpl.remove(key);
			log.error(LogServiceImpl.getInstance().getLogPrefix() + LogInfoConstant.RESP_ERROR + configDS.toString()
					+ " 增加阀值后结果：" + configDS.fail(configDS.getThresholdConf().getErrorStep()), e);
		}
		return responseXml;
	}

	/**
	 * 判断结果是是否可以放入缓存中并根据结果值来增加或减少数据源的阀值
	 * 
	 * @param responseXml
	 *            响应结果
	 * @param xmlMap
	 *            请求xmlMap
	 * @param configDS
	 *            数据源配置信息
	 */
	private void dealResu(String resu, Map<String, String> xmlMap, ConfigDataSource configDS,String key) {
		try {
			
			DataSourcePO dataSourcePO = configDS.getDataSourcePO();
			if (StringUtils.isEmpty(resu) || StringUtils.isEmpty(resu.trim().replaceAll("\r\n", ""))) {
				log.info(LogServiceImpl.getInstance().getLogPrefix() + " 结果为空，值不放入缓存,增加通道切换阀值后结果:"
						+ configDS.fail(configDS.getThresholdConf().getNotGetStep()) + ",增加连续未查得阀值后结果:"
						+ configDS.notGet());
				//防止分布式锁造成延迟，不入缓存的结果在redis中删除对应的key
				redisCacheImpl.remove(key);
				return;
			}
			resu = resu.trim().replaceAll("\r\n", "");
			if (!dataSourcePO.resuCanUse(resu)) {
				log.info(LogServiceImpl.getInstance().getLogPrefix() + " 结果值不可缓存：" + resu + " 数据源结果不可用集合："
						+ dataSourcePO.getNoCacheResu() + ",增加阀值后结果:"
						+ configDS.fail(configDS.getThresholdConf().getNotGetStep()) + ",增加连续未查得阀值后结果:"
						+ configDS.notGet());
				//防止分布式锁造成延迟，不入缓存的结果在redis中删除对应的key
				redisCacheImpl.remove(key);
				return;
			}
			// 返回数据可用，阀值减少
			configDS.success();
			if (CollectionUtils.isEmpty(dataSourcePO.getKeyElems())) {
				log.info(LogServiceImpl.getInstance().getLogPrefix() + " 数据源取值元素集合为空，不放入缓存");
				return;
			}
			if (StringUtils.isEmpty(TaskHandler.rowLocal.get())) {
				log.info(LogServiceImpl.getInstance().getLogPrefix() + " 本地线程没有存放key值结果不做缓存");
				return;
			}
			log.info(LogServiceImpl.getInstance().getLogPrefix() + " 将结果放入缓存中：key=" + key + " value=" + resu);
			redisCacheImpl.set(key, resu);
		} catch (Exception e) {
			log.info(LogServiceImpl.getInstance().getLogPrefix() + "放值入缓存出错：", e);
		}
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(CHRHttpUtil.post("http://10.10.67.48:9116/af/score/getScore", "<request>  <datetime>20180510000135</datetime>  <funcode>Gck00012</funcode>  <license>f9e20k8d4xfsxwtcvbpm</license>  <merid>01601002</merid>  <mobileType>1</mobileType>  <mobileid>15115511155</mobileid>  <operatorType>1</operatorType>  <querymonth>201805</querymonth>  <sign>acf5e4a727bb8f4526e453c4ea918b29</sign>  <transid>16137598111012</transid></request>"));
		
	}

}
