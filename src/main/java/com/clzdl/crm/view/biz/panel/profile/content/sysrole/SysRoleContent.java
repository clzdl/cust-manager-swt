package com.clzdl.crm.view.biz.panel.profile.content.sysrole;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.clzdl.crm.App;
import com.clzdl.crm.Constants;
import com.clzdl.crm.springboot.auth.EnumSysPermissionProfile;
import com.clzdl.crm.springboot.persistence.entity.SysRole;
import com.clzdl.crm.utils.HttpUtil;
import com.clzdl.crm.utils.HttpUtil.HttpParam;
import com.clzdl.crm.view.common.AbstractComposite;
import com.clzdl.crm.view.common.LoadingDialog;
import com.clzdl.crm.view.common.LoadingDialog.TaskLoading;
import com.clzdl.crm.view.common.MsgBox;
import com.clzdl.crm.view.common.TablePager;
import com.clzdl.crm.view.common.TablePager.PagerOperation;
import com.fasterxml.jackson.databind.JsonNode;
import com.framework.common.util.json.JsonUtil;
import com.framework.mybatis.page.PageModel;

public class SysRoleContent extends AbstractComposite {
	private final static String title = "系统角色信息";
	private Table table;
	private TablePager pager;
	private SysRole searchCondition = new SysRole();

	public SysRoleContent(Composite parent, int style) {
		super(parent, style, title, EnumSysPermissionProfile.SYSROLE);
		setLayout(new FormLayout());
		table = new Table(this, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setHeaderForeground(App.getTabHeaderForeground());
		TableColumn colId = new TableColumn(table, SWT.NONE);
		colId.setText("id");
		colId.setWidth(50);
		colId.setAlignment(SWT.CENTER);

		TableColumn colName = new TableColumn(table, SWT.NONE);
		colName.setText("名称");
		colName.setWidth(150);
		colName.setAlignment(SWT.CENTER);

		TableColumn colDesc = new TableColumn(table, SWT.NONE);
		colDesc.setText("描述");
		colDesc.setWidth(150);
		colDesc.setAlignment(SWT.CENTER);

		TableColumn createTime = new TableColumn(table, SWT.NONE);
		createTime.setText("创建时间");
		createTime.setWidth(200);
		createTime.setAlignment(SWT.CENTER);

		pager = new TablePager(this, SWT.NONE, new PagerOperation() {
			@Override
			public Integer refresh(final Integer pageNo, final Integer pageSize) {
				final PageModel<SysRole> pm = new PageModel<SysRole>();
				LoadingDialog loading = new LoadingDialog(App.getMainWindow(), App.getLoadingImgages());
				loading.start(new TaskLoading() {
					@Override
					public void doing() {
						try {
							JsonNode result = HttpUtil.postJsonObject("/panel/profile/sysrole/list.json",
									searchCondition);
							for (JsonNode node : result.get("list")) {
								pm.getList().add(JsonUtil.jsonNodeToObject(node, SysRole.class));
							}
						} catch (Exception e) {
							new MsgBox(App.getMainWindow(), e.getMessage()).open();
						}
					}
				});

				table.removeAll();
				TableItem tableItem = null;
				Integer pos = 0;
				for (SysRole role : pm.getList()) {
					tableItem = new TableItem(table, SWT.NONE);
					if (pos++ % 2 == 1) {
						tableItem.setBackground(App.getTabItemBackground());
					}
					tableItem.setText(new String[] { role.getId().toString(), role.getRoleName(), role.getDescription(),
							role.getCreateTimeOutput() });
				}

				return (int) pm.getTotalRecords();
			}
		});
		pager.refreshPage(true);
		///
		Menu popMenu = new Menu(parent);
		MenuItem addItem = new MenuItem(popMenu, SWT.PUSH);
		addItem.setText("增加");
		addItem.addSelectionListener(new SelectionAdapter() {
			/// 新增
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				new SysRoleEditDialog(App.getMainWindow(), null);
				pager.refreshPage(false);
			}

		});

		MenuItem modifyItem = new MenuItem(popMenu, SWT.PUSH);
		modifyItem.setText("修改");
		modifyItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				TableItem item[] = table.getSelection();
				if (0 >= item.length) {
					return;
				}
				new SysRoleEditDialog(App.getMainWindow(), Long.valueOf(item[0].getText(Constants.ID_INDEX).trim()));
				pager.refreshPage(false);
			}
		});

		MenuItem deletItem = new MenuItem(popMenu, SWT.PUSH);
		deletItem.setText("删除");
		deletItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				if (SWT.CANCEL == new MsgBox(App.getMainWindow(), SWT.OK | SWT.CANCEL, "确认删除?").open()) {
					return;
				}
				TableItem item[] = table.getSelection();

				try {
					HttpUtil.get("/panel/profile/sysrole/deletebyid.json",
							new HttpParam("id", Long.valueOf(item[0].getText(Constants.ID_INDEX).trim())));
					pager.refreshPage(false);
				} catch (Exception ex) {
					new MsgBox(App.getMainWindow(), ex.getMessage()).open();
				}
			}
		});

		table.setMenu(popMenu);

		FormData tabFormData = new FormData();
		tabFormData.left = new FormAttachment(0);
		tabFormData.top = new FormAttachment(0);
		tabFormData.right = new FormAttachment(100);
		tabFormData.bottom = new FormAttachment(pager);
		table.setLayoutData(tabFormData);

		FormData pagerFormData = new FormData();
		pagerFormData.left = new FormAttachment(0);
		pagerFormData.top = new FormAttachment(100, -60);
		pagerFormData.right = new FormAttachment(100);
		pagerFormData.bottom = new FormAttachment(100);
		pager.setLayoutData(pagerFormData);
	}
}
