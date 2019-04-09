package com.clzdl.crm.springboot.vo;

import java.io.Serializable;

/**
 * 上传图片返回值
 * 
 *
 */
public class UploadResultVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7328924646008965244L;

	private String fileName;
	private String contentType;
	private String path;

	public UploadResultVO(String fileName, String contentType, String path) {
		this.fileName = fileName;
		this.contentType = contentType;
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
