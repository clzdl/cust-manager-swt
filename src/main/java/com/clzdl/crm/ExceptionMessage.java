package com.clzdl.crm;

import com.base.exception.IExceptionMessage;

public enum ExceptionMessage implements IExceptionMessage {

	SYS_ERROR(10000, "系统异常，请稍后再试"), PARAMS_ERROR(10001, "参数错误"), NO_DATA_ERROR(10002, "数据不存在"),
	NO_RIGHT_ERROR(10003, "无权操作"), USER_LOGIN_ERROR(10004, "登录名或密码错误"),
	EXIST_SUBORDINATE_DELETE_WRONG(10005, "存在下级时不允许删除");

	public static ExceptionMessage getEnum(String message) {
		if (message == null) {
			return null;
		}
		for (ExceptionMessage exceptionMessage : ExceptionMessage.values()) {
			if (exceptionMessage.getMessage().equals(message)) {
				return exceptionMessage;
			}
		}
		return null;
	}

	private Integer code;
	private String message;

	private ExceptionMessage(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public Integer code() {
		return getCode();
	}

	@Override
	public String message() {
		return getMessage();
	}

}
