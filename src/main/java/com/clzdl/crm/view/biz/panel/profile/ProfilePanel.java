package com.clzdl.crm.view.biz.panel.profile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class ProfilePanel extends Composite {

	public ProfilePanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
		new Button(this, SWT.PUSH);
	}

}
