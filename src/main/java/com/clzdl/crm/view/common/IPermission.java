package com.clzdl.crm.view.common;

import com.clzdl.crm.springboot.auth.EnumSysPermissionProfile;

public interface IPermission {

	/**
	 * 组件需要的权限
	 * 
	 * @return
	 */
	EnumSysPermissionProfile getPermission();
}
