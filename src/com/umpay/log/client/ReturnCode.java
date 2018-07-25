package com.umpay.log.client;


public class ReturnCode {
	/**
	 * 处理成功 ：0
	 */
	public static final short EXE_SUCC = 0;

	/**
	 * 处理失败：4
	 */
	public static final short EXE_FAIL = 4;

	/**
	 * 验证成功 ：2
	 */
	public static final short AUTH_SUCC = 2;
	
	/**
	 * 验证失败 ：3
	 */
	public static final short AUTH_FAIL = 3;
	
	/**
	 * 速度控制 ：5
	 */
	public static final short SPEED = 5;
}
