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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clzdl.crm.springboot.auth.EnumSysPermissionProfile;
import com.clzdl.crm.utils.HttpUtil;
import com.clzdl.crm.utils.HttpUtil.HttpParam;
import com.clzdl.crm.view.biz.panel.profile.content.sysmenu.SysMenuContent;
import com.clzdl.crm.view.biz.panel.profile.content.sysrole.SysRoleContent;
import com.clzdl.crm.view.biz.panel.profile.content.sysuser.SysUserContent;
import com.clzdl.crm.view.common.AbstractComposite;
import com.fasterxml.jackson.databind.JsonNode;

public class ProfilePanel extends AbstractComposite {
	private final static Logger _logger = LoggerFactory.getLogger(ProfilePanel.class);
	private final static String title = "配置数据";
	private Sash sash;
	private Table leftMenuTab;
	private List<AbstractComposite> rightContents = new ArrayList<AbstractComposite>();

	public ProfilePanel(Composite parent, int style) {
		super(parent, style, title, EnumSysPermissionProfile.PROFILE);
		setLayout(new FormLayout());
		leftMenuTab = new Table(this, SWT.BORDER);
		leftMenuTab.setLinesVisible(true);

		sash = new Sash(this, SWT.VERTICAL);
		buildContent();
		leftMenuTab.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				super.widgetDefaultSelected(e);
				refreshContent((AbstractComposite) e.item.getData());
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
		for (AbstractComposite content : rightContents) {
			item = new TableItem(leftMenuTab, SWT.NULL);
			item.setData(content);
			item.setText(content.getTitle());
			content.setLayoutData(contentFormData);
			if (0 == index++) {
				leftMenuTab.setSelection(item);
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

	private void refreshContent(AbstractComposite composite) {
		for (AbstractComposite content : rightContents) {
			if (content.equals(composite)) {
				content.setVisible(true);
			} else {
				content.setVisible(false);
			}
		}
	}

	private void buildContent() {
		List<AbstractComposite> list = new ArrayList<AbstractComposite>();
		list.add(new SysUserContent(this, SWT.BORDER));
		list.add(new SysRoleContent(this, SWT.BORDER));
		list.add(new SysMenuContent(this, SWT.BORDER));
		try {
			JsonNode result = null;
			for (AbstractComposite composite : list) {
				if (EnumSysPermissionProfile.NONE.equals(composite.getPermission())) {
					rightContents.add(composite);
				} else {
					result = HttpUtil.get("/panel/profile/sysuser/havepermission.json",
							new HttpParam[] { new HttpParam("permission", composite.getPermission().getCode()) });

					if (result.asBoolean()) {
						rightContents.add(composite);
					} else {
						composite.dispose();
					}
				}
			}
		} catch (Exception e) {
			_logger.error(e.getMessage(), e);
		}
	}

}
