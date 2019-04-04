package com.clzdl.crm.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clzdl.crm.App;
import com.clzdl.crm.ExceptionMessage;
import com.fasterxml.jackson.databind.JsonNode;
import com.framework.common.exception.BizException;
import com.framework.common.util.json.JsonUtil;
import com.framework.common.util.net.http.client.HttpSendClientFactory;

public class HttpUtil {
	private final static Integer _successFlagCode = 1;
	private final static Integer _errNoLoginCode = 1;
	private final static Logger _logger = LoggerFactory.getLogger(HttpUtil.class);
	public static String domain = "http://127.0.0.1:8088";

	public static class HttpParam {
		private String name;
		private Object value;

		public HttpParam(String name, Object value) {
			super();
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

	}

	public static JsonNode get(String uri, HttpParam... paramArray) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		if (paramArray != null) {
			for (int i = 0; i < paramArray.length; ++i) {
				params.put(paramArray[i].getName(), paramArray[i].getValue());
			}
		}
		return _get(uri, params);
	}

	public static JsonNode post(String uri, HttpParam... paramArray) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		if (paramArray != null) {
			for (int i = 0; i < paramArray.length; ++i) {
				params.put(paramArray[i].getName(), paramArray[i].getValue());
			}
		}
		return _post(uri, params);
	}

	public static JsonNode postJsonString(String uri, String json) throws Exception {
		_logger.info("http request uri:{},json:{}", uri, json);
		String resp = HttpSendClientFactory.getCookieInstance().postJson(domain + uri, null, json);
		return _parse(resp);
	}

	public static JsonNode postJsonObject(String uri, Object json) throws Exception {
		return postJsonString(uri, JsonUtil.toJson(json));
	}

	public static JsonNode postJson(String uri, String json, HttpParam... paramArray) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		if (paramArray != null) {
			for (int i = 0; i < paramArray.length; ++i) {
				params.put(paramArray[i].getName(), paramArray[i].getValue());
			}
		}

		String resp = HttpSendClientFactory.getCookieInstance().postJson(domain + uri, params, json);
		return _parse(resp);
	}

	public static JsonNode post(String uri) throws Exception {
		_logger.info("http request:{}", uri);
		return _post(uri, null);
	}

	private static JsonNode _get(String uri, Map<String, Object> params) throws Exception {
		_logger.info("http request uri:{}", uri);
		String resp = HttpSendClientFactory.getCookieInstance().get(domain + uri, params);
		_logger.info("http response:{}", resp);
		return _parse(resp);
	}

	private static JsonNode _post(String uri, Map<String, Object> params) throws Exception {
		_logger.info("http request:{},{}:", uri, JsonUtil.toJson(params));
		String resp = HttpSendClientFactory.getCookieInstance().post(domain + uri, params);
		return _parse(resp);
	}

	private static JsonNode _parse(String resp) throws Exception {
		_logger.info("resp:{}", resp);
		JsonNode jsonNode = JsonUtil.stringToJsonNode(resp);
		if (jsonNode == null) {
			throw new BizException(ExceptionMessage.NWETWORK_WRONG);
		}

		if (jsonNode.get("flag").asInt() != _successFlagCode) {
			if (jsonNode.get("errorCode").asInt() == _errNoLoginCode) {
				App.getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						App.login();
					}
				});
			} else {
				throw new BizException(ExceptionMessage.getEnum(jsonNode.get("errorCode").asInt()));
			}
		}
		return jsonNode.get("data");
	}

}
