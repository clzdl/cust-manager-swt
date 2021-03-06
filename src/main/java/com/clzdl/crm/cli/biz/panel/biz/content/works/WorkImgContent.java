package com.clzdl.crm.cli.biz.panel.biz.content.works;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
import com.clzdl.crm.cli.common.ImgViewDialog;
import com.clzdl.crm.cli.common.LoadingDialog;
import com.clzdl.crm.cli.common.MsgBox;
import com.clzdl.crm.cli.common.TablePager;
import com.clzdl.crm.cli.common.LoadingDialog.TaskLoading;
import com.clzdl.crm.cli.common.TablePager.PagerOperation;
import com.clzdl.crm.common.enums.EnumSysPermissionProfile;
import com.clzdl.crm.srv.persistence.entity.CmWorksImages;
import com.clzdl.crm.utils.HttpUtil;
import com.clzdl.crm.utils.HttpUtil.HttpParam;
import com.fasterxml.jackson.databind.JsonNode;
import com.framework.common.util.json.JsonUtil;
import com.framework.mybatis.page.PageModel;

public class WorkImgContent extends AbstractComposite {
	private final static String title = "用户作品";
	private final static Integer imgUrlIndex = 2;
	private Table table;
	private TablePager pager;
	private CmWorksImages searchCondition = new CmWorksImages();

	public WorkImgContent(Composite parent, int style) {
		super(parent, style, title, EnumSysPermissionProfile.WORKIMG);
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
		colName.setWidth(100);
		colName.setAlignment(SWT.CENTER);

		TableColumn colSex = new TableColumn(table, SWT.NONE);
		colSex.setText("图片地址");
		colSex.setWidth(300);
		colSex.setAlignment(SWT.CENTER);

		TableColumn createDate = new TableColumn(table, SWT.NONE);
		createDate.setText("创建时间");
		createDate.setWidth(100);
		createDate.setAlignment(SWT.CENTER);

		TableColumn remark = new TableColumn(table, SWT.NONE);
		remark.setText("备注");
		remark.setWidth(200);
		remark.setAlignment(SWT.CENTER);

		pager = new TablePager(this, SWT.NONE, new PagerOperation() {
			@Override
			public Integer refresh(Integer pageNo, Integer pageSize) {
				final PageModel<CmWorksImages> pm = new PageModel<CmWorksImages>();
				LoadingDialog loading = new LoadingDialog(App.getMainWindow(), App.getLoadingImgages());
				loading.start(new TaskLoading() {
					@Override
					public void doing() {
						try {
							JsonNode result = HttpUtil.postJsonObject("/panel/biz/workimg/list.json", searchCondition);
							for (JsonNode node : result.get("list")) {
								pm.getList().add(JsonUtil.jsonNodeToObject(node, CmWorksImages.class));
							}
						} catch (Exception e) {
							new MsgBox(App.getMainWindow(), e.getMessage()).open();
						}
					}
				});

				table.removeAll();
				TableItem tableItem = null;
				Integer pos = 0;
				for (CmWorksImages img : pm.getList()) {
					tableItem = new TableItem(table, SWT.NONE);
					if (pos++ % 2 == 1) {
						tableItem.setBackground(App.getTabItemBackground());
					}
					tableItem.setText(new String[] { img.getId().toString(), img.getName(), img.getImgUrl(),
							img.getCreateTimeOutput() });
				}

				return (int) (pm.getTotalRecords());
			}
		});
		pager.refreshPage(true);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				super.mouseDoubleClick(e);
				TableItem item[] = table.getSelection();
				if (0 >= item.length) {
					return;
				}
				try {
					new ImgViewDialog(getDisplay(), item[0].getText(imgUrlIndex).trim());
				} catch (Exception e1) {
					new MsgBox(getShell(), e1.getMessage()).open();
				}
			}
		});

		///
		Menu popMenu = new Menu(parent);
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
					HttpUtil.get("/panel/biz/workimg/deletebyid.json",
							new HttpParam("id", item[0].getText(Constants.ID_INDEX).trim()));
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
