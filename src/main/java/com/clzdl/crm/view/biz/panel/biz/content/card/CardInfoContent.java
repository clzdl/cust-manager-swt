package com.clzdl.crm.view.biz.panel.biz.content.card;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.clzdl.crm.App;
import com.clzdl.crm.view.common.AbstractPanelRightContent;
import com.clzdl.crm.view.common.TablePager;
import com.clzdl.crm.view.common.TablePager.PagerOperation;

public class CardInfoContent extends AbstractPanelRightContent {
	private final static String title = "会员卡信息";
	private Table table;
	private TablePager pager;

	public CardInfoContent(Composite parent, int style) {
		super(parent, style, title);
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

		TableColumn colSex = new TableColumn(table, SWT.NONE);
		colSex.setText("类型");
		colSex.setWidth(50);
		colSex.setAlignment(SWT.CENTER);

		TableColumn colPhone = new TableColumn(table, SWT.NONE);
		colPhone.setText("价格");
		colPhone.setWidth(50);
		colPhone.setAlignment(SWT.CENTER);

		TableColumn effDate = new TableColumn(table, SWT.NONE);
		effDate.setText("开始时间");
		effDate.setWidth(100);
		effDate.setAlignment(SWT.CENTER);

		TableColumn expDate = new TableColumn(table, SWT.NONE);
		expDate.setText("结束时间");
		expDate.setWidth(100);
		expDate.setAlignment(SWT.CENTER);

		TableColumn remark = new TableColumn(table, SWT.NONE);
		remark.setText("备注");
		remark.setWidth(200);
		remark.setAlignment(SWT.CENTER);

		pager = new TablePager(this, SWT.NONE, new PagerOperation() {
			@Override
			public Integer refresh(Integer pageNo, Integer pageSize) {

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
}
