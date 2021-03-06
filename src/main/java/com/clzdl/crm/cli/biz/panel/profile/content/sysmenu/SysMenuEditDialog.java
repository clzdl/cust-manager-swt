package com.clzdl.crm.cli.biz.panel.profile.content.sysmenu;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clzdl.crm.App;
import com.clzdl.crm.cli.common.MsgBox;
import com.clzdl.crm.cli.common.tree.SelectTree;
import com.clzdl.crm.cli.common.tree.TreeNodeData;
import com.clzdl.crm.common.enums.EnumSysMenuType;
import com.clzdl.crm.srv.persistence.entity.SysMenu;
import com.clzdl.crm.utils.HttpUtil;
import com.clzdl.crm.utils.HttpUtil.HttpParam;
import com.fasterxml.jackson.databind.JsonNode;
import com.framework.common.util.json.JsonUtil;
import com.framework.common.util.string.StringUtil;

public class SysMenuEditDialog extends Shell {
	private final static Logger _logger = LoggerFactory.getLogger(SysMenuEditDialog.class);
	private final static Long _PARENT_ID = 0L;
	private Long id;
	private Label txtName;
	private Text edtName;
	private Label txtPermission;
	private Text edtPermission;
	private Label txtParent;
	private SelectTree stParent;
	private Label txtMenuType;
	private CCombo cboMenuType;
	private Button btnSubmit;
	private Button btnReset;
	private EnumSysMenuType sysMenuType = EnumSysMenuType.MENU;

	private SysMenu sysMenu = null;

	public SysMenuEditDialog(Shell parent, Long id) {
		super(parent, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		this.id = id;
		setSize(300, 200);
		setText("修改");
		center(parent);
		setLayout(new FormLayout());
		/// 名称
		txtName = new Label(this, SWT.NONE);
		txtName.setText("名称");
		edtName = new Text(this, SWT.BORDER);

		/// 权限
		txtPermission = new Label(this, SWT.NONE);
		txtPermission.setText("权限");
		edtPermission = new Text(this, SWT.BORDER);

		/// 父节点
		txtParent = new Label(this, SWT.NONE);
		txtParent.setText("父节点");
		stParent = new SelectTree(this, SWT.BORDER, _PARENT_ID);

		/// 菜单类型
		txtMenuType = new Label(this, SWT.NONE);
		txtMenuType.setText("类型");

		cboMenuType = new CCombo(this, SWT.FLAT | SWT.BORDER | SWT.READ_ONLY);
		Point point = cboMenuType.getSize();
		cboMenuType.setSize(200, point.y);
		cboMenuType.add(EnumSysMenuType.MENU.getName(), EnumSysMenuType.MENU.getCode());
		cboMenuType.add(EnumSysMenuType.FUNCTION.getName(), EnumSysMenuType.FUNCTION.getCode());

		cboMenuType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				sysMenuType = EnumSysMenuType.getEnum(cboMenuType.getSelectionIndex());
			}
		});

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

		FormData txtPermissionFormData = new FormData();
		txtPermissionFormData.top = new FormAttachment(txtName, 4);
		txtPermissionFormData.right = new FormAttachment(15);
		txtPermission.setLayoutData(txtPermissionFormData);

		FormData edtPermissionFormData = new FormData();
		edtPermissionFormData.left = new FormAttachment(txtPermission, 2);
		edtPermissionFormData.top = new FormAttachment(edtName, 4);
		edtPermissionFormData.right = new FormAttachment(100, -10);
		edtPermission.setLayoutData(edtPermissionFormData);

		FormData txtParentFormData = new FormData();
		txtParentFormData.top = new FormAttachment(txtPermission, 4);
		txtParentFormData.right = new FormAttachment(15);
		txtParent.setLayoutData(txtParentFormData);

		FormData edtParentFormData = new FormData();
		edtParentFormData.left = new FormAttachment(txtParent, 2);
		edtParentFormData.top = new FormAttachment(edtPermission, 4);
		edtParentFormData.right = new FormAttachment(100, -10);
		stParent.setLayoutData(edtParentFormData);

		FormData txtMenuTypeFormData = new FormData();
		txtMenuTypeFormData.top = new FormAttachment(txtParent, 4);
		txtMenuTypeFormData.right = new FormAttachment(15);
		txtMenuType.setLayoutData(txtMenuTypeFormData);

		FormData btnMenuTypeFormData = new FormData();
		btnMenuTypeFormData.left = new FormAttachment(txtMenuType, 2);
		btnMenuTypeFormData.top = new FormAttachment(stParent, 4);
		btnMenuTypeFormData.right = new FormAttachment(100, -10);
		cboMenuType.setLayoutData(btnMenuTypeFormData);

		FormData btnSubmitFormData = new FormData();
		btnSubmitFormData.left = new FormAttachment(45);
		btnSubmitFormData.top = new FormAttachment(100, -30);
		btnSubmit.setLayoutData(btnSubmitFormData);

		FormData btnResetFormData = new FormData();
		btnResetFormData.left = new FormAttachment(btnSubmit, 10);
		btnResetFormData.top = new FormAttachment(100, -30);
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
		sysMenu.setName(edtName.getText().trim());
		sysMenu.setHref(edtPermission.getText().trim());
		Object parentId = stParent.getCodeValue();
		sysMenu.setParentId(parentId != null ? Long.valueOf(parentId.toString()) : 0L);
		sysMenu.setMenuType(sysMenuType.getCode().byteValue());
		if (StringUtil.isBlank(sysMenu.getName())) {
			new MsgBox(this, "名称不能为空").open();
			return;
		}

		if (StringUtil.isBlank(sysMenu.getHref())) {
			new MsgBox(this, "权限不能为空").open();
			return;
		}

		try {
			HttpUtil.postJsonObject("/panel/profile/sysmenu/save.json", sysMenu);
			close();
		} catch (Exception ex) {
			new MsgBox(App.getMainWindow(), ex.getMessage()).open();
		}

	}

	private void reset() {
		edtName.setText("");
		edtPermission.setText("");
	}

	private void fillValue() {
		try {
			List<SysMenu> allMenus = new ArrayList<SysMenu>();
			JsonNode result = HttpUtil.get("/panel/profile/sysmenu/listall.json");
			for (JsonNode node : result) {
				allMenus.add(JsonUtil.jsonNodeToObject(node, SysMenu.class));
			}

			if (id == null) {
				sysMenu = new SysMenu();
			} else {
				result = HttpUtil.get("/panel/profile/sysmenu/getbyid.json", new HttpParam("id", id));
				sysMenu = JsonUtil.jsonNodeToObject(result, SysMenu.class);
				edtName.setText(sysMenu.getName());
				edtPermission.setText(sysMenu.getHref());
				stParent.setDefault(sysMenu.getParentId());
				cboMenuType.select(sysMenu.getMenuType());
			}
			stParent.setDataList(buildSelTreeList(allMenus, sysMenu.getParentId()));
		} catch (Exception e) {
			new MsgBox(App.getMainWindow(), e.getMessage()).open();
		}

	}

	private List<TreeNodeData> buildSelTreeList(List<SysMenu> list, Long defaultCode) {
		List<TreeNodeData> result = new ArrayList<TreeNodeData>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (SysMenu menu : list) {
				result.add(new TreeNodeData(menu.getId(), menu.getParentId(), menu.getName(),
						defaultCode == menu.getId() ? true : false));
			}
		}

		return result;
	}

}
