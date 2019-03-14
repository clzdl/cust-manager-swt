package com.clzdl.crm.view.common;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class TablePager extends Composite {

	public TablePager(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		Label txtCount = new Label(this, SWT.NONE);
		txtCount.setText("共:" + 100 + "条");
		Button btnFirst = new Button(this, SWT.PUSH);
		btnFirst.setText("首页");
		Button btnPrev = new Button(this, SWT.PUSH);
		btnPrev.setText("上一页");
		Button btnNext = new Button(this, SWT.PUSH);
		btnNext.setText("下一页");
		Button btnLast = new Button(this, SWT.PUSH);
		btnLast.setText("尾页");

		FormData txtCountFormData = new FormData();
		txtCountFormData.right = new FormAttachment(btnFirst, -20);
		txtCount.setLayoutData(txtCountFormData);

		FormData btnFirstFormData = new FormData();
		btnFirstFormData.right = new FormAttachment(btnPrev);
		btnFirst.setLayoutData(btnFirstFormData);

		FormData btnPrevFormData = new FormData();
		btnPrevFormData.right = new FormAttachment(btnNext);
		btnPrev.setLayoutData(btnPrevFormData);

		FormData btnNextFormData = new FormData();
		btnNextFormData.right = new FormAttachment(btnLast);
		btnNext.setLayoutData(btnNextFormData);

		FormData btnLastFormData = new FormData();
		btnLastFormData.left = new FormAttachment(100, -50);
		btnLast.setLayoutData(btnLastFormData);

	}

}
