package com.umpay.channelRouting.center;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.umf.warnMsg.service.WXWarnService;
import com.umpay.channelRouting.util.CHRHttpUtil;

/**
 * 异步定时访问数据源的bean类
 * 
 * @author xuxiaojia
 */
public class AsynTimerTask extends TimerTask {
	private static final Logger log = LoggerFactory.getLogger(AsynTimerTask.class);
	private ConfigDataSource configDS;
	public ConfigDataSource getConfigDS() {
		return configDS;
	}

	public void setConfigDS(ConfigDataSource configDS) {
		this.configDS = configDS;
	}

	public AsynTimerTask() {
		super();
	}

	public AsynTimerTask(ConfigDataSource configDS) {
		super();
		this.configDS = configDS;
	}

	@Override
	public void run() {
		try {
//			MsgWarnService.sendMsg(configDS.getDataSourcePO().getName()+"开始尝试切回");
			WXWarnService.sendMsg(configDS.getDataSourcePO().getName()+"开始尝试切回");
			// 连续访问3次 TODO 是否做到配置里？
			for (int i = 0; i < 3; i++) {
				CHRHttpUtil.post(configDS.getDataSourcePO().getUrl(), configDS.getTestMessage());
			}
			// 设置该数据源为可以使用 并停止此异步线程
			configDS.DScanUseAndSync2dataBase();
			WXWarnService.sendMsg(configDS.getDataSourcePO().getName()+"切回成功");
//			MsgWarnService.sendMsg(configDS.getDataSourcePO().getName()+"切回成功");
		} catch (Exception e) {
			WXWarnService.sendMsg(configDS.getDataSourcePO().getName()+"切回失败");
//			MsgWarnService.sendMsg(configDS.getDataSourcePO().getName()+"切回失败");
			//记录日志 并等待下一个时刻执行默认访问
			log.error("此次访问数据源失败，等待下次访问"+configDS.toString(),e);
			configDS.DScanNotUseAndSync2dataBase();
		}

	}

}
