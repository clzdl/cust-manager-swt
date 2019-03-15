package com.clzdl.crm.view.biz;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.clzdl.crm.App;
import com.clzdl.crm.common.persistence.entity.CmCustInfo;
import com.clzdl.crm.view.common.LoadingDialog;
import com.clzdl.crm.view.common.LoadingDialog.TaskLoading;
import com.clzdl.crm.view.common.TablePager;
import com.clzdl.crm.view.common.TablePager.PagerOperation;

public class CustInfoContent extends Composite {
	private final String title = "客户信息";
	private Table table;
	private TablePager pager;

	public CustInfoContent(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		table = new Table(this, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setHeaderForeground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
		TableColumn colId = new TableColumn(table, SWT.NONE);
		colId.setText("id");
		colId.setWidth(50);
		colId.setAlignment(SWT.CENTER);

		TableColumn colName = new TableColumn(table, SWT.NONE);
		colName.setText("姓名");
		colName.setWidth(150);
		colName.setAlignment(SWT.CENTER);

		TableColumn colSex = new TableColumn(table, SWT.NONE);
		colSex.setText("性别");
		colSex.setWidth(50);
		colSex.setAlignment(SWT.CENTER);

		TableColumn colPhone = new TableColumn(table, SWT.NONE);
		colPhone.setText("电话");
		colPhone.setWidth(150);
		colPhone.setAlignment(SWT.CENTER);

		TableColumn remark = new TableColumn(table, SWT.NONE);
		remark.setText("备注");
		remark.setWidth(200);
		remark.setAlignment(SWT.CENTER);

		pager = new TablePager(this, SWT.NONE, new PagerOperation() {
			@Override
			public Integer refresh(final Integer pageNo, final Integer pageSize) {
				final List<CmCustInfo> list = new ArrayList<CmCustInfo>();
				LoadingDialog loading = new LoadingDialog(App.mainWindow, App.loadingImages);
				loading.start(new TaskLoading() {
					@Override
					public void doing() {
						CmCustInfo cust = null;
						Integer serialNo = (pageNo - 1) * pageSize;
						for (int i = 0; i < pageSize; i++) {
							cust = new CmCustInfo();
							cust.setId(serialNo);
							cust.setName("name " + serialNo);
							cust.setSex(0);
							cust.setPhone("phone " + serialNo);
							++serialNo;
							list.add(cust);
						}

						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

				table.removeAll();
				TableItem tableItem = null;
				Integer pos = 0;
				for (CmCustInfo cust1 : list) {
					tableItem = new TableItem(table, SWT.NONE);
					if (pos++ % 2 == 1) {
						tableItem.setBackground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
					}
					tableItem.setText(new String[] { cust1.getId().toString(), cust1.getName(),
							cust1.getSex().toString(), cust1.getPhone() });
				}

				return 101;
			}
		});

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

	public String getTitle() {
		return title;
	}

}
