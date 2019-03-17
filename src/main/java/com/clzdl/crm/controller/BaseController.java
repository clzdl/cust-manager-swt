package com.clzdl.crm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.exception.BizException;
import com.clzdl.crm.App;
import com.clzdl.crm.ExceptionMessage;
import com.clzdl.crm.dto.ResultDTO;

public abstract class BaseController {
	private final static Logger _logger = LoggerFactory.getLogger(BaseController.class);

	protected static Object getBeanFromContainer(Class<?> clazz) {
		return App.context.getBean(clazz);
	}

	protected void decorateResult4Exception(ResultDTO result, Exception e) {
		_logger.error("errMsg:{}", e.getMessage(), e);
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
