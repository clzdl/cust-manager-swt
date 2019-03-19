package com.clzdl.crm.enums;

/**
 * @classname: EnumSysMenuType
 * @description: 系统菜单类型
 * @author chengl
 *
 */
public enum EnumSysMenuType {

	MENU(0, "菜单"), FUNCTION(1, "功能");

	private Integer code;

	private String name;

	private EnumSysMenuType(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static EnumSysMenuType getEnum(Integer code) {
		if (code == null) {
			return null;
		}
		for (EnumSysMenuType ruleEnum : EnumSysMenuType.values()) {
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
