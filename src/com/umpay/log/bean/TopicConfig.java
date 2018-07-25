package com.umpay.log.bean;

import java.util.Arrays;
import java.util.List;

import com.umpay.log.util.DateUtil;
import com.umpay.log.util.LogConstant;


/**
 * kafka 主题配置 抽象类
 * 
 * @author xuxiaojia
 */
public class TopicConfig {
	/* 主题名称 */
	protected String name;
	/* 符合当前主题的日志 */
	private List<String> monitorLoggerNames;

	private TopicType type;

	public String getName() {
		return name;
	}

	public TopicConfig(String name, String monitorLoggerNames, Integer typeNum) {
		this.name = name;
		this.monitorLoggerNames = Arrays.asList(monitorLoggerNames.split(","));
		switch (typeNum) {
		case 1:
			this.type = TopicType.merge;
			break;
		case 2:
			this.type = TopicType.monitor;
			break;
		default:
			this.type = TopicType.merge;
			break;
		}
	}

	/**
	 * 是否属于当前主题的日志信息
	 * 
	 * @param info
	 * @return
	 */
	public boolean vestIn(LogInfo info) {
		return type.vestIn(info, monitorLoggerNames);
	}

	/**
	 * 转换为字符串
	 * 
	 * @param info
	 *            日志信息
	 * @param instanceCode
	 *            实例码
	 * @return
	 */
	public String info2str(LogInfo info, String instanceCode) {
		return type.info2str(info, instanceCode);
	}

	enum TopicType {
		merge {
			@Override
			public String info2str(LogInfo info, String instanceCode) {
				return instanceCode+ LogConstant.ARG_SEPARATOR+ info.toSimpleStr() ;
			}

			@Override
			public boolean vestIn(LogInfo info, List<String> monitorLoggerNames) {
				return true;
			}

		},
		monitor {
			@Override
			public String info2str(LogInfo info, String instanceCode) {
				return DateUtil.getDateString(info.getDate(), DateUtil.PARTEN_4_Y_M_D_H_M)
						+ LogConstant.SPLIT_SEPARATOR+instanceCode+LogConstant.ARG_SEPARATOR + info.toSimpleStr();
			}

			@Override
			public boolean vestIn(LogInfo info, List<String> monitorLoggerNames) {
				return monitorLoggerNames.contains(info.getLogName());
			}

		};
		public abstract String info2str(LogInfo info, String instanceCode);
		public abstract boolean vestIn(LogInfo info, List<String> monitorLoggerNames);
	}
}
