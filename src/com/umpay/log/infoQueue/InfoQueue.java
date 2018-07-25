package com.umpay.log.infoQueue;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.umpay.log.bean.LogInfo;

/**
 * 消息隊列
 * 
 * @author xuxiaojia
 */
public class InfoQueue {
	private static InfoQueue queue = new InfoQueue();
	private Queue<LogInfo> infoQueue = new LinkedBlockingQueue<LogInfo>(20000000);

	private InfoQueue() {

	}

	public static InfoQueue getInstance() {
		return queue;
	}

	/**
	 * 无阻塞添 向队列中添加信息 添加成功返回true 添加失败返回false
	 * 
	 * @param info
	 */
	public boolean offer(LogInfo info) {
		return infoQueue.offer(info);
	}

	/**
	 * 无阻塞 从队列中取队列头信息并删除头信息 如果队列为空，则返回null
	 * 
	 * @return
	 */
	public LogInfo poll() {
		return infoQueue.poll();
	}

	/**
	 * 返回队列的长度
	 * 
	 * @return
	 */
	public int size() {
		return infoQueue.size();
	}

}
