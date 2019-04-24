package com.clzdl.crm.cli.biz.panel.profile.content.sysuser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
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
import com.clzdl.crm.cli.common.MsgBox;
import com.clzdl.crm.common.enums.EnumUserSex;
import com.clzdl.crm.srv.persistence.entity.SysRole;
import com.clzdl.crm.srv.persistence.entity.SysUser;
import com.clzdl.crm.utils.HttpUtil;
import com.clzdl.crm.utils.HttpUtil.HttpParam;
import com.fasterxml.jackson.databind.JsonNode;
import com.framework.common.util.cipher.MD5Util;
import com.framework.common.util.json.JsonUtil;
import com.framework.common.util.string.StringUtil;

public class SysUserEditDialog extends Shell {
	private final static Logger _logger = LoggerFactory.getLogger(SysUserEditDialog.class);
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
	private Label txtLoginName;
	private Text edtLoginName;
	private Label txtLoginPwd;
	private Text edtLoginPwd;
	private Label txtRole;
	private Group roleContainer;
	private Button btnSubmit;
	private Button btnReset;
	private Set<Long> selectRuleId = new HashSet<Long>();

	private SysUser sysUser = null;

	private EnumUserSex userSex = EnumUserSex.WOMAN;

	public SysUserEditDialog(Shell parent, Long id) {
		super(parent, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		this.id = id;
		setSize(400, 300);
		setText("修改");
		center(parent);
		setLayout(new FormLayout());
		/// 姓名
		txtName = new Label(this, SWT.NONE);
		txtName.setText("姓名");
		edtName = new Text(this, SWT.BORDER);

		/// 电话
		txtPhone = new Label(this, SWT.NONE);
		txtPhone.setText("电话");
		edtPhone = new Text(this, SWT.BORDER);

		// 性别
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

		// 邮箱
		txtEmail = new Label(this, SWT.NONE);
		txtEmail.setText("邮箱");
		edtEmail = new Text(this, SWT.BORDER);

		/// 登录名
		txtLoginName = new Label(this, SWT.NONE);
		txtLoginName.setText("登录名");
		edtLoginName = new Text(this, SWT.BORDER);

		/// 登录密码
		txtLoginPwd = new Label(this, SWT.NONE);
		txtLoginPwd.setText("密码");
		edtLoginPwd = new Text(this, SWT.BORDER | SWT.PASSWORD);

		/// 角色
		txtRole = new Label(this, SWT.NONE);
		txtRole.setText("角色");

		roleContainer = new Group(this, SWT.NONE);
		roleContainer.setLayout(new FillLayout());

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

		FormData txtLoginNameFormData = new FormData();
		txtLoginNameFormData.top = new FormAttachment(txtEmail, 4);
		txtLoginNameFormData.right = new FormAttachment(15);
		txtLoginName.setLayoutData(txtLoginNameFormData);

		FormData edtLoginNameFormData = new FormData();
		edtLoginNameFormData.left = new FormAttachment(txtLoginName, 2);
		edtLoginNameFormData.top = new FormAttachment(edtEmail, 4);
		edtLoginNameFormData.right = new FormAttachment(100, -10);
		edtLoginName.setLayoutData(edtLoginNameFormData);

		FormData txtLoginPwdFormData = new FormData();
		txtLoginPwdFormData.top = new FormAttachment(txtLoginName, 4);
		txtLoginPwdFormData.right = new FormAttachment(15);
		txtLoginPwd.setLayoutData(txtLoginPwdFormData);

		FormData edtLoginPwdFormData = new FormData();
		edtLoginPwdFormData.left = new FormAttachment(txtLoginPwd, 2);
		edtLoginPwdFormData.top = new FormAttachment(edtLoginName, 4);
		edtLoginPwdFormData.right = new FormAttachment(100, -10);
		edtLoginPwd.setLayoutData(edtLoginPwdFormData);

		FormData txtRoleFormData = new FormData();
		txtRoleFormData.top = new FormAttachment(txtLoginPwd, 4);
		txtRoleFormData.right = new FormAttachment(15);
		txtRole.setLayoutData(txtRoleFormData);

		FormData roleContainerPwdFormData = new FormData();
		roleContainerPwdFormData.left = new FormAttachment(txtRole, 2);
		roleContainerPwdFormData.top = new FormAttachment(edtLoginPwd, 4);
		roleContainerPwdFormData.right = new FormAttachment(100, -10);
		roleContainer.setLayoutData(roleContainerPwdFormData);

		FormData btnSubmitFormData = new FormData();
		btnSubmitFormData.left = new FormAttachment(45);
		btnSubmitFormData.top = new FormAttachment(roleContainer, 4);
		btnSubmit.setLayoutData(btnSubmitFormData);

		FormData btnResetFormData = new FormData();
		btnResetFormData.left = new FormAttachment(btnSubmit, 10);
		btnResetFormData.top = new FormAttachment(roleContainer, 4);
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
		sysUser.setName(edtName.getText().trim());
		sysUser.setPhone(edtPhone.getText().trim());
		sysUser.setEmail(edtEmail.getText().trim());
		sysUser.setSex(userSex.getCode().toString());
		sysUser.setLoginName(edtLoginName.getText().trim());
		sysUser.setLoginPwd(MD5Util.MD5Encode(edtLoginPwd.getText().trim()));
		sysUser.setUserRoleIds(selectRuleId);

		if (StringUtil.isBlank(sysUser.getName())) {
			new MsgBox(this, "姓名不能为空").open();
			return;
		}

		if (StringUtil.isBlank(sysUser.getPhone())) {
			new MsgBox(this, "手机号码不能为空").open();
			return;
		}

		if (StringUtil.isBlank(sysUser.getLoginName())) {
			new MsgBox(this, "登录名不能为空").open();
			return;
		}

		if (StringUtil.isBlank(sysUser.getLoginPwd())) {
			new MsgBox(this, "登录密码不能为空").open();
			return;
		}

		try {
			HttpUtil.postJsonString("/panel/profile/sysuser/save.json", JsonUtil.toJson(sysUser));
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
		edtLoginName.setText("");
		edtLoginPwd.setText("");
	}

	private void fillValue() {
		JsonNode result = null;

		try {
			if (id == null) {
				sysUser = new SysUser();
			} else {

				result = HttpUtil.get("/panel/profile/sysuser/getbyid.json", new HttpParam("id", id));
				sysUser = JsonUtil.jsonNodeToObject(result, SysUser.class);
				edtName.setText(sysUser.getName());
				edtPhone.setText(sysUser.getPhone());
				EnumUserSex enumSex = EnumUserSex.getEnum(Integer.valueOf(sysUser.getSex()));
				switch (enumSex) {
				case MAN:
					radioMen.setSelection(true);
					break;
				default:
					radioWomen.setSelection(true);
					break;
				}
				edtEmail.setText(sysUser.getEmail());
				edtLoginName.setText(sysUser.getLoginName());
				edtLoginPwd.setText(sysUser.getLoginPwd());

			}

			List<SysRole> roleResult = new ArrayList<SysRole>();
			result = HttpUtil.get("/panel/profile/sysrole/listuserrole.json", new HttpParam("userId", id));
			for (JsonNode node : result) {
				roleResult.add(JsonUtil.jsonNodeToObject(node, SysRole.class));
			}
			fillRoleCheck(roleResult);
		} catch (Exception e) {
			new MsgBox(App.getMainWindow(), e.getMessage()).open();
		}

	}

	private void fillRoleCheck(List<SysRole> listSysRole) {
		if (CollectionUtils.isEmpty(listSysRole)) {
			return;
		}
		SelectionAdapter listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				SysRole sysRole = (SysRole) e.widget.getData();
				if (sysRole.getSelected()) {
					sysRole.setSelected(false);
					if (selectRuleId.contains(sysRole.getId())) {
						selectRuleId.remove(sysRole.getId());
					}
				} else {
					sysRole.setSelected(true);
					selectRuleId.add(sysRole.getId());
				}
			}
		};
		Button btnCheck = null;
		for (SysRole sysRole : listSysRole) {
			btnCheck = new Button(roleContainer, SWT.CHECK);
			btnCheck.setText(sysRole.getDescription());
			btnCheck.setData(sysRole);
			btnCheck.addSelectionListener(listener);
			if (sysRole.getSelected()) {
				btnCheck.setSelection(true);
				selectRuleId.add(sysRole.getId());
			}

		}

	}

}
