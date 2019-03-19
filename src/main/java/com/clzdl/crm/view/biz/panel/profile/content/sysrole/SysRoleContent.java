package com.clzdl.crm.view.biz.panel.profile.content.sysrole;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.base.mvc.page.PageModel;
import com.clzdl.crm.App;
import com.clzdl.crm.Constants;
import com.clzdl.crm.common.persistence.entity.SysRole;
import com.clzdl.crm.controller.sys.SysRoleController;
import com.clzdl.crm.dto.ResultDTO;
import com.clzdl.crm.view.common.AbstractPanelRightContent;
import com.clzdl.crm.view.common.LoadingDialog;
import com.clzdl.crm.view.common.LoadingDialog.TaskLoading;
import com.clzdl.crm.view.common.MsgBox;
import com.clzdl.crm.view.common.TablePager;
import com.clzdl.crm.view.common.TablePager.PagerOperation;

public class SysRoleContent extends AbstractPanelRightContent {
	private final static String title = "系统角色信息";
	private Table table;
	private TablePager pager;
	private SysRole searchCondition = new SysRole();

	public SysRoleContent(Composite parent, int style) {
		super(parent, style, title);
		table = new Table(this, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setHeaderForeground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
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
				LoadingDialog loading = new LoadingDialog(App.mainWindow, App.loadingImages);
				loading.start(new TaskLoading() {
					@Override
					public void doing() {
						ResultDTO<PageModel<SysRole>> result = SysRoleController.getBean().list(searchCondition, pageNo,
								pageSize);
						if (result.getCode() != ResultDTO.SUCCESS_CODE) {
							new MsgBox(App.mainWindow, result.getErrMsg()).open();
							return;

						} else {
							pm.setTotalRecords(result.getData().getTotalRecords());
							pm.setList(result.getData().getList());
						}
					}
				});

				table.removeAll();
				TableItem tableItem = null;
				Integer pos = 0;
				for (SysRole role : pm.getList()) {
					tableItem = new TableItem(table, SWT.NONE);
					if (pos++ % 2 == 1) {
						tableItem.setBackground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
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
				new SysRoleEditDialog(App.mainWindow, null);
				pager.refreshPage(false);
			}

		});

		MenuItem modifyItem = new MenuItem(popMenu, SWT.PUSH);
		modifyItem.setText("修改");
		modifyItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);
				TableItem item[] = table.getSelection();
				new SysRoleEditDialog(App.mainWindow, Long.valueOf(item[0].getText(Constants.ID_INDEX).trim()));
				pager.refreshPage(false);
			}
		});

		MenuItem deletItem = new MenuItem(popMenu, SWT.PUSH);
		deletItem.setText("删除");
		deletItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				if (SWT.CANCEL == new MsgBox(App.mainWindow, SWT.OK | SWT.CANCEL, "确认删除?").open()) {
					return;
				}
				TableItem item[] = table.getSelection();
				ResultDTO<?> result = SysRoleController.getBean()
						.deleteById(Long.valueOf(item[0].getText(Constants.ID_INDEX).trim()));
				if (result.getCode() != ResultDTO.SUCCESS_CODE) {
					new MsgBox(App.mainWindow, result.getErrMsg()).open();
				} else {
					pager.refreshPage(false);
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
