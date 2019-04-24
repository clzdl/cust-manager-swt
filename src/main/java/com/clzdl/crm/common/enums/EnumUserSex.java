package com.clzdl.crm.common.enums;

/**
 * @classname： EnumUserSex
 * 
 * @descripiton: 性别类型
 * @author java
 *
 */
public enum EnumUserSex {

	WOMAN(0, "女"), MAN(1, "男"), UNKNOW(2, "未知");

	private Integer code;

	private String name;

	private EnumUserSex(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static EnumUserSex getEnum(Integer code) {
		if (code == null) {
			return null;
		}
		for (EnumUserSex ruleEnum : EnumUserSex.values()) {
			if (ruleEnum.code.equals(code))
				return ruleEnum;
		}
		return null;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
