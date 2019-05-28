package com.dlu.ghh.utils;

import java.util.HashMap;
import java.util.Map;

public class ResponseMsg {
	// 状态码 1 成功 0 失败
	private int code;

	private String msg;

	// 用户要返回给浏览器的数据
	private Map<String, Object> extend = new HashMap<>();

	public static ResponseMsg success(String msg) {
		ResponseMsg result = new ResponseMsg();
		result.setCode(1);
		result.setMsg(msg);
		return result;
	}

	public static ResponseMsg fail(String msg) {
		ResponseMsg result = new ResponseMsg();
		result.setCode(0);
		result.setMsg(msg);
		return result;
	}

	public ResponseMsg add(String key, Object value) {
		this.getExtend().put(key, value);
		return this;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getExtend() {
		return extend;
	}

	public void setExtend(Map<String, Object> extend) {
		this.extend = extend;
	}

}
