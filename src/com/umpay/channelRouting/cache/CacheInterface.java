package com.umpay.channelRouting.cache;

import java.util.concurrent.TimeUnit;

/**
 * 缓存接口
 * 
 * @author xuxiaojia
 */
public interface CacheInterface<K, V> {
	/**
	 * 往缓存放值
	 * 
	 * @param k
	 *            -- key
	 * @param v
	 *            -- value
	 * @return 返回--true 入值成功 返回--false 入值失败
	 */
	public boolean set(K k, V v);

	/**
	 * 如果已存在key则value设置不成功，返回false ，如果不存在key，则设置value成功，返回true
	 * 
	 * @param k
	 * @param v
	 * @return
	 */
	public boolean setNX(K k, V v);

	/**
	 * 往缓存放值
	 * 
	 * @param k
	 *            -- key
	 * @param v
	 *            -- value
	 * @param timeOut
	 *            -- 缓存时长
	 * @param timeUnit
	 *            -- 缓存时间类型
	 * @return 返回--true 入值成功 返回--false 入值失败
	 */
	public boolean set(K k, V v, long timeOut, TimeUnit timeUnit);

	/**
	 * 删除k对应的值
	 * 
	 * @param k
	 * @return
	 */
	public void remove(K k);

	/**
	 * 根据key
	 * 
	 * @param k
	 * @return 返回根据key获取的value值 可能返回null
	 */
	public V get(K k);
}
