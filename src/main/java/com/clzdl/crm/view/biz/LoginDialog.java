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

import com.clzdl.crm.utils.HttpUtil;
import com.clzdl.crm.utils.HttpUtil.HttpParam;

public class LoginDialog extends Shell {
	private final static Logger _logger = LoggerFactory.getLogger(LoginDialog.class);
	private Boolean logStatus = false;
	private Text edtName;
	private Text edtPwd;
	private Integer tryTime = 0;

	public LoginDialog(Display parent) {
		super(parent, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		setSize(300, 150);
		setText("登录");
		Rectangle bounds = parent.getBounds();
		Rectangle rect = getBounds();
		int x = bounds.x + Math.max(0, (bounds.width - rect.width) / 2);
		int y = bounds.y + Math.max(0, (bounds.height - rect.height) / 2);
		setBounds(x, y, rect.width, rect.height);

		setLayout(new FormLayout());
		Label txtName = new Label(this, SWT.NONE);
		txtName.setText("姓名");

		edtName = new Text(this, SWT.BORDER);
		edtName.setText("admin@clzdl.com");

		Label txtPwd = new Label(this, SWT.NONE);
		txtPwd.setText("密码");

		edtPwd = new Text(this, SWT.BORDER | SWT.PASSWORD);
		edtPwd.setText("123456");

		final Button login = new Button(this, SWT.PUSH);
		login.setText("登录");

		login.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				login();
			}
		});
		setDefaultButton(login);

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

		open();
		while (!isDisposed()) {
			if (!getDisplay().readAndDispatch()) {
				getDisplay().sleep();
			}
		}
	}

	public Boolean isLogin() {
		return logStatus;
	}

	// 继承shell 需要此函数
	protected void checkSubclass() {
	}

	private void reset() {
		edtName.setText("");
		edtPwd.setText("");
	}

	private void login() {
		_logger.info("userName:{},pwd:{}", edtName.getText(), edtPwd.getText());
		try {
//			ResultDTO result = SysUserController.getBean().login(edtName.getText(), edtPwd.getText());
//			if (result.getCode() != ResultDTO.SUCCESS_CODE) {
//				MessageBox box = new MessageBox(this);
//				box.setMessage(result.getErrMsg());
//				box.open();
//				reset();
//			} else {
//				logStatus = true;
//			}
			HttpUtil.get("/panel/profile/sysuser/login.json", new HttpParam("userName", edtName.getText()),
					new HttpParam("userPwd", edtPwd.getText()));
			logStatus = true;
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (logStatus || ++tryTime >= 3) {
			dispose();
		}
	}

}
