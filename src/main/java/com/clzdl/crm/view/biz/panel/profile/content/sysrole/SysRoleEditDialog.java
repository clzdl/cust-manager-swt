package com.clzdl.crm.view.biz.panel.profile.content.sysrole;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.util.string.StringUtil;
import com.clzdl.crm.common.persistence.entity.SysRole;
import com.clzdl.crm.controller.sys.SysRoleController;
import com.clzdl.crm.dto.ResultDTO;
import com.clzdl.crm.view.common.MsgBox;
import com.clzdl.crm.view.common.tree.AuthTree;
import com.clzdl.crm.view.common.tree.TreeNodeData;

public class SysRoleEditDialog extends Shell {
	private final static Logger _logger = LoggerFactory.getLogger(SysRoleEditDialog.class);
	private Long id;
	private Label txtName;
	private Text edtName;
	private Label txtDesc;
	private Text edtDesc;
	private Label txtAuth;
	private AuthTree authTree;
	private Button btnSubmit;
	private Button btnReset;

	private SysRole sysRole = null;

	public SysRoleEditDialog(Shell parent, Long id) {
		super(parent, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		this.id = id;
		setSize(400, 300);
		setText("修改");
		center(parent);
		setLayout(new FormLayout());
		/// 名称
		txtName = new Label(this, SWT.NONE);
		txtName.setText("姓名");
		edtName = new Text(this, SWT.BORDER);

		/// 简要描述
		txtDesc = new Label(this, SWT.NONE);
		txtDesc.setText("描述");
		edtDesc = new Text(this, SWT.BORDER);

		txtAuth = new Label(this, SWT.NONE);
		txtAuth.setText("权限");
		authTree = new AuthTree(this, SWT.NONE, 0L);

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

		FormData txtDescFormData = new FormData();
		txtDescFormData.top = new FormAttachment(txtName, 4);
		txtDescFormData.right = new FormAttachment(15);
		txtDesc.setLayoutData(txtDescFormData);

		FormData edtDescFormData = new FormData();
		edtDescFormData.left = new FormAttachment(txtDesc, 2);
		edtDescFormData.top = new FormAttachment(edtName, 4);
		edtDescFormData.right = new FormAttachment(100, -10);
		edtDesc.setLayoutData(edtDescFormData);

		FormData txtAuthFormData = new FormData();
		txtAuthFormData.top = new FormAttachment(txtDesc, 4);
		txtAuthFormData.right = new FormAttachment(15);
		txtAuth.setLayoutData(txtAuthFormData);

		FormData authTreeFormData = new FormData();
		authTreeFormData.left = new FormAttachment(txtAuth, 2);
		authTreeFormData.top = new FormAttachment(edtDesc, 4);
		authTreeFormData.right = new FormAttachment(100, -10);
		authTreeFormData.bottom = new FormAttachment(btnSubmit, 4);
		authTree.setLayoutData(authTreeFormData);

		FormData btnSubmitFormData = new FormData();
		btnSubmitFormData.left = new FormAttachment(45);
		btnSubmitFormData.top = new FormAttachment(100, -40);
		btnSubmit.setLayoutData(btnSubmitFormData);

		FormData btnResetFormData = new FormData();
		btnResetFormData.left = new FormAttachment(btnSubmit, 10);
		btnResetFormData.top = new FormAttachment(100, -40);
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
		sysRole.setRoleName(edtName.getText().trim());
		sysRole.setDescription(edtDesc.getText().trim());

		List<TreeNodeData> list = authTree.getSelections();
		List<Long> menuIds = new ArrayList<Long>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (TreeNodeData data : list) {
				menuIds.add(Long.valueOf(data.getCode().toString()));
			}
		}
		sysRole.setMenuIds(menuIds);

		if (StringUtil.isBlank(sysRole.getRoleName())) {
			new MsgBox(this, "名称不能为空").open();
			return;
		}

		if (StringUtil.isBlank(sysRole.getDescription())) {
			new MsgBox(this, "描述不能为空").open();
			return;
		}

		ResultDTO<?> result = SysRoleController.getBean().save(sysRole);
		if (result.getCode() != ResultDTO.SUCCESS_CODE) {
			MessageBox box = new MessageBox(this);
			box.setMessage(result.getErrMsg());
			box.open();
		} else {
			close();
		}

	}

	private void reset() {
		edtName.setText("");
		edtDesc.setText("");
	}

	private void fillValue() {

		if (id == null) {
			sysRole = new SysRole();
		} else {
			ResultDTO<SysRole> result = SysRoleController.getBean().getById(id);
			if (result.getCode() != ResultDTO.SUCCESS_CODE) {
				new MsgBox(this, result.getErrMsg()).open();
			} else {
				sysRole = result.getData();
				edtName.setText(sysRole.getRoleName());
				edtDesc.setText(sysRole.getDescription());
			}
		}
		ResultDTO<List<TreeNodeData>> authReuslt = SysRoleController.getBean().listRoleAuth(sysRole.getId());
		authTree.fillTree(authReuslt.getData());
	}

}
