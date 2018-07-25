package com.umpay.channelRouting.proxyservice;

/**
 * 常量
 * 
 * @author xuxiaojia
 */
public class Constant {
	public static final String NAME = "name";
	public static final String CARD_NO = "cardno";
	public static final String MOBILE = "mobile";

	public static final String TOKEN = "token";

	public static final String PY_RESULT = "result";
	public static final String UMPAY_RESULT = "umpayResult";

	public static final String PY_MSG = "msg";

	public static final String PRODUCT_CODE = "Pck3c001";

	public static final String DATA = "data";

	public static final String UMPAY_NAME = "name";
	public static final String UMPAY_IDENTITY = "identityNo";
	public static final String UMPAY_MOBILE = "mobileid";

	public static final String LOG_SEPARATOR = ",";
	public static final String LOG_PARAM_SEPARATOR = "&";
	/**
	 * 消息资源文件的values分隔符
	 */
	public static final String PROP_VALUE_SEPARATOR = ";";

	public static final String IDCARD_REG = "\\d{15}|\\d{18}|\\d{17}(\\d|X|x)";

	public static final String MOBILE_REG = "\\d{11}";

	public static final String UMP_NO_RECORD = "0";

	public static final String UMP_MATCH = "2";

	public static final String UMP_NO_MATCH = "1";

	public static final String MOBILE_CHINA_MOBILE = "1";
	public static final String MOBILE_CHINA_UNICOM = "2";
	public static final String MOBILE_CHINA_TELECOM = "3";
	public static final String MOBILE_NOT_MATCH = "0";

	public static final String QUERYTYPE = "queryType";

	public static final String SEP_COLON = ":";
	public static final String SEP_SEMICOLON = ";";
	/**
	 * 查询次数
	 */
	public static final String QUERY_TIMES = "queryTimes";
	/**
	 * 未查得
	 */
	public static final String NOT_FOUND = "0";
	/**
	 * 通道路由重启的线程
	 */
	public static final String CR_RELOAD_URL = "/dpsop/channelRoutingReload";

	/**
	 * utf-8编码
	 */
	public static final String UTF_8 = "utf-8";
	/**
	 * 访问数据源的读取时间
	 */
	public static final String READ_TIME="readTime";
}
