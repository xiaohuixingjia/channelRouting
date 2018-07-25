package com.umpay.channelRouting.center;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.umpay.channelRouting.exception.NotFoundLocalConfigException;
import com.umpay.channelRouting.proxyservice.Constant;
import com.umpay.channelRouting.util.FileOperatorUtils;
import com.umpay.channelRouting.util.JacksonUtil;

public class LocalConfig {
	private static final Logger log = LoggerFactory.getLogger(LocalConfig.class);

	/* 本地配置文件对应类名的配置 */
	private Map<String, String> configFileMap;

	public void setConfigFileMap(Map<String, String> configFileMap) {
		this.configFileMap = configFileMap;
	}

	public <T> List<T> getConfig(Class<T> t) throws Exception {
		log.info("获取" + t.getSimpleName() + "对应文件的信息");
		if (!configFileMap.containsKey(t.getSimpleName())) {
			throw new NotFoundLocalConfigException("未找到" + t.getSimpleName() + "对应的本地文件的信息");
		}
		File file = new File(configFileMap.get(t.getSimpleName()));
		if (!file.exists()) {
			throw new NotFoundLocalConfigException("未找到" + configFileMap.get(t.getSimpleName()) + "文件");
		}
		StringBuffer buffer = new StringBuffer();
		Scanner sc = null;
		try {
			sc = new Scanner(new FileInputStream(file), Constant.UTF_8);
			// 逐行读取解析
			while (sc.hasNextLine()) {
				buffer.append(sc.nextLine());
			}
			log.info("文件信息读取完毕" + buffer.toString());
			
			return  JacksonUtil.jacksonToCollection(buffer.toString(),List.class,t );
		} catch (Exception e) {
			throw e;
		} finally {
			if (sc != null) {
				try {
					sc.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 写配置信息到本地
	 * 
	 * @param obj
	 * @param filePath
	 * @throws Exception
	 */
	public void writeConfig2localFile(Object obj, String filePath) throws Exception {
		FileOperatorUtils.deleteGeneralFile(filePath);
		FileOperatorUtils.createGeneralFile(filePath);
		FileOperatorUtils.writeTxtFile(JacksonUtil.obj2json(obj), filePath);
	}

	/**
	 * 获取本地文件路径
	 * 
	 * @param class1
	 * @return
	 * @throws Exception
	 */
	public <T> String getConfigFilePath(Class<T> t) throws Exception {
		if (!configFileMap.containsKey(t.getSimpleName())) {
			throw new NotFoundLocalConfigException("未找到" + t.getSimpleName() + "对应的本地文件的信息");
		}
		return configFileMap.get(t.getSimpleName());
	}

}
