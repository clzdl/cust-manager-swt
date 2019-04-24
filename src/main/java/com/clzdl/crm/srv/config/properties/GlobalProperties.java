package com.clzdl.crm.srv.config.properties;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 全局项目配置
 */
@Component
@ConfigurationProperties(prefix = "global")
public class GlobalProperties {
	public static final String PREFIX = "global";

	private String fileUploadPath;

	private Boolean haveCreatePath = false;

	private Boolean springSessionOpen = false;

	private Integer sessionInvalidateTime = 30 * 60; // session 失效时间（默认为30分钟 单位：秒）

	private Integer sessionValidationInterval = 15 * 60; // session 验证失效时间（默认为15分钟 单位：秒）

	public String getFileUploadPath() {
		// 如果没有写文件上传路径,保存到临时目录
		if (isEmpty(fileUploadPath)) {
			return getTempPath();
		} else {
			// 判断有没有结尾符,没有得加上
			if (!fileUploadPath.endsWith(File.separator)) {
				fileUploadPath = fileUploadPath + File.separator;
			}
			// 判断目录存不存在,不存在得加上
			if (haveCreatePath == false) {
				File file = new File(fileUploadPath);
				file.mkdirs();
				haveCreatePath = true;
			}
			return fileUploadPath;
		}
	}

	public void setFileUploadPath(String fileUploadPath) {
		this.fileUploadPath = fileUploadPath;
	}

	public Boolean getSpringSessionOpen() {
		return springSessionOpen;
	}

	public void setSpringSessionOpen(Boolean springSessionOpen) {
		this.springSessionOpen = springSessionOpen;
	}

	public Integer getSessionInvalidateTime() {
		return sessionInvalidateTime;
	}

	public void setSessionInvalidateTime(Integer sessionInvalidateTime) {
		this.sessionInvalidateTime = sessionInvalidateTime;
	}

	public Integer getSessionValidationInterval() {
		return sessionValidationInterval;
	}

	public void setSessionValidationInterval(Integer sessionValidationInterval) {
		this.sessionValidationInterval = sessionValidationInterval;
	}

	/**
	 * 对象是否为空
	 *
	 * @param obj
	 *            String,List,Map,Object[],int[],long[]
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object o) {
		if (o == null) {
			return true;
		}
		if (o instanceof String) {
			if (o.toString().trim().equals("")) {
				return true;
			}
		} else if (o instanceof List) {
			if (((List) o).size() == 0) {
				return true;
			}
		} else if (o instanceof Map) {
			if (((Map) o).size() == 0) {
				return true;
			}
		} else if (o instanceof Set) {
			if (((Set) o).size() == 0) {
				return true;
			}
		} else if (o instanceof Object[]) {
			if (((Object[]) o).length == 0) {
				return true;
			}
		} else if (o instanceof int[]) {
			if (((int[]) o).length == 0) {
				return true;
			}
		} else if (o instanceof long[]) {
			if (((long[]) o).length == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取临时目录
	 *
	 */
	public static String getTempPath() {
		return System.getProperty("java.io.tmpdir");
	}
}
