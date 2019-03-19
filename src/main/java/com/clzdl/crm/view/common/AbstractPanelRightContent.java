package com.clzdl.crm.view.common;

import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

public class AbstractPanelRightContent extends Composite {
	private String title;

	public AbstractPanelRightContent(Composite parent, int style, String title) {
		super(parent, style);
		this.title = title;
		setLayout(new FormLayout());
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
