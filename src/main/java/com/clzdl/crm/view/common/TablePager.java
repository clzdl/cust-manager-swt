package com.clzdl.crm.view.common;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class TablePager extends Composite {
	// 页展现量
	private Integer pageSize = 50;
	// 当前页号
	private Integer pageNo = 1;
	// 总记录数
	private Integer count = 0;
	private Button btnFirst;
	private Button btnPrev;
	private Button btnNext;
	private Button btnLast;
	private Label txtCount;
	private Label txtPage; /// 当前页号显示
	///
	private PagerOperation pagerOperation;

	public static interface PagerOperation {
		/// 刷新,返回记录总数
		Integer refresh(final Integer pageNo, final Integer pageSize);
	}

	public TablePager(Composite parent, int style, PagerOperation pagerOperation) {
		super(parent, style);
		this.pagerOperation = pagerOperation;
		createContent(parent, style);

	}

	public TablePager(Composite parent, int style, PagerOperation pagerOperation, Integer pageNo, Integer pageSize) {
		super(parent, style);
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.pagerOperation = pagerOperation;
		createContent(parent, style);
	}

	public void refreshCurrentPage() {
		count = pagerOperation.refresh(pageNo, pageSize);
		refreshButton();
	}

	private void createContent(Composite parent, int style) {
		setLayout(new FormLayout());
		txtCount = new Label(this, SWT.NONE);
		txtPage = new Label(this, SWT.NONE);

		btnFirst = new Button(this, SWT.PUSH);
		btnFirst.setText("<");
		btnFirst.setToolTipText("首页");

		btnPrev = new Button(this, SWT.PUSH);
		btnPrev.setText("<<");
		btnPrev.setToolTipText("上一页");

		btnNext = new Button(this, SWT.PUSH);
		btnNext.setText(">>");
		btnNext.setToolTipText("下一页");

		btnLast = new Button(this, SWT.PUSH);
		btnLast.setText(">");
		btnLast.setToolTipText("尾页");

		FormData txtCountFormData = new FormData();
		txtCountFormData.right = new FormAttachment(btnFirst, -20);
		txtCount.setLayoutData(txtCountFormData);

		FormData btnFirstFormData = new FormData();
		btnFirstFormData.right = new FormAttachment(btnPrev);
		btnFirst.setLayoutData(btnFirstFormData);

		FormData btnPrevFormData = new FormData();
		btnPrevFormData.right = new FormAttachment(txtPage, -10);
		btnPrev.setLayoutData(btnPrevFormData);

		FormData txtPageFormData = new FormData();
		txtPageFormData.right = new FormAttachment(btnNext, -10);
		txtPage.setLayoutData(txtPageFormData);

		FormData btnNextFormData = new FormData();
		btnNextFormData.right = new FormAttachment(btnLast);
		btnNext.setLayoutData(btnNextFormData);

		FormData btnLastFormData = new FormData();
		btnLastFormData.left = new FormAttachment(70);
		btnLast.setLayoutData(btnLastFormData);

		SelectionAdapter firstSelectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				pageNo = 1;
				count = pagerOperation.refresh(pageNo, pageSize);
				refreshButton();
				super.widgetSelected(e);
			}
		};

		SelectionAdapter lastSelectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				pageNo = getLastPageNo();
				count = pagerOperation.refresh(pageNo, pageSize);
				refreshButton();
				super.widgetSelected(e);
			}
		};

		SelectionAdapter prevSelectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				--pageNo;
				count = pagerOperation.refresh(pageNo, pageSize);
				refreshButton();
				super.widgetSelected(e);
			}
		};

		SelectionAdapter nextSelectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				++pageNo;
				count = pagerOperation.refresh(pageNo, pageSize);
				refreshButton();
				super.widgetSelected(e);
			}
		};

		btnFirst.addSelectionListener(firstSelectionAdapter);
		btnLast.addSelectionListener(lastSelectionAdapter);
		btnPrev.addSelectionListener(prevSelectionAdapter);
		btnNext.addSelectionListener(nextSelectionAdapter);
		count = pagerOperation.refresh(pageNo, pageSize);
		refreshButton();
	}

	private Integer getLastPageNo() {
		return (count + pageSize) / pageSize;
	}

	private void refreshButton() {
		txtCount.setText("共:" + count + "条");
		txtPage.setText("第 " + pageNo + " 页");
		if (count <= pageSize) {
			/// 单页
			btnNext.setEnabled(false);
			btnLast.setEnabled(false);
			btnFirst.setEnabled(false);
			btnPrev.setEnabled(false);
		} else {
			// 多页
			if (pageNo == 1) {
				btnFirst.setEnabled(false);
				btnPrev.setEnabled(false);
				btnNext.setEnabled(true);
				btnLast.setEnabled(true);
			} else if (pageNo == getLastPageNo()) {
				btnFirst.setEnabled(true);
				btnPrev.setEnabled(true);
				btnNext.setEnabled(false);
				btnLast.setEnabled(false);
			} else {
				if (!btnFirst.getEnabled()) {
					btnFirst.setEnabled(true);
				}
				if (!btnPrev.getEnabled()) {
					btnPrev.setEnabled(true);
				}
				if (!btnNext.getEnabled()) {
					btnNext.setEnabled(true);
				}
				if (!btnLast.getEnabled()) {
					btnLast.setEnabled(true);
				}
			}
		}
	}

}
