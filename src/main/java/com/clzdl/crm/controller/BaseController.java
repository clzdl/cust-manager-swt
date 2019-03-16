package com.clzdl.crm.controller;

import com.base.exception.BizException;
import com.clzdl.crm.App;
import com.clzdl.crm.ExceptionMessage;
import com.clzdl.crm.dto.ResultDTO;

public abstract class BaseController {
	protected static Object getBeanFromContainer(Class<?> clazz) {
		return App.context.getBean(clazz);
	}

	protected void decorateResult4Exception(ResultDTO result, Exception e) {
		if (e instanceof BizException) {
			BizException biz = (BizException) e;
			result.setCode(biz.getError_code());
			result.setErrMsg(biz.getMessage());
		} else {
			result.setCode(ExceptionMessage.SYS_ERROR.getCode());
			result.setErrMsg(ExceptionMessage.SYS_ERROR.getMessage());
		}
	}

}
