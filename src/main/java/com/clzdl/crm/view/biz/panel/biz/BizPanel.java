package com.clzdl.crm.view.biz.panel.biz;

import java.util.ArrayList;
import java.util.List;

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

import com.clzdl.crm.common.auth.enums.EnumSysPermissionProfile;
import com.clzdl.crm.view.biz.panel.biz.content.card.CardInfoContent;
import com.clzdl.crm.view.biz.panel.biz.content.customer.CustInfoContent;
import com.clzdl.crm.view.common.AbstractComposite;

public class BizPanel extends AbstractComposite {
	private final static String title = "业务数据";
	private Sash sash;
	private Table leftMenuTab;
	private List<AbstractComposite> rightContents = new ArrayList<AbstractComposite>();
	private AbstractComposite currentContent;

	public BizPanel(Composite parent, int style) {
		super(parent, style, title, EnumSysPermissionProfile.BIZ);
		setLayout(new FormLayout());

		leftMenuTab = new Table(this, SWT.BORDER);
		leftMenuTab.setLinesVisible(true);

		sash = new Sash(this, SWT.VERTICAL);
		rightContents.add(new CustInfoContent(this, SWT.BORDER));
		rightContents.add(new CardInfoContent(this, SWT.BORDER));

		leftMenuTab.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				super.widgetDefaultSelected(e);
				currentContent.setVisible(false);
				currentContent = (AbstractComposite) e.item.getData();
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
		Integer index = 0;
		TableItem item = null;
		for (AbstractComposite content : rightContents) {
			item = new TableItem(leftMenuTab, SWT.NULL);
			item.setData(content);
			item.setText(content.getTitle());
			content.setLayoutData(contentFormData);
			if (0 == index++) {
				currentContent = content;
				leftMenuTab.setSelection(item);
			} else {
				content.setVisible(false);
			}
		}

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