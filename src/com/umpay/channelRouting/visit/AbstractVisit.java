package com.umpay.channelRouting.visit;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.umpay.channelRouting.center.ConfigDataSource;
import com.umpay.channelRouting.proxyservice.Constant;
import com.umpay.channelRouting.proxyservice.TaskHandler;
import com.umpay.channelRouting.service.impl.LogServiceImpl;
import com.umpay.channelRouting.util.HttpMap;
import com.umpay.channelRouting.util.TimeCountUtil;
/**
 * 模板模式配置访问数据源的多种访问方式
* @author xuxiaojia
 */
public abstract class AbstractVisit implements Visit4getResu {
	protected static final Logger log = LoggerFactory.getLogger(AbstractVisit.class);

	/**
	 * 访问方式名称
	 */
	private String visitTypeName;
	
	
	public AbstractVisit(String visitTypeName) {
		super();
		this.visitTypeName = visitTypeName;
	}




	@Override
	public String getResuFromDs(Map<String, String> xmlMap, ConfigDataSource configDataSource) {
		String resu =null;
		boolean flag=false;
		long startTime=System.currentTimeMillis();
		try {
			
			if(flag=canVisit(configDataSource)){
				resu= visit(xmlMap, configDataSource);
			}
		} catch (Exception e) {
			log.info(LogServiceImpl.getInstance().getLogPrefix()+" 以"+this.visitTypeName+"方式访问出现异常：",e);
		} finally {
			if(flag){
				logSomeThing(this.visitTypeName,TimeCountUtil.getTimeConsuming(startTime),resu);
			}
		}
		return resu;
	}
	
	/**
	 * 获取在redis缓存中的key值
	 * @param xmlMap
	 * @return
	 */
	protected String getRedisKey(Map<String, String> xmlMap){
		return xmlMap.get(HttpMap.FUNCODE)+ Constant.LOG_SEPARATOR +xmlMap.get(HttpMap.OPERATOR_TYPE)+Constant.LOG_SEPARATOR+ TaskHandler.rowLocal.get();
	}

	/**
	 * 记录访问信息
	 * @param visitTypeName2 当前的访问方式
	 * @param xmlMap 商户的访问报文信息
	 * @param configDataSource  配置数据源信息
	 * @param timeConsuming  耗时
	 * @param resu  结果
	 */
	protected void logSomeThing(String visitTypeName2, long timeConsuming,String resu) {
		log.info(LogServiceImpl.getInstance().getLogPrefix()+"访问 "+visitTypeName2+" 方式耗时："+timeConsuming+" 获得结果："+resu);
	}



	/**
	 * 判断是否可以访问 子类实现
	 * @param configDataSource
	 * @return
	 */
	protected abstract boolean canVisit(ConfigDataSource configDataSource);

	/**
	 * 访问数据源 子类实现
	 * @param xmlMap
	 * @param configDataSource
	 * @return
	 */
	protected abstract String visit(Map<String, String> xmlMap, ConfigDataSource configDataSource);
	


}
