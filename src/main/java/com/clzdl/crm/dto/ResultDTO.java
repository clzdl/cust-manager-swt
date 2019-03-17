package com.clzdl.crm.dto;

public class ResultDTO<T> {
	public final static Integer SUCCESS_CODE = 0;
	private Integer code = 0;
	private String errMsg = "SUCCESS";
	private T data;

	public ResultDTO() {

	}

	public ResultDTO(Integer code, String errMsg, T data) {
		super();
		this.code = code;
		this.errMsg = errMsg;
		this.data = data;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
