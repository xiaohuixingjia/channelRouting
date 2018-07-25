package com.umpay.channelRouting.center;

import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import com.umpay.channelRouting.po.DataSourcePO;

/**
 * 异步定时访问数据源的对象池
 * (单例)
 * @author xuxiaojia
 */
public class AsynTimerPool {
	/* 线程安全的map键值对，key为数据源ID 值为定时器 */
	private Map<String, Timer> timerMap = new ConcurrentHashMap<String, Timer>();
	private static AsynTimerPool pool=new AsynTimerPool();
	private AsynTimerPool (){
		
	}
	public static AsynTimerPool getInstance(){
		return pool;
	}
	/**
	 * 停止定时器
	 * 
	 * @param timer
	 *            定时器
	 */
	public void stopTimer(Timer timer) {
		if (timer != null) {
			timer.cancel();
		}
	}

	/**
	 * 删除该数据源ID在内存中存在的定时器并将该定时器停止
	 * 
	 * @param DSid
	 *            -- 数据源ID
	 */
	public void removeAndStopAsynTimer(String DSid) {
		Timer timer = timerMap.remove(DSid);
		stopTimer(timer);
	}

	/**
	 * 删除该数据源在内存中对应存在的定时器并将该定时器停止
	 * 
	 * @param configDS
	 *            -- 数据源
	 */
	public void removeAndStopAsynTimer(ConfigDataSource configDS) {
		removeAndStopAsynTimer(configDS.getDataSourcePO());
	}

	/**
	 * 删除该数据源在内存中对应存在的定时器并将该定时器停止
	 * 
	 * @param ds
	 *            -- 数据源
	 */
	public void removeAndStopAsynTimer(DataSourcePO ds) {
		removeAndStopAsynTimer(ds.getId());
	}

	/**
	 * 增加该数据源的定时任务并将依赖关系放入内存中 (每一个数据源ID 对应一个timer定时器)
	 * 
	 * @param configDS
	 */
	public synchronized void addTimer(ConfigDataSource configDS) {
		// 为避免重复，先删掉内存中存在的
		removeAndStopAsynTimer(configDS);
		// 设置守护线程如果主线程死掉则停止
		Timer timer = new Timer(true);
		// 安排指定的任务从指定的延迟后开始进行
		timer.schedule(new AsynTimerTask(configDS), configDS.getCutBackDSConf().getDelayTime());
		// 将定时器和数据源进行关系绑定并放入内存
		timerMap.put(configDS.getDataSourcePO().getId(), timer);
	}
	
	/**
	 * 获取当前异步访问线程池中的数据源ID
	 * @return
	 */
	public Set<String> getAllTimerTask4dsId(){
		return timerMap.keySet();
	}
}
