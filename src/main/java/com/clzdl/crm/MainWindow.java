package com.clzdl.crm;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.clzdl.crm.view.biz.panel.biz.BizPanel;
import com.clzdl.crm.view.biz.panel.profile.ProfilePanel;
import com.clzdl.crm.view.common.AbstractComposite;

public class MainWindow extends Shell {
	private Label bottomLabel;
	private Label underToolBarSeparator;
	private List<AbstractComposite> panelContainer = new ArrayList<AbstractComposite>();

	public MainWindow(Display display) {
		super(display);
		createContent();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void createContent() {
		setSize(800, 600);
		setText("客户管理");
		setImage(App.getAppImage());
		Monitor primary = getDisplay().getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = getBounds();
		int x = bounds.x + Math.max(0, (bounds.width - rect.width) / 2);
		int y = bounds.y + Math.max(0, (bounds.height - rect.height) / 2);
		setBounds(x, y, rect.width, rect.height);
		setLayout(new FormLayout());

		////
		ToolBar toolBar = new ToolBar(this, SWT.HORIZONTAL);
		underToolBarSeparator = new Label(this, SWT.SEPARATOR | SWT.BORDER);
		panelContainer.add(new BizPanel(this, SWT.BORDER));
		panelContainer.add(new ProfilePanel(this, SWT.BORDER));

		ToolItem toolItem = null;
		Integer index = 0;
		for (AbstractComposite composite : panelContainer) {
			toolItem = new ToolItem(toolBar, SWT.RADIO);
			toolItem.setText(composite.getTitle());
			toolItem.setWidth(100);
			toolItem.setData(composite);
			toolItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					super.widgetSelected(e);
					refreshPanel((AbstractComposite) e.widget.getData());
				}
			});
			if (index++ == 0) {
				toolItem.setSelection(true);
			}
		}
		bottomLabel = new Label(this, SWT.BORDER);
		FormData toolBarData = new FormData();
		toolBarData.left = new FormAttachment(0);
		toolBarData.top = new FormAttachment(0);
		toolBarData.right = new FormAttachment(100);
		toolBarData.bottom = new FormAttachment(0, 40);
		toolBar.setLayoutData(toolBarData);

		FormData underToolBarSeparatorData = new FormData();
		underToolBarSeparatorData.left = new FormAttachment(0);
		underToolBarSeparatorData.top = new FormAttachment(0, 40);
		underToolBarSeparatorData.bottom = new FormAttachment(0, 41);
		underToolBarSeparatorData.right = new FormAttachment(100);
		underToolBarSeparator.setLayoutData(underToolBarSeparatorData);

		FormData panelFormData = new FormData();
		panelFormData.left = new FormAttachment(0);
		panelFormData.top = new FormAttachment(underToolBarSeparator, 4);
		panelFormData.right = new FormAttachment(100);
		panelFormData.bottom = new FormAttachment(bottomLabel);
		for (AbstractComposite composite : panelContainer) {
			composite.setLayoutData(panelFormData);
		}
		refreshPanel(panelContainer.get(0));

		FormData bottomLabelData = new FormData();
		bottomLabelData.left = new FormAttachment(0);
		bottomLabelData.right = new FormAttachment(100);
		bottomLabelData.bottom = new FormAttachment(100);
		bottomLabel.setLayoutData(bottomLabelData);
		open();
	}

	@Override
	protected void checkSubclass() {
	}

	private void refreshPanel(AbstractComposite panel) {
		for (AbstractComposite composite : panelContainer) {
			if (composite.equals(panel)) {
				composite.setVisible(true);
			} else {
				composite.setVisible(false);
			}
		}
	}
}
