package com.clzdl.crm.view.biz.panel.profile;

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

import com.clzdl.crm.view.biz.panel.profile.content.sysmenu.SysMenuContent;
import com.clzdl.crm.view.biz.panel.profile.content.sysrole.SysRoleContent;
import com.clzdl.crm.view.biz.panel.profile.content.sysuser.SysUserContent;
import com.clzdl.crm.view.common.AbstractPanelRightContent;

public class ProfilePanel extends Composite {

	private Sash sash;
	private Table leftMenuTab;
	private List<AbstractPanelRightContent> rightContents = new ArrayList<AbstractPanelRightContent>();
	private AbstractPanelRightContent currentContent;

	public ProfilePanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		leftMenuTab = new Table(this, SWT.BORDER);
		leftMenuTab.setLinesVisible(true);

		sash = new Sash(this, SWT.VERTICAL);
		rightContents.add(new SysUserContent(this, SWT.BORDER));
		rightContents.add(new SysRoleContent(this, SWT.BORDER));
		rightContents.add(new SysMenuContent(this, SWT.BORDER));

		leftMenuTab.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				super.widgetDefaultSelected(e);
				currentContent.setVisible(false);
				currentContent = (AbstractPanelRightContent) e.item.getData();
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

		///
		FormData contentFormData = new FormData();
		contentFormData.left = new FormAttachment(sash);
		contentFormData.top = new FormAttachment(0);
		contentFormData.right = new FormAttachment(100, -10);
		contentFormData.bottom = new FormAttachment(100);

		TableItem item = null;
		Integer index = 0;
		for (AbstractPanelRightContent content : rightContents) {
			item = new TableItem(leftMenuTab, SWT.NULL);
			item.setData(content);
			item.setText(content.getTitle());
			content.setLayoutData(contentFormData);
			if (0 == index++) {
				leftMenuTab.setSelection(item);
				currentContent = content;
			} else {
				content.setVisible(false);
			}
		}

		///
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
