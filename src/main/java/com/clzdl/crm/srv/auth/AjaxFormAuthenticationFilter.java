package com.clzdl.crm.srv.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.clzdl.crm.ExceptionMessage;
import com.framework.common.util.json.JsonUtil;
import com.framework.shrio.filter.AbstractFormAuthenticationFilter;

public class AjaxFormAuthenticationFilter extends AbstractFormAuthenticationFilter {

	@Override
	protected void deny4Ajax(HttpServletResponse httpServletResponse) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", 0);
		map.put("errorCode", ExceptionMessage.NOLOGIN.getCode());
		map.put("errorMsg", ExceptionMessage.NOLOGIN.getMessage());
		try {
			JsonUtil.reponseUtf8JsonMsg(httpServletResponse, map);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
