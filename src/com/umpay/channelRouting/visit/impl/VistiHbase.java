package com.umpay.channelRouting.visit.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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
import com.umpay.channelRouting.util.HttpMap;
import com.umpay.channelRouting.util.MD5Utils;
import com.umpay.channelRouting.util.StringUtil;
import com.umpay.channelRouting.util.TimeCountUtil;
import com.umpay.channelRouting.util.XmlUtils;
import com.umpay.channelRouting.visit.AbstractVisit;
import com.umpay.log.util.ConfigParams;
import com.umpay.logMerge.bean.MergeInfoBean;
import com.umpay.logMerge.util.MergeLogUtil;
/**
 * 查hbase二级缓存
* @author xuxiaojia
 */
public class VistiHbase extends AbstractVisit {
	private static final Logger channel_routing_NO_DS_simple = LoggerFactory.getLogger("channel_routing_NO_DS_simple");
	private static final String CACHE_STR = "hbaseCacheStr";
	/* 缓存调用 */
	private AbstractStringCacheImpl redisCacheImpl;
	public void setRedisCacheImpl(AbstractStringCacheImpl redisCacheImpl) {
		this.redisCacheImpl = redisCacheImpl;
	}
	public VistiHbase(String visitTypeName) {
		super(visitTypeName);
	}

	@Override
	protected boolean canVisit(ConfigDataSource configDataSource) {
		return configDataSource.getDataSourcePO().canUseHbase() && StringUtils.isNotEmpty(TaskHandler.rowLocal.get())
				&& StringUtils.isNotEmpty(configDataSource.getDataSourcePO().getHbaseTable());
	}

	@Override
	protected String visit(Map<String, String> xmlMap, ConfigDataSource configDS) {
		String key = getRedisKey(xmlMap);
		DataSourcePO dataSourcePO = configDS.getDataSourcePO();
		xmlMap.put(HttpMap.TABLENAME, dataSourcePO.getHbaseTable());
		String mobile=xmlMap.get(HttpMap.MOBILEID);
		if(StringUtils.isNotEmpty(mobile)&&mobile.length()<32){
			mobile=MD5Utils.getMD5Str(mobile);
		}
		xmlMap.put(HttpMap.MOBILE, mobile);
		xmlMap.put(HttpMap.VERSIONNUM,"v2");
		String responseXml = null;
		String reqXml=XmlUtils.mapToXml(xmlMap, "request");
		try {
			// 发送请求
			responseXml = CHRHttpUtil.post(ConfigParams.getProp("queryHbaseUrl")+"?queryType="+dataSourcePO.getHbaseResult(), reqXml);
			// 记录数据源返回报文
			if (StringUtils.isNotEmpty(responseXml)) {
				responseXml=responseXml.replace("\r\n", "");
				responseXml=responseXml.replace("\n", "");
				//结果可用的情况下删除分布式锁 并记录查得日志
				if(StringUtils.isNotEmpty(responseXml)&&dataSourcePO.resuCanUse(responseXml)){
					redisCacheImpl.remove(key);
					logSimple(xmlMap, responseXml);
				}else{
					responseXml=null;
				}
			}
		} catch (Exception e) {
			log.error(LogServiceImpl.getInstance().getLogPrefix() + LogInfoConstant.RESP_ERROR + "查询hbase信息出错", e);
		}
		log.info(LogServiceImpl.getInstance().getLogPrefix() + "查取hbase的xml报文为：{} 响应报文：{} " ,reqXml, responseXml);
		return responseXml;
	}
	/**
	 * 获取响应的简要日志
	 * 
	 * @param xmlMap
	 *            请求报文map
	 * @param resu
	 *            查得结果
	 * @param logSuffix
	 *            日志尾缀
	 * @return
	 */
	private void logSimple(Map<String, String> xmlMap, String resu) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String dateTime = xmlMap.get(HttpMap.DATETIME);
		String transId = xmlMap.get(HttpMap.TRANSID);
		String sequenceId = xmlMap.get(HttpMap.SEQUENCE);
		String merId = xmlMap.get(HttpMap.MERID);
		String funcode = xmlMap.get(HttpMap.FUNCODE);
		String operatorType = xmlMap.get(HttpMap.OPERATOR_TYPE);
		String umpTime = sdf.format(new Date());
		String umpayResu = CastUtil.castInt(resu, 1) + "";
		String isCache = "cache";
		// 因为简要日志中以逗号来分隔各个参数，如果商户返回结果中包含逗号，则简要日志的格式会乱，所以用一个特殊的字符来代替
		if (StringUtils.isNotEmpty(resu) ) {
			resu.replace("\r\n", "");
			resu.replace("\n", "");
			if(resu.contains(Constant.LOG_SEPARATOR)||resu.length()>CACHE_STR.length()){
				resu = CACHE_STR;
			}
		}
		String[] reqArgs = { umpTime, dateTime, transId, funcode, merId, sequenceId, TaskHandler.reqArgLocal.get(),
				operatorType, "00", resu, umpayResu, isCache };
		channel_routing_NO_DS_simple.info(StringUtil.toSpecificLine(reqArgs, Constant.LOG_SEPARATOR));
		String jifeiFlag = MergeLogUtil.NO_JIFEI;
		MergeLogUtil.mergeResponse(new MergeInfoBean.MergeInfoBeanBuilder()
				.setCacheFlag(isCache)
				.setCommonInfoMap(xmlMap)
				.setDataSourceRelult(resu)
				.setDataSourceReturnCode("00")
				.setJiFeiFlag(jifeiFlag)
				.setLdResult(umpayResu)
				.setQueryArams(TaskHandler.reqArgLocal.get())
				.setTimeCount(TimeCountUtil.getTimeConsuming() + "")
				.setQueryType("00")
				.setDataSourceType("hbaseCache")
				.build());
	}
}
