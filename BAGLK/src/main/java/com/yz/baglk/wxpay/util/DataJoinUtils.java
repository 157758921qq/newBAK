package com.yz.baglk.wxpay.util;

import java.util.HashMap;
import java.util.Map;

import com.github.wxpay.sdk.WXPayUtil;
import com.yz.baglk.wxpay.config.ConfigConstant;

//微信支付方法
public class DataJoinUtils {

	public Map<String, String> wxPay(String notify_url, String out_trade_no, String total_fee, String ip, String body)
			throws Exception {
		Map<String, String> mp = new HashMap<>();
		// 公众号ID
		mp.put("appid", ConfigConstant.appId);
		// 商户号
		mp.put("mch_id", ConfigConstant.mchId);
		// 随机字符串
		mp.put("nonce_str", WXPayUtil.generateNonceStr());
		// 商品描述
		mp.put("body", body);
		// 商户订单号
		mp.put("out_trade_no", out_trade_no);
		// 订单总金额，单位分
		mp.put("total_fee", total_fee);
		// 终端IP
		mp.put("spbill_create_ip", ip);
		// 异步接收微信支付结果通知的回调地址，通知url必须外网可访问url，不能携带参数
		mp.put("notify_url", notify_url);
		// 交易类型 JSAPI公众号支付 NATIVE扫码支付 APP支付
		mp.put("trade_type", "NATIVE");
		// trade_type = NATIVE 扫码付， 必传参数
		mp.put("product_id", out_trade_no);
		// 对发送的信息进行签名
		String sign = WXPayUtil.generateSignature(mp, ConfigConstant.key);

		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("<appid>" + mp.get("appid") + "</appid>");
		sb.append("<mch_id><![CDATA[" + mp.get("mch_id") + "]]></mch_id>");
		sb.append("<nonce_str><![CDATA[" + mp.get("nonce_str") + "]]></nonce_str>");
		sb.append("<body><![CDATA[" + mp.get("body") + "]]></body>");
		sb.append("<out_trade_no><![CDATA[" + mp.get("out_trade_no") + "]]></out_trade_no>");
		sb.append("<total_fee><![CDATA[" + mp.get("total_fee") + "]]></total_fee>");
		sb.append("<spbill_create_ip><![CDATA[" + mp.get("spbill_create_ip") + "]]></spbill_create_ip>");
		sb.append("<notify_url><![CDATA[" + mp.get("notify_url") + "]]></notify_url>");
		sb.append("<trade_type><![CDATA[" + mp.get("trade_type") + "]]></trade_type>");
		sb.append("<product_id><![CDATA[" + mp.get("product_id") + "]]></product_id>");
		sb.append("<sign><![CDATA[" + sign + "]]></sign>");
		sb.append("</xml>");

		// 指定与微信的url接口
		String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String str = UrlPreUtils.post(url, sb.toString());
		System.out.println("StringBuffer str= " + str);
		Map<String, String> retmap = new HashMap<>();
		try {
			// 与微信交互，返回的消息
			retmap = WXPayUtil.xmlToMap(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retmap;
	}

	/**
	 * 
	 * 
	 * 
	 * @param orderNo
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> wxOrderQuery(String out_trade_no) throws Exception {
		Map<String, String> mp = new HashMap<>();
		mp.put("appid", ConfigConstant.appId);
		// 商户号
		mp.put("mch_id", ConfigConstant.mchId);
		mp.put("out_trade_no", out_trade_no);
		mp.put("nonce_str", WXPayUtil.generateNonceStr());
		String sign = WXPayUtil.generateSignature(mp, ConfigConstant.key);
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("<appid>" + mp.get("appid") + "</appid>");
		sb.append("<mch_id><![CDATA[" + mp.get("mch_id") + "]]></mch_id>");
		sb.append("<out_trade_no><![CDATA[" + mp.get("out_trade_no") + "]]></out_trade_no>");
		sb.append("<nonce_str><![CDATA[" + mp.get("nonce_str") + "]]></nonce_str>");
		sb.append("<sign><![CDATA[" + sign + "]]></sign>");
		sb.append("</xml>");
		// 指定与微信的url接口
		String url = "https://api.mch.weixin.qq.com/pay/orderquery";
		String str = UrlPreUtils.post(url, sb.toString());

		Map<String, String> retmap = new HashMap<>();
		try {
			// 与微信交互，返回的消息
			retmap = WXPayUtil.xmlToMap(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retmap;
	}

	public Map<String, String> wxOrderClose(String out_trade_no) throws Exception {
		Map<String, String> mp = new HashMap<>();
		mp.put("appid", ConfigConstant.appId);
		// 商户号
		mp.put("mch_id", ConfigConstant.mchId);
		mp.put("out_trade_no", out_trade_no);
		mp.put("nonce_str", WXPayUtil.generateNonceStr());
		String sign = WXPayUtil.generateSignature(mp, ConfigConstant.key);
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("<appid>" + mp.get("appid") + "</appid>");
		sb.append("<mch_id><![CDATA[" + mp.get("mch_id") + "]]></mch_id>");
		sb.append("<out_trade_no><![CDATA[" + mp.get("out_trade_no") + "]]></out_trade_no>");
		sb.append("<nonce_str><![CDATA[" + mp.get("nonce_str") + "]]></nonce_str>");
		sb.append("<sign><![CDATA[" + sign + "]]></sign>");
		sb.append("</xml>");
		// 指定与微信的url接口
		String url = "https://api.mch.weixin.qq.com/pay/closeorder";
		String str = UrlPreUtils.post(url, sb.toString());

		Map<String, String> retmap = new HashMap<>();
		try {
			// 与微信交互，返回的消息
			retmap = WXPayUtil.xmlToMap(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retmap;
	}

}
