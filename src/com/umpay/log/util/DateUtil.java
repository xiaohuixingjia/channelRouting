package com.umpay.log.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	/**
	 * 日期格式化模板：yyyy-MM-dd
	 */
	public static final String PARTEN_4_Y_M_D = "yyyy-MM-dd";
	/**
	 * 日期格式化模板：yyyy-MM-dd HH:mm:ss
	 */
	public static final String PARTEN_4_Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 日期格式化模板：yyyy-MM-dd HH:mm
	 */
	public static final String PARTEN_4_Y_M_D_H_M = "yyyy:MM:dd:HH:mm";
	/**
	 * 日期格式化模板：yyyy-MM-dd HH:mm:ss
	 */
	public static final String PARTEN_4_Y_M_D_H_M_S_2 = "yyyy-MM-dd_HH-mm-ss";

	/**
	 * 判断传入的两个日期是否是相同的日期(精确到天)
	 * 
	 * @param day1
	 * @param day2
	 * @return
	 */
	public static boolean isSameDay(Date day1, Date day2) {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.PARTEN_4_Y_M_D);
		return isSame(sdf, day1, day2);
	}

	/**
	 * 判断传入的两个日期是否是不同的日期(精确到天)
	 * 
	 * @param day1
	 * @param day2
	 * @return
	 */
	public static boolean isNotSameDay(Date day1, Date day2) {
		return !isSameDay(day1, day2);
	}

	/**
	 * 判断传入的两个日期是否是相同的日期(精确到秒)
	 * 
	 * @param day1
	 * @param day2
	 * @return
	 */
	public static boolean isSameDayTime(Date day1, Date day2) {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.PARTEN_4_Y_M_D_H_M_S);
		return isSame(sdf, day1, day2);
	}

	/**
	 * 判断传入的两个日期是否是相同的日期(精确到秒)
	 * 
	 * @param day1
	 * @param day2
	 * @return
	 */
	public static boolean isNotSameDayTime(Date day1, Date day2) {
		return !isSameDayTime(day1, day2);
	}

	/**
	 * 根据日期格式器格式化后的时间来判断传入的两个日期是否代表同一时间
	 * 
	 * @param sdfPattern
	 *            日期格式化生成器遵循的格式模板
	 * @param day1
	 *            第一个日期
	 * @param day2
	 *            第二个日期
	 * @return
	 */
	public static boolean isSame(String sdfPattern, Date day1, Date day2) {
		SimpleDateFormat sdf = new SimpleDateFormat(sdfPattern);
		return isSame(sdf, day1, day2);
	}

	/**
	 * 根据日期格式器格式化后的时间来判断传入的两个日期是否代表同一时间
	 * 
	 * @param sdf
	 *            日期格式化生成器对象
	 * @param day1
	 *            第一个日期
	 * @param day2
	 *            第二个日期
	 * @return
	 */
	public static boolean isSame(SimpleDateFormat sdf, Date day1, Date day2) {
		String ds1 = sdf.format(day1);
		String ds2 = sdf.format(day2);
		if (ds1.equals(ds2)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取当前时间的时间戳
	 * 
	 * @return
	 */
	public static String getCurrDateString() {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.PARTEN_4_Y_M_D_H_M_S_2);
		return sdf.format(new Date());
	}

	/**
	 * 获取传入时间的字符串格式
	 * 
	 * @param day
	 * @param sdfPattern
	 * @return
	 */
	public static String getDateString(Date day, String sdfPattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(sdfPattern);
		return sdf.format(day);
	}

	public static String getCurrentDate(String format) {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		SimpleDateFormat sdFromat = new SimpleDateFormat(format);
		return sdFromat.format(date);
	}

	/**
	 * 时间差，秒
	 */
	public static int getTimeDifference(Date d1, Date d2) {
		long a = d1.getTime();
		long b = d2.getTime();
		int c = (int) ((a - b) / 1000);
		return Math.abs(c);
	}

	/**
	 * 获取传入时间的分钟
	 * 
	 * @param date
	 * @return
	 */
	public static int getMinuteFromDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MINUTE);
	}
	
	public static void main(String[] args) {
		System.out.println(getMinuteFromDate(new Date()));
	}
}
