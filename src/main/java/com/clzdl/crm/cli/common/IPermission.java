package com.clzdl.crm.cli.common;

import com.clzdl.crm.common.enums.EnumSysPermissionProfile;

public interface IPermission {

	/**
	 * 组件需要的权限
	 * 
	 * @return
	 */
	EnumSysPermissionProfile getPermission();
}
