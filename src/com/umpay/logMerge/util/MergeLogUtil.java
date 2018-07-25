package com.umpay.logMerge.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.umpay.channelRouting.proxyservice.Constant;
import com.umpay.channelRouting.util.HttpMap;
import com.umpay.channelRouting.util.MD5Utils;
import com.umpay.log.proxy.ProxyLogFactory;
import com.umpay.log.proxy.ProxyLogger;
import com.umpay.logMerge.bean.MergeInfoBean;

/**
 * 日志合并工具类
 * 
 * @author xuxiaojia
 */
public class MergeLogUtil {
	private static final ProxyLogger merge_response_arg_simple = ProxyLogFactory.getLogger("merge_response_arg_simple");
	/**
	 * 计费
	 */
	public static final String JIFEI = "1";
	/**
	 * 不计费
	 */
	public static final String NO_JIFEI = "0";

	/**
	 * 打印
	 * 
	 * @param commonInfoMap
	 * @param result
	 */
	public static void mergeResponse(MergeInfoBean mergeInfoBean) {
		String md5phoneNum = "";
		if (StringUtils.isNotEmpty(mergeInfoBean.getCommonInfoMap().get(HttpMap.MOBILEID))) {
			if ("4".equals(mergeInfoBean.getCommonInfoMap().get(HttpMap.OPERATOR_TYPE))) {
				md5phoneNum = mergeInfoBean.getCommonInfoMap().get(HttpMap.MOBILEID);
			} else {
				md5phoneNum = MD5Utils.getMD5Str(mergeInfoBean.getCommonInfoMap().get(HttpMap.MOBILEID));
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		// 系统时间
		StringBuilder builder = new StringBuilder(sdf.format(new Date()));
		// 商户请求时间
		builder.append(Constant.LOG_SEPARATOR).append(mergeInfoBean.getCommonInfoMap().get(HttpMap.DATETIME))
				// 流水
				.append(Constant.LOG_SEPARATOR).append(mergeInfoBean.getCommonInfoMap().get(HttpMap.TRANSID))
				// Seqid
				.append(Constant.LOG_SEPARATOR).append(mergeInfoBean.getCommonInfoMap().get(HttpMap.SEQUENCE))
				// funcode
				.append(Constant.LOG_SEPARATOR).append(mergeInfoBean.getCommonInfoMap().get(HttpMap.FUNCODE))
				// merid
				.append(Constant.LOG_SEPARATOR).append(mergeInfoBean.getCommonInfoMap().get(HttpMap.MERID))
				// cMerid
				.append(Constant.LOG_SEPARATOR).append(mergeInfoBean.getCommonInfoMap().get(HttpMap.CHILDMERID))
				// 协议单号
				.append(Constant.LOG_SEPARATOR).append(mergeInfoBean.getCommonInfoMap().get(HttpMap.AGREEMENT))
				// 手机号MD5
				.append(Constant.LOG_SEPARATOR).append(md5phoneNum)
				// 运营商类型
				.append(Constant.LOG_SEPARATOR).append(mergeInfoBean.getCommonInfoMap().get(HttpMap.OPERATOR_TYPE))
				// 工单号
				.append(Constant.LOG_SEPARATOR).append(mergeInfoBean.getCommonInfoMap().get(HttpMap.ORDERID))
				// 查询参数
				.append(Constant.LOG_SEPARATOR).append(mergeInfoBean.getQueryArams())
				// 数据源标识
				.append(Constant.LOG_SEPARATOR).append(mergeInfoBean.getDataSourceType())
				// 接口类型
				.append(Constant.LOG_SEPARATOR).append(mergeInfoBean.getQueryType())
				// 第三方返回码
				.append(Constant.LOG_SEPARATOR).append(mergeInfoBean.getDataSourceReturnCode())
				// 第三方返回结果
				.append(Constant.LOG_SEPARATOR).append(mergeInfoBean.getDataSourceRelult())
				// 联动转码后结果
				.append(Constant.LOG_SEPARATOR).append(mergeInfoBean.getLdResult())
				// 计费标识
				.append(Constant.LOG_SEPARATOR).append(mergeInfoBean.getJiFeiFlag())
				// 缓存标识
				.append(Constant.LOG_SEPARATOR).append(mergeInfoBean.getCacheFlag())
				// 耗时
				.append(Constant.LOG_SEPARATOR).append(mergeInfoBean.getTimeCount());
		merge_response_arg_simple.info(builder.toString());
	}
}
