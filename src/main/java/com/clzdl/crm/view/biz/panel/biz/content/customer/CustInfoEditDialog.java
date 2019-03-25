package com.clzdl.crm.view.biz.panel.biz.content.customer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clzdl.crm.App;
import com.clzdl.crm.enums.EnumUserSex;
import com.clzdl.crm.springboot.persistence.entity.CmUserInfo;
import com.clzdl.crm.utils.HttpUtil;
import com.clzdl.crm.utils.HttpUtil.HttpParam;
import com.clzdl.crm.view.common.MsgBox;
import com.fasterxml.jackson.databind.JsonNode;
import com.framework.common.util.json.JsonUtil;
import com.framework.common.util.string.StringUtil;

public class CustInfoEditDialog extends Shell {
	private final static Logger _logger = LoggerFactory.getLogger(CustInfoEditDialog.class);
	private Long id;
	private Label txtName;
	private Text edtName;
	private Label txtPhone;
	private Text edtPhone;
	private Label txtSex;
	private Group sexGroup;
	private Button radioWomen;
	private Button radioMen;
	private Label txtEmail;
	private Text edtEmail;
	private Label txtRemark;
	private Text edtRemark;
	private Button btnSubmit;
	private Button btnReset;
	private CmUserInfo cmUserInfo = null;

	private EnumUserSex userSex = EnumUserSex.WOMAN;

	public CustInfoEditDialog(Shell parent, Long id) {
		super(parent, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		this.id = id;
		setLayout(new FormLayout());

		setSize(400, 300);
		setText("修改");
		center(parent);

		setLayout(new FormLayout());
		txtName = new Label(this, SWT.NONE);
		txtName.setText("姓名");

		edtName = new Text(this, SWT.BORDER);

		txtPhone = new Label(this, SWT.NONE);
		txtPhone.setText("电话");

		edtPhone = new Text(this, SWT.BORDER);

		txtSex = new Label(this, SWT.NONE);
		txtSex.setText("性别");

		sexGroup = new Group(this, SWT.SHADOW_ETCHED_OUT);
		sexGroup.setLayout(new FillLayout(SWT.HORIZONTAL));
		radioWomen = new Button(sexGroup, SWT.RADIO);
		radioWomen.setText("女");
		radioWomen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				userSex = EnumUserSex.WOMAN;
				super.widgetSelected(e);
			}
		});
		radioMen = new Button(sexGroup, SWT.RADIO);
		radioMen.setText("男");
		radioMen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				userSex = EnumUserSex.MAN;
				super.widgetSelected(e);
			}
		});

		txtEmail = new Label(this, SWT.NONE);
		txtEmail.setText("邮箱");

		edtEmail = new Text(this, SWT.BORDER);

		txtRemark = new Label(this, SWT.NONE);
		txtRemark.setText("备注");

		edtRemark = new Text(this, SWT.BORDER | SWT.MULTI);

		btnSubmit = new Button(this, SWT.PUSH);
		btnSubmit.setText("提交");

		btnReset = new Button(this, SWT.PUSH);
		btnReset.setText("重置");

		btnSubmit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				submit();
			}
		});

		btnReset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				reset();
			}
		});

		FormData txtNameFormData = new FormData();
		txtNameFormData.right = new FormAttachment(15);
		txtName.setLayoutData(txtNameFormData);

		FormData edtNameFormData = new FormData();
		edtNameFormData.left = new FormAttachment(txtName, 2);
		edtNameFormData.right = new FormAttachment(100, -10);
		edtName.setLayoutData(edtNameFormData);

		FormData txtPhoneFormData = new FormData();
		txtPhoneFormData.top = new FormAttachment(txtName, 4);
		txtPhoneFormData.right = new FormAttachment(15);
		txtPhone.setLayoutData(txtPhoneFormData);

		FormData edtPhoneFormData = new FormData();
		edtPhoneFormData.left = new FormAttachment(txtPhone, 2);
		edtPhoneFormData.top = new FormAttachment(edtName, 4);
		edtPhoneFormData.right = new FormAttachment(100, -10);
		edtPhone.setLayoutData(edtPhoneFormData);

		FormData txtSexFormData = new FormData();
		txtSexFormData.top = new FormAttachment(txtPhone, 4);
		txtSexFormData.right = new FormAttachment(15);
		txtSex.setLayoutData(txtSexFormData);

		FormData sexGroupFormData = new FormData();
		sexGroupFormData.left = new FormAttachment(txtSex, 2);
		sexGroupFormData.top = new FormAttachment(edtPhone, 4);
		sexGroupFormData.right = new FormAttachment(100, -10);
		sexGroup.setLayoutData(sexGroupFormData);

		FormData txtEmailFormData = new FormData();
		txtEmailFormData.top = new FormAttachment(txtSex, 4);
		txtEmailFormData.right = new FormAttachment(15);
		txtEmail.setLayoutData(txtEmailFormData);

		FormData edtEmailFormData = new FormData();
		edtEmailFormData.left = new FormAttachment(txtEmail, 2);
		edtEmailFormData.top = new FormAttachment(sexGroup, 4);
		edtEmailFormData.right = new FormAttachment(100, -10);
		edtEmail.setLayoutData(edtEmailFormData);

		FormData txtRemarkFormData = new FormData();
		txtRemarkFormData.top = new FormAttachment(txtEmail, 4);
		txtRemarkFormData.right = new FormAttachment(15);
		txtRemark.setLayoutData(txtRemarkFormData);

		FormData edtRemarkFormData = new FormData();
		edtRemarkFormData.left = new FormAttachment(txtRemark, 2);
		edtRemarkFormData.top = new FormAttachment(edtEmail, 4);
		edtRemarkFormData.right = new FormAttachment(100, -10);
		edtRemarkFormData.bottom = new FormAttachment(btnSubmit, -4);
		edtRemark.setLayoutData(edtRemarkFormData);

		FormData btnSubmitFormData = new FormData();
		btnSubmitFormData.left = new FormAttachment(45);
		btnSubmitFormData.top = new FormAttachment(100, -50);
		btnSubmit.setLayoutData(btnSubmitFormData);

		FormData btnResetFormData = new FormData();
		btnResetFormData.left = new FormAttachment(btnSubmit, 10);
		btnResetFormData.top = new FormAttachment(100, -50);
		btnReset.setLayoutData(btnResetFormData);
		fillValue();
		open();
		while (!isDisposed()) {
			if (!getDisplay().readAndDispatch()) {
				getDisplay().sleep();
			}
		}
	}

	// 继承shell 需要此函数
	protected void checkSubclass() {
	}

	private void center(Shell parent) {
		/// 主屏幕显示位置
		Rectangle bounds = parent.getBounds();
		Rectangle rect = getBounds();
		int x = bounds.x + Math.max(0, (bounds.width - rect.width) / 2);
		int y = bounds.y + Math.max(0, (bounds.height - rect.height) / 2);
		setBounds(x, y, rect.width, rect.height);
	}

	private void submit() {
		cmUserInfo.setName(edtName.getText().trim());
		cmUserInfo.setPhone(edtPhone.getText().trim());
		cmUserInfo.setEmail(edtEmail.getText().trim());
		cmUserInfo.setSex(userSex.getCode().byteValue());
		cmUserInfo.setRemark(edtRemark.getText().trim());

		if (StringUtil.isBlank(cmUserInfo.getName())) {
			new MsgBox(this, "姓名不能为空").open();
			return;
		}

		if (StringUtil.isBlank(cmUserInfo.getPhone())) {
			new MsgBox(this, "手机号码不能为空").open();
			return;
		}

		try {
			HttpUtil.postJsonObject("/panel/biz/customer/save.json", cmUserInfo);
			close();
		} catch (Exception ex) {
			new MsgBox(App.getMainWindow(), ex.getMessage()).open();
		}

	}

	private void reset() {
		edtName.setText("");
		edtPhone.setText("");
		radioWomen.setSelection(true);
		edtEmail.setText("");
		edtRemark.setText("");
	}

	private void fillValue() {
		if (id == null) {
			cmUserInfo = new CmUserInfo();
		} else {
			try {
				JsonNode result = HttpUtil.get("/panel/biz/customer/getbyid.json", new HttpParam("id", id));
				cmUserInfo = JsonUtil.jsonNodeToObject(result, CmUserInfo.class);
				edtName.setText(cmUserInfo.getName());
				edtPhone.setText(cmUserInfo.getPhone());
				EnumUserSex enumSex = EnumUserSex.getEnum(cmUserInfo.getSex().intValue());
				switch (enumSex) {
				case MAN:
					radioMen.setSelection(true);
					break;
				default:
					radioWomen.setSelection(true);
					break;
				}
				edtEmail.setText(cmUserInfo.getEmail());
				edtRemark.setText(cmUserInfo.getRemark());
			} catch (Exception e) {
				new MsgBox(App.getMainWindow(), e.getMessage()).open();
			}

		}
	}

}
