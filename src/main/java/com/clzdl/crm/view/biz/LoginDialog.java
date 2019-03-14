package com.clzdl.crm.view.biz;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginDialog {
	private final static Logger _logger = LoggerFactory.getLogger(LoginDialog.class);
	private Display display;
	private Shell parent;
	private Shell dialog;
	private Boolean logStatus = false;
	private Text edtName;
	private Text edtPwd;

	public LoginDialog(Shell parent) {
		this.parent = parent;
		display = parent.getDisplay();
	}

	public void show() {
		dialog = new Shell(parent, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		dialog.setSize(300, 150);
		dialog.setText("登录");

		/// 主屏幕显示位置
		Rectangle bounds = parent.getBounds();
		Rectangle rect = dialog.getBounds();
		int x = bounds.x + Math.max(0, (bounds.width - rect.width) / 2);
		int y = bounds.y + Math.max(0, (bounds.height - rect.height) / 2);
		dialog.setBounds(x, y, rect.width, rect.height);

		dialog.setLayout(new FormLayout());
		Label txtName = new Label(dialog, SWT.NONE);
		txtName.setText("姓名");

		edtName = new Text(dialog, SWT.BORDER);

		Label txtPwd = new Label(dialog, SWT.NONE);
		txtPwd.setText("密码");

		edtPwd = new Text(dialog, SWT.BORDER);

		Button login = new Button(dialog, SWT.PUSH);
		login.setText("登录");

		login.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				_logger.info("userName:{},pwd:{}", edtName.getText(), edtPwd.getText());

				logStatus = true;

				dialog.close();
			}
		});

		FormData txtNameFormData = new FormData();
		txtNameFormData.right = new FormAttachment(15);
		txtName.setLayoutData(txtNameFormData);

		FormData edtNameFormData = new FormData();
		edtNameFormData.left = new FormAttachment(txtName, 2);
		edtNameFormData.right = new FormAttachment(100, -10);
		edtName.setLayoutData(edtNameFormData);

		FormData txtPwdFormData = new FormData();
		txtPwdFormData.top = new FormAttachment(txtName, 4);
		txtPwdFormData.right = new FormAttachment(15);
		txtPwd.setLayoutData(txtPwdFormData);

		FormData edtPwdFormData = new FormData();
		edtPwdFormData.left = new FormAttachment(txtPwd, 2);
		edtPwdFormData.top = new FormAttachment(edtName, 4);
		edtPwdFormData.right = new FormAttachment(100, -10);
		edtPwd.setLayoutData(edtPwdFormData);

		FormData loginBtnFormData = new FormData();
		loginBtnFormData.left = new FormAttachment(50, -15);
		loginBtnFormData.top = new FormAttachment(edtPwd, 2);
		login.setLayoutData(loginBtnFormData);

		dialog.open();
		while (!dialog.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public Boolean isLogin() {
		return logStatus;
	}
}
