package com.clzdl.crm.view.biz;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class CardInfoContent extends Composite {
	private Table custInfoTable;

	public CardInfoContent(Composite parent, int style) {
		super(parent, style);
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		setLayout(layout);
		custInfoTable = new Table(this, SWT.BORDER);
		custInfoTable.setHeaderVisible(true);
		custInfoTable.setLinesVisible(true);
		TableColumn tableColumn1 = new TableColumn(custInfoTable, SWT.NONE);
		tableColumn1.setText("id");

		TableColumn tableColumn2 = new TableColumn(custInfoTable, SWT.NONE);
		tableColumn2.setText("name");
		custInfoTable.pack();
	}
}
