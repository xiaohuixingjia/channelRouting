package com.umpay.channelRouting.proxyservice;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.http.MutableHttpRequest;
import org.apache.mina.filter.codec.http.MutableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bs3.inf.IProcessors.HSessionInf;
import com.bs3.nio.mina2.Mina2H4Rpc2;
import com.bs3.nio.mina2.codec.IHttp;
import com.umpay.channelRouting.reload.ReloadService;
import com.umpay.channelRouting.service.impl.LogServiceImpl;
import com.umpay.channelRouting.util.SpringUtil;
import com.umpay.channelRouting.util.TimeCountUtil;
import com.umpay.channelRouting.util.XmlUtils;

/**
 * 
 * @author xuxiaojia
 *
 */
public class NioServerHandler extends Mina2H4Rpc2 {
	/* 重载标识 */
	private final static Logger _log = LoggerFactory.getLogger(NioServerHandler.class);
	@Override
	protected void onServerReadReq(HSessionInf session, Object req) {
		String reqXML = null;
		try {
			//判断是否是重载数据库配置到内存的请求
			if(have2reload(req)){
				//重载配置信息
				reload(session);
				return ;
			}
			TimeCountUtil.setStartTime();
			// 获取报文
			reqXML = getRequXml(req);
			//接收到请求报文
			_log.info("通道路由接收到的报文为："+reqXML);
			// 解析报文为map格式
			Map<String, String> reqMap = getReqXmlMap(reqXML);
			//记录此线程的日志参数
			LogServiceImpl.getInstance().init(reqMap);
			// 获取处理对象
			TaskHandler taskHandler = SpringUtil.getInstance().getContext().getBean(TaskHandler.class);
			// 获取处理后的信息
			String responseStr = taskHandler.execute(reqMap);
			//返回给商户报文
			_log.info(LogServiceImpl.getInstance().getLogPrefix()+"响应耗时:"+TimeCountUtil.getTimeConsuming()+",返回给商户的报文:"+responseStr);
			// 将处理后的信息返回
			this.responseContent(session, responseStr!=null?responseStr:"");
		} catch (Exception e) {
			 _log.error(LogServiceImpl.getInstance().getLogPrefix()+LogInfoConstant.CHANNEL_ERROR + reqXML == null ? LogInfoConstant.REQ_XML_ANALYZE_ERROR : reqXML, e);
			 this.responseContent(session, "");
		}
	}

	private synchronized void reload(HSessionInf session) {
		ReloadService reloadService =  SpringUtil.getInstance().getContext().getBean(ReloadService.class);
		String resu = reloadService.reload();
		this.responseContent(session, resu);
	}
	
	/**
	 * 从请求信息中判断该次请求是否需要重载数据库数据到内存
	 * @param req
	 * @return
	 */
	private boolean have2reload(Object req){
		MutableHttpRequest request = (MutableHttpRequest) req;
		String requestURL = "";
		if (request != null && request.getRequestUri() != null) {
			requestURL = request.getRequestUri().getPath();
		}
		return Constant.CR_RELOAD_URL.equals(requestURL);
	}

	/**
	 * 将xml报文解析为hashmap对象 解析出错返回一个空的hashmap
	 * 
	 * @param reqXml
	 *            解析的xml文本
	 * @return
	 */
	private Map<String, String> getReqXmlMap(String reqXml) {
		try {
			Map<String, String> reqXmlMap = XmlUtils.xmlToMap(reqXml);
			return reqXmlMap;
		} catch (Exception e) {
			_log.error("将请求报文解析为hashmap出错" + reqXml + e);
			return new HashMap<String, String>();
		}

	}

	/**
	 * 获取请求中的xml报文
	 * 
	 * @param req
	 * @return
	 */
	private String getRequXml(Object req) {
		/* 接收请求，解析POST报文体 */
		MutableHttpRequest request = (MutableHttpRequest) req;
		IoBuffer content = (IoBuffer) request.getContent();
		byte[] conBytes = new byte[content.limit()];
		content.get(conBytes);
		String reqXML = new String(conBytes);
		return reqXML;
	}

	/**
	 * 返回响应给商户的方法
	 * 
	 * @param session
	 * @param responseStr
	 */
	private void responseContent(HSessionInf session, String responseStr) {
		try {
			/* 第四步：返回 */
			// _log.info("返回的报文如下:\n"+responseStr);
			MutableHttpResponse res = IHttp.makeResp(new IHttp.HResponse(), IHttp.HConst.SC_OK, "", null, "text/plain",
					responseStr.getBytes());
			session.write(res);
		} catch (Exception e) {
			_log.error("", e);
			session.close("");
		}
	}

}
