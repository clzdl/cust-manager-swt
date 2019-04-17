package com.clzdl.crm.springboot.auth;

/**
 * @classname: EnumSysPermissionProfile
 * @description: 系统权限配置
 * @author chengl
 *
 */
public enum EnumSysPermissionProfile {

	BIZ("/panel/biz", "业务数据"), CUSTOMER("/panel/biz/customer", "客户信息"), VIPCARD("/panel/biz/vipcard", "会员卡信息"),
	WORKIMG("/panel/biz/workimg", "用户作品"),

	PROFILE("/panel/profile", "配置数据"), SYSUSER("/panel/profile/sysuser", "系统用户"),
	SYSROLE("/panel/profile/sysrole", "系统角色"), SYSMENU("/panel/profile/sysmenu", "系统菜单"),

	NONE("/ALL", "无需权限");

	private String code;
	private String name;

	private EnumSysPermissionProfile(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static EnumSysPermissionProfile getEnum(String code) {
		if (code == null) {
			return null;
		}
		for (EnumSysPermissionProfile ruleEnum : EnumSysPermissionProfile.values()) {
			if (ruleEnum.code.equals(code))
				return ruleEnum;
		}
		return null;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
