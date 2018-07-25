package com.umpay.channelRouting.visit.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.umpay.channelRouting.cache.AbstractStringCacheImpl;
import com.umpay.channelRouting.center.ConfigDataSource;
import com.umpay.channelRouting.proxyservice.Constant;
import com.umpay.channelRouting.proxyservice.TaskHandler;
import com.umpay.channelRouting.service.impl.LogServiceImpl;
import com.umpay.channelRouting.util.CastUtil;
import com.umpay.channelRouting.util.HttpMap;
import com.umpay.channelRouting.util.StringUtil;
import com.umpay.channelRouting.util.TimeCountUtil;
import com.umpay.channelRouting.visit.AbstractVisit;
import com.umpay.log.util.PropertiesUtil;
import com.umpay.logMerge.bean.MergeInfoBean;
import com.umpay.logMerge.util.MergeLogUtil;

/**
 * 访问reids的方式
 * 
 * @author xuxiaojia
 */
public class VisitRedis extends AbstractVisit {
	private static final Logger channel_routing_NO_DS_simple = LoggerFactory.getLogger("channel_routing_NO_DS_simple");
	private static final String CACHE_STR = "redisCacheStr";
	/**
	 * 通道路由分布式锁标识
	 */
	private static final String CHANNEL_ROUTING_REDIS_LOCK = "CHANNEL_ROUTING_REDIS_LOCK";
	/**
	 * redis分布式锁的默认锁时间 2秒
	 */
	private static final int REDIS_LOCK_TIME=Integer.parseInt(PropertiesUtil.getInstance("config.properties").getConfigItem("redis_lock_time"));
	public VisitRedis(String visitTypeName) {
		super(visitTypeName);
	}

	/* 缓存调用 */
	private AbstractStringCacheImpl redisCacheImpl;

	public void setRedisCacheImpl(AbstractStringCacheImpl redisCacheImpl) {
		this.redisCacheImpl = redisCacheImpl;
	}

	@Override
	protected boolean canVisit(ConfigDataSource configDataSource) {
		return configDataSource.getDataSourcePO().canUseCache() && StringUtils.isNotEmpty(TaskHandler.rowLocal.get());
	}

	@Override
	protected String visit(Map<String, String> xmlMap, ConfigDataSource configDataSource) {
		String key = getRedisKey(xmlMap);
		String resu = getByDistributedLock(key);
		log.info(LogServiceImpl.getInstance().getLogPrefix() + " 从redis查询,key为：" + key + ",结果为：" + resu);
		if (StringUtils.isNotEmpty(resu)) {
			logSimple(xmlMap, resu);
		}
		return resu;
	}

	/**
	 * 通过分布式锁来获取缓存数据（在同一个信息并发访问下，只允许一个访问数据源，剩下的最多拦截2秒后如果还是未查得，则放行）
	 * 
	 * @param key
	 * @return
	 */
	private String getByDistributedLock(String key) {
		String resu = redisCacheImpl.get(key);
				
		if (StringUtils.isEmpty(resu) || isLockFalg(resu)) {
			// 查出来的结果为空或通道路由锁标识，重置结果为空
			resu = null;
			// 设置成功，则放行去查数据源
			if (redisCacheImpl.setNX(key, CHANNEL_ROUTING_REDIS_LOCK)) {
				return resu;
			} else {
				for (int i = 0; i < REDIS_LOCK_TIME; i++) {
					resu = redisCacheImpl.get(key);
					if (StringUtils.isEmpty(resu)) {
						// 1.分布式锁已删除，放行去查数据源
						break;
					}else if (isLockFalg(resu)) {
						// 2.已有分布式锁，休息100毫秒后重新查
						try {
							Thread.sleep(100);
						} catch (Exception e) {
						}
					}else {
						// 3.返回缓存结果
						break;
					}
				}
			}
		}
		if(isLockFalg(resu)){
			resu=null;
		}
		return resu;
	}

	private boolean isLockFalg(String flag){
		return CHANNEL_ROUTING_REDIS_LOCK.equals(flag);
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
				.setDataSourceType("redisCache")
				.build());
	}

}
