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

import com.clzdl.crm.common.persistence.entity.CmCustInfo;
import com.clzdl.crm.view.common.TablePager;
import com.clzdl.crm.view.common.TablePager.PagerOperation;

public class CustInfoContent extends Composite {
	private Table custInfoTable;
	private TablePager pager;

	public CustInfoContent(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		custInfoTable = new Table(this, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		custInfoTable.setHeaderVisible(true);
		custInfoTable.setLinesVisible(true);
		custInfoTable.setHeaderForeground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
		TableColumn colId = new TableColumn(custInfoTable, SWT.NONE);
		colId.setText("id");
		colId.setWidth(50);
		colId.setAlignment(SWT.CENTER);

		TableColumn colName = new TableColumn(custInfoTable, SWT.NONE);
		colName.setText("姓名");
		colName.setWidth(150);
		colName.setAlignment(SWT.CENTER);

		TableColumn colSex = new TableColumn(custInfoTable, SWT.NONE);
		colSex.setText("性别");
		colSex.setWidth(50);
		colSex.setAlignment(SWT.CENTER);

		TableColumn colPhone = new TableColumn(custInfoTable, SWT.NONE);
		colPhone.setText("电话");
		colPhone.setWidth(150);
		colPhone.setAlignment(SWT.CENTER);

		TableColumn remark = new TableColumn(custInfoTable, SWT.NONE);
		remark.setText("备注");
		remark.setWidth(200);
		remark.setAlignment(SWT.CENTER);

		pager = new TablePager(this, SWT.NONE, new PagerOperation() {
			@Override
			public Integer refresh(Integer pageNo, Integer pageSize) {
				CmCustInfo cust = null;
				final List<CmCustInfo> list = new ArrayList<CmCustInfo>();
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

				custInfoTable.removeAll();
				TableItem tableItem = null;
				Integer pos = 0;
				for (CmCustInfo cust1 : list) {
					tableItem = new TableItem(custInfoTable, SWT.NONE);
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
		custInfoTable.setLayoutData(tabFormData);

		FormData pagerFormData = new FormData();
		pagerFormData.left = new FormAttachment(0);
		pagerFormData.top = new FormAttachment(100, -60);
		pagerFormData.right = new FormAttachment(100);
		pagerFormData.bottom = new FormAttachment(100);
		pager.setLayoutData(pagerFormData);
	}
}
