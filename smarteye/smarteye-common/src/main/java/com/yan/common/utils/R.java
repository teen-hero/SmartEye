
package com.yan.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 */
public class R extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	public R() {
		put("code", 0);
		put("msg", "success");
	}

	/**
	 * 复杂类型转换 TypeReference
	 */
	public <T> T getData(String key, TypeReference<T> typeReference){
		Object data = get(key);
		return JSON.parseObject(JSON.toJSONString(data), typeReference);
	}

	
	public static R error(int code, String msg) {
		R r = new R();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}


	public static R ok() {
		return new R();
	}

	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}

	public Integer getCode(){
		return (Integer) this.get("code");
	}


}
