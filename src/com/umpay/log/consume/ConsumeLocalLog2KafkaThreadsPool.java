package com.umpay.log.consume;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.umpay.log.bean.LogInfo;
import com.umpay.log.bean.TopicConfig;
import com.umpay.log.infoQueue.InfoQueue;
import com.umpay.log.monitor.InfoQueueMonitor;
import com.umpay.log.util.ConfigParams;
import com.umpay.log.util.PropertiesUtil;

/**
 * 日志消费线程 --消费队列中的日志到kafka 因开启的端口权限较多，所以弃用 <!-- 日志消费 使用kafka时可用 -->
 * <bean id="consumeLogThreadsPool" class=
 * "com.umpay.log.consume.ConsumeLocalLog2KafkaThreadsPool" init-method=
 * "execute"> <constructor-arg index="0" value="${consumeLog.instanceCode}"/>
 * <constructor-arg index="1" value="${consumeLog.topic}"/> <constructor-arg
 * index="2" value="${consumeLog.threadNums}"/> <constructor-arg index="3" value
 * ="${consumeLog.sleepTime}"/> </bean>
 * 
 * @author xuxiaojia
 */

public class ConsumeLocalLog2KafkaThreadsPool {
	private final static Logger log = LoggerFactory.getLogger("ConsumeLogThreadsPool");
	private final static Logger noConsumeSimpleInfo = LoggerFactory.getLogger("no_consume_simple_Info");

	/* 实例码 用于区别不同机器上的应用 */
	private String instanceCode;
	/* 启动的线程个数 */
	private int threadNums;
	/* 线程在取出null后的休息时间 */
	private int sleepTime;
	/* 消费日志的线程集合 */
	private List<ConsumeLogThread> consumeLogThreads;
	/* 入kafka的消息主题 -- 日志合并 */
	private List<TopicConfig> topicOfMerges;
	private KafkaProducer<String, String> kafkaProducer;
	private AtomicInteger threshold;

	/**
	 * 传入实例号生成日志消费线程池 默认一个消费线程 无日志可取的时候间隔休息为10毫秒
	 * 
	 * @param instanceCode
	 */
	public ConsumeLocalLog2KafkaThreadsPool(String instanceCode, List<TopicConfig> topicOfMerges) {
		this(instanceCode, topicOfMerges, 1, 10);
	}

	/**
	 * 日志消费线程池
	 * 
	 * @param instanceCode
	 *            工程实例号
	 * @param threadNums
	 *            消费线程数
	 * @param sleepTime
	 *            无日志可取的间隔休息时间 单位毫秒
	 */
	public ConsumeLocalLog2KafkaThreadsPool(String instanceCode, List<TopicConfig> topicOfMerges, int threadNums,
			int sleepTime) {
		this.instanceCode = instanceCode;
		this.topicOfMerges = topicOfMerges;
		this.threadNums = threadNums;
		this.sleepTime = sleepTime;
		this.consumeLogThreads = new ArrayList<ConsumeLocalLog2KafkaThreadsPool.ConsumeLogThread>();
		threshold = new AtomicInteger(0);
	}

	private class ConsumeLogThread extends Thread {

		public void run() {
			while (true) {
				try {
					LogInfo info = InfoQueue.getInstance().poll();
					if (info != null) {
						consumeLog(info);
					} else {
						sleep(sleepTime);
					}
				} catch (Exception e) {
				}
			}
		}

		/**
		 * 消费信息到kafka
		 * 
		 * @param string
		 */
		private void consumeLog(LogInfo info) {
			try {
				// 一定阈值下发送到kafka 否则直接落错误日志
				if (threshold.get() < 100) {
					for (TopicConfig config : topicOfMerges) {
						if (config.vestIn(info)) {
							long start = System.currentTimeMillis();
							kafkaProducer.send(new ProducerRecord<String, String>(config.getName(),
									config.info2str(info, instanceCode)));
							if ((System.currentTimeMillis() - start) > 200) {
								threshold.addAndGet(10);
							}
						}
					}
					InfoQueueMonitor.consumeNum.getAndIncrement();
					// 阈值大于零，则减一
					if (threshold.get() > 0)
						threshold.decrementAndGet();
					return;
				} else {
					closeKafka();
				}
			} catch (Exception e) {
				log.info("阈值 " + threshold.incrementAndGet() + " kafka生产消息出错：" + info.toSimpleStr(), e);
			}
			noConsumeSimpleInfo.info(info.toSimpleStr());
		}

		private void closeKafka() {
			if (kafkaProducer != null) {
				try {
					kafkaProducer.close();
					kafkaProducer = null;
				} catch (Exception e) {
					log.error("关闭kafka连接出错", e);
				}
			}

		}
	}

	public void execute() {
		if (ConfigParams.useConsumeLog()) {
			kafkaProducer = new KafkaProducer<String, String>(
					PropertiesUtil.getInstance("kafka_producer.properties").getProperties());
			for (int i = 0; i < threadNums; i++) {
				consumeLogThreads.add(new ConsumeLogThread());
				consumeLogThreads.get(i).start();
			}
		}
	}

}
