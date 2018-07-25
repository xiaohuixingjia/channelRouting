package com.umpay.channelRouting.cache.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.umpay.channelRouting.cache.AbstractStringCacheImpl;

/**
 * 给予redis的缓存实现
 * 
 * @author xuxiaojia
 */
public class RedisCacheImpl extends AbstractStringCacheImpl {
	private RedisTemplate<String, String> stringRedisTemplate;

	public void setStringRedisTemplate(RedisTemplate<String, String> stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}

	public RedisCacheImpl(long catchTime, int timeType) {
		super(catchTime, timeType);
	}

	@Override
	protected String getValue(String k) {
		return stringRedisTemplate.opsForValue().get(k);
	}

	@Override
	protected boolean setValue(String k, String v, long catchTime, TimeUnit timeUnit) {
		ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
		opsForValue.set(k, v, catchTime, timeUnit);
		return true;
	}

	@Override
	public boolean setNX(String k, String v) {
		return stringRedisTemplate.opsForValue().setIfAbsent(k, v);
	}

	@Override
	public void remove(String k) {
		 stringRedisTemplate.opsForValue().getOperations().delete(k);
	}

}
