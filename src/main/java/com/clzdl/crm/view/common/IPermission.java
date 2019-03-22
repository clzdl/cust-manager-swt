package com.clzdl.crm.view.common;

import com.clzdl.crm.common.auth.enums.EnumSysPermissionProfile;

public interface IPermission {

	/**
	 * 组件需要的权限
	 * 
	 * @return
	 */
	EnumSysPermissionProfile getPermission();
}
