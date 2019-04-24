package com.clzdl.crm.cli.biz.panel.profile.content.sysmenu;

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
import com.clzdl.crm.cli.common.AbstractComposite;
import com.clzdl.crm.cli.common.LoadingDialog;
import com.clzdl.crm.cli.common.MsgBox;
import com.clzdl.crm.cli.common.TablePager;
import com.clzdl.crm.cli.common.LoadingDialog.TaskLoading;
import com.clzdl.crm.cli.common.TablePager.PagerOperation;
import com.clzdl.crm.common.enums.EnumSysPermissionProfile;
import com.clzdl.crm.srv.persistence.entity.SysMenu;
import com.clzdl.crm.utils.HttpUtil;
import com.clzdl.crm.utils.HttpUtil.HttpParam;
import com.fasterxml.jackson.databind.JsonNode;
import com.framework.common.util.json.JsonUtil;
import com.framework.mybatis.page.PageModel;

public class SysMenuContent extends AbstractComposite {
	private final static String title = "系统菜单信息";
	private Table table;
	private TablePager pager;
	private SysMenu searchCondition = new SysMenu();

	public SysMenuContent(Composite parent, int style) {
		super(parent, style, title, EnumSysPermissionProfile.SYSMENU);
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

		TableColumn colPermission = new TableColumn(table, SWT.NONE);
		colPermission.setText("权限");
		colPermission.setWidth(150);
		colPermission.setAlignment(SWT.CENTER);

		TableColumn colParent = new TableColumn(table, SWT.NONE);
		colParent.setText("父节点");
		colParent.setWidth(150);
		colParent.setAlignment(SWT.CENTER);

		TableColumn colType = new TableColumn(table, SWT.NONE);
		colType.setText("类型");
		colType.setWidth(150);
		colType.setAlignment(SWT.CENTER);

		TableColumn createTime = new TableColumn(table, SWT.NONE);
		createTime.setText("创建时间");
		createTime.setWidth(200);
		createTime.setAlignment(SWT.CENTER);

		pager = new TablePager(this, SWT.NONE, new PagerOperation() {
			@Override
			public Integer refresh(final Integer pageNo, final Integer pageSize) {
				final PageModel<SysMenu> pm = new PageModel<SysMenu>();
				LoadingDialog loading = new LoadingDialog(App.getMainWindow(), App.getLoadingImgages());
				loading.start(new TaskLoading() {
					@Override
					public void doing() {
						try {
							JsonNode result = HttpUtil.postJsonObject("/panel/profile/sysmenu/list.json",
									searchCondition);
							for (JsonNode node : result.get("list")) {
								pm.getList().add(JsonUtil.jsonNodeToObject(node, SysMenu.class));
							}
						} catch (Exception e) {
							new MsgBox(App.getMainWindow(), e.getMessage()).open();
						}
					}
				});

				table.removeAll();
				TableItem tableItem = null;
				Integer pos = 0;
				for (SysMenu menu : pm.getList()) {
					tableItem = new TableItem(table, SWT.NONE);
					if (pos++ % 2 == 1) {
						tableItem.setBackground(App.getTabItemBackground());
					}
					tableItem.setText(new String[] { menu.getId().toString(), menu.getName(), menu.getHref(),
							menu.getParentId().toString(), menu.getMenuTypeOutput(), menu.getCreateTimeOutput() });
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
				new SysMenuEditDialog(App.getMainWindow(), null);
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
				new SysMenuEditDialog(App.getMainWindow(), Long.valueOf(item[0].getText(Constants.ID_INDEX).trim()));
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
					HttpUtil.get("/panel/profile/sysmenu/deletebyid.json",
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
