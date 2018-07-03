package com.yuechuankeji.http;

import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http协议客户端，用于同LoginServer或其第三方服务器进行http通信（变更服务器状态、登录验证、SDK充值等）
 */
public class MyClient {
	public static Logger log = LoggerFactory.getLogger(MyClient.class.getSimpleName());
	public String host;
	public int port;

	public MyClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}


	public String sendRequest(String page, String message) {
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost(host, port, "http");
		log.info("pre request url:{}:{}//{}",host,port,page);
		HttpMethod method = getPostMethod(page, message);
		String responseMess = null;
		try {
			client.executeMethod(method);
			log.info("after request url:{}",page);
			int code = method.getStatusLine().getStatusCode();
			log.info("after request code:{}",code);
			responseMess = method.getResponseBodyAsString();
			responseMess = responseMess == null ? null : responseMess.trim();
			log.info("after request ret:{}",responseMess);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
			client.getHttpConnectionManager().closeIdleConnections(0);
		}
		return responseMess;
	}
	
	public HttpMethod getPostMethod(String page, String message) {
		PostMethod post = new PostMethod(page);
		post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		NameValuePair nvl = new NameValuePair("message", message);
		post.setRequestBody(new NameValuePair[] { nvl });
		return post;
	}
	
}
