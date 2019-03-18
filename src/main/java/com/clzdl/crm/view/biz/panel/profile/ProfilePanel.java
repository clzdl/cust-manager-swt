package com.clzdl.crm.view.biz.panel.profile;

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

import com.clzdl.crm.view.biz.panel.profile.content.sysuser.SysUserContent;

public class ProfilePanel extends Composite {

	private Sash sash;
	private Table leftMenuTab;
	private SysUserContent sysUserContent;
	private Composite currentContent;

	public ProfilePanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		leftMenuTab = new Table(this, SWT.BORDER);
		leftMenuTab.setLinesVisible(true);

		sash = new Sash(this, SWT.VERTICAL);
		sysUserContent = new SysUserContent(this, SWT.BORDER);

		TableItem itemSysUser = new TableItem(leftMenuTab, SWT.NULL);
		itemSysUser.setData(sysUserContent);
		itemSysUser.setText(sysUserContent.getTitle());

		leftMenuTab.setSelection(itemSysUser);
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
		sysUserContent.setLayoutData(contentFormData);
		currentContent = sysUserContent;

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
