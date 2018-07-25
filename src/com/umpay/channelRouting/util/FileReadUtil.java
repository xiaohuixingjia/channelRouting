package com.umpay.channelRouting.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件读入常用工具类
 * 
 * @author xuxiaojia
 */
public class FileReadUtil {
	private static final Logger log = LoggerFactory.getLogger(FileReadUtil.class);
	/**
	 * 默认的文件解读编码格式 utf-8
	 */
	private static final String DEFAULT_CHARSET_NAME = "utf-8";

	/**
	 * 从文件中获取内容
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static String getContentFromFile(String fileName) throws Exception {
		return getContentFromFile(new File(fileName), DEFAULT_CHARSET_NAME);
	}

	/**
	 * 从文件中获取内容
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static String getContentFromFile(File file) throws Exception {
		return getContentFromFile(file, DEFAULT_CHARSET_NAME);
	}

	/**
	 * 从文件中获取内容
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static String getContentFromFile(String fileName, String charsetName) throws Exception {
		return getContentFromFile(new File(fileName), charsetName);
	}

	/**
	 * 从文件中获取内容
	 * @param file
	 * @param charsetName
	 * @return
	 * @throws Exception 
	 */
	public static String getContentFromFile(File file, String charsetName) throws Exception {
		InputStreamReader inputStreamReader = null;
		BufferedReader bfReader = null;
		StringBuilder builder = new StringBuilder();
		try {
			inputStreamReader = new InputStreamReader(new FileInputStream(file), charsetName);
			bfReader = new BufferedReader(inputStreamReader);
			String lineString = null;
			while ((lineString = bfReader.readLine()) != null) {
				builder.append(lineString);
				builder.append("\n");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (bfReader != null) {
					bfReader.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
			} catch (Exception e) {
				log.error("关闭文件流出现异常：" + file.getName(), e);
			}
		}
		return builder.toString();
	}
}
