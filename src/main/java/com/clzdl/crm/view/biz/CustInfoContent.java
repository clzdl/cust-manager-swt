package com.clzdl.crm.view.biz;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.clzdl.crm.view.common.TablePager;

public class CustInfoContent extends Composite {
	private Table custInfoTable;
	private TablePager pager;

	public CustInfoContent(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		custInfoTable = new Table(this, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		custInfoTable.setHeaderVisible(true);
		custInfoTable.setLinesVisible(true);
		TableColumn colId = new TableColumn(custInfoTable, SWT.NONE);
		colId.setText("id");
		colId.setWidth(50);

		TableColumn colName = new TableColumn(custInfoTable, SWT.NONE);
		colName.setText("姓名");
		colName.setWidth(100);

		TableColumn colSex = new TableColumn(custInfoTable, SWT.NONE);
		colSex.setText("性别");
		colSex.setWidth(50);

		TableColumn colPhone = new TableColumn(custInfoTable, SWT.NONE);
		colPhone.setText("电话");
		colPhone.setWidth(100);

		pager = new TablePager(this, SWT.NONE);

		FormData tabFormData = new FormData();
		tabFormData.left = new FormAttachment(0);
		tabFormData.top = new FormAttachment(0);
		tabFormData.right = new FormAttachment(100);
		tabFormData.bottom = new FormAttachment(pager);
		custInfoTable.setLayoutData(tabFormData);

		FormData pagerFormData = new FormData();
		pagerFormData.left = new FormAttachment(0);
		pagerFormData.top = new FormAttachment(100, -40);
		pagerFormData.right = new FormAttachment(100);
		pagerFormData.bottom = new FormAttachment(100);
		pager.setLayoutData(pagerFormData);
	}
}
