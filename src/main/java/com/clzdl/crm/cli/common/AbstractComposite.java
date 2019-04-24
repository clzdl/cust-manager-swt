package com.clzdl.crm.cli.common;

import org.eclipse.swt.widgets.Composite;

import com.clzdl.crm.common.enums.EnumSysPermissionProfile;

public class AbstractComposite extends Composite implements IPermission {
	private String title;
	private EnumSysPermissionProfile permission;

	public AbstractComposite(Composite parent, int style, String title, EnumSysPermissionProfile permission) {
		super(parent, style);
		this.title = title;
		this.permission = permission;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public EnumSysPermissionProfile getPermission() {
		return permission;
	}

}
