package com.clzdl.crm.view.biz.panel.biz;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.clzdl.crm.view.biz.panel.biz.content.card.CardInfoContent;
import com.clzdl.crm.view.biz.panel.biz.content.customer.CustInfoContent;

public class BizPanel extends Composite {
	private Sash sash;
	private Table leftMenuTab;
	private CustInfoContent custInfoContent;
	private CardInfoContent cardInfoContent;
	private Composite currentContent;

	public BizPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());

		leftMenuTab = new Table(this, SWT.BORDER);
		leftMenuTab.setLinesVisible(true);

		sash = new Sash(this, SWT.VERTICAL);
		custInfoContent = new CustInfoContent(this, SWT.BORDER);
		cardInfoContent = new CardInfoContent(this, SWT.BORDER);

		TableItem itemCust = new TableItem(leftMenuTab, SWT.NULL);
		itemCust.setData(custInfoContent);
		itemCust.setText(custInfoContent.getTitle());

		TableItem itemCard = new TableItem(leftMenuTab, SWT.NULL);
		itemCard.setData(cardInfoContent);
		itemCard.setText(cardInfoContent.getTitle());

		leftMenuTab.setSelection(itemCust);

		leftMenuTab.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				super.widgetDefaultSelected(e);
				currentContent.setVisible(false);
				currentContent = (Composite) e.item.getData();
				currentContent.setVisible(true);
			}
		});

		FormData leftMenuFormData = new FormData();
		leftMenuFormData.left = new FormAttachment(0);
		leftMenuFormData.top = new FormAttachment(0);
		leftMenuFormData.right = new FormAttachment(sash);
		leftMenuFormData.bottom = new FormAttachment(100);
		leftMenuTab.setLayoutData(leftMenuFormData);

		final FormData sashFormData = new FormData();
		sashFormData.left = new FormAttachment(30);
		sashFormData.top = new FormAttachment(0);
		sashFormData.bottom = new FormAttachment(100);
		sash.setLayoutData(sashFormData);

		FormData contentFormData = new FormData();
		contentFormData.left = new FormAttachment(sash);
		contentFormData.top = new FormAttachment(0);
		contentFormData.right = new FormAttachment(100, -10);
		contentFormData.bottom = new FormAttachment(100);
		custInfoContent.setLayoutData(contentFormData);
		currentContent = custInfoContent;
		cardInfoContent.setLayoutData(contentFormData);
		cardInfoContent.setVisible(false);

		sash.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.detail != SWT.DRAG) {
					sashFormData.left = new FormAttachment(0, e.x);
					layout();
				}
				super.widgetSelected(e);

			}
		});
	}

}
