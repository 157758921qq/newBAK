package com.yz.baglk.wxpay.util;
/*
 * 工具类
 * 
 * 
 * */

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hibernate.annotations.common.reflection.XMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javassist.expr.NewArray;

/**
 * 
 *  数据的预处理，提供静态的post 和 get方法，
 *  
 * 
 * @author Administrator
 *
 */
public class UrlPreUtils {

	//编码格式，统一用UTF-8
	private static final String ENCODING = "UTF-8";
	private final static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
	
	/**
	 * 参数：url 和 字符串
	 * 
	 * @param url
	 * @param json
	 * @return
	 */
	//返回的是string类型，里面的，xml格式
	public static String post(String url, String json) {
		StringBuffer requestText = new StringBuffer();
		CloseableHttpResponse response = null;
		CloseableHttpClient client = null;
		HttpPost httpPost = new HttpPost(url);
		StringEntity entityParams = null;
		try {
			entityParams = new StringEntity(json, "utf-8");
			httpPost.setEntity(entityParams);
			httpPost.setHeader("Content-Type", "type/json;charset=ISO-8859-1");
			client = HttpClients.createDefault();
			response = client.execute(httpPost);
			byte[] x = EntityUtils.toByteArray(response.getEntity());
			requestText.append(new String(x, "utf-8"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			logger.info("Request= " + requestText.toString());
		}
		return requestText.toString();
	}
	
	
	
	/*
	 * 基于httpClient的get方法
	 * 
	 * 
	 * 
	 * */
	
	public static String get(String url) throws RuntimeException {
		CloseableHttpClient client = HttpClients.createDefault();
		String responseText = "";
		CloseableHttpResponse response = null;
		try {
			HttpGet get = new HttpGet(url);
			response = client.execute(get);
			HttpEntity entity = response.getEntity();
			if(entity != null) {
				responseText = EntityUtils.toString(entity, ENCODING);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if(response != null)
					response.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return responseText;
	}
}















