package com.clzdl.crm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.clzdl.crm.view.biz.CardInfoContent;
import com.clzdl.crm.view.biz.CustInfoContent;

public class MainWindow {
	private final Logger _logger = LoggerFactory.getLogger(MainWindow.class);
	private Display display;
	private static ApplicationContext context;
	private Shell shell;
	private static Boolean isLogin = false;
	private Label bottomLabel;
	private Label underToolBarSeparator;
	private Sash sash;
	private Table leftMenuTab;
	private CustInfoContent custInfoContent;
	private CardInfoContent cardInfoContent;
	private Composite currentContent;

	public MainWindow(Display display) {
		this.display = display;
	}

	public void launch() {
		try {
			context = new ClassPathXmlApplicationContext(new String[] { "classpath:/applicationContext.xml" });
		} catch (Exception e) {
			_logger.error("{}:{}", e.getMessage(), e);
		}
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void show() {
		shell = new Shell(display);
		shell.setSize(800, 600);
		shell.setText("客户管理");

		/// 主屏幕显示位置
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + Math.max(0, (bounds.width - rect.width) / 2);
		int y = bounds.y + Math.max(0, (bounds.height - rect.height) / 2);
		shell.setBounds(x, y, rect.width, rect.height);
		shell.setLayout(new FormLayout());

		Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		MenuItem fileItem = new MenuItem(menuBar, SWT.CASCADE);
		fileItem.setText("File");
		MenuItem editItem = new MenuItem(menuBar, SWT.CASCADE);
		editItem.setText("Edit");
		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		fileItem.setMenu(fileMenu);
		String[] fileStrings = { "New", "Close", "Exit" };
		for (int i = 0; i < fileStrings.length; i++) {
			MenuItem item = new MenuItem(fileMenu, SWT.PUSH);
			item.setText(fileStrings[i]);

			item.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}
			});
		}

		Menu editMenu = new Menu(shell, SWT.DROP_DOWN);
		String[] editStrings = { "Cut", "Copy", "Paste" };
		editItem.setMenu(editMenu);
		for (int i = 0; i < editStrings.length; i++) {
			MenuItem item = new MenuItem(editMenu, SWT.PUSH);
			item.setText(editStrings[i]);
		}

		////
		ToolBar toolBar = new ToolBar(shell, SWT.HORIZONTAL | SWT.SHADOW_OUT);
		ToolItem cutItem = new ToolItem(toolBar, SWT.PUSH);
		cutItem.setText("cut");
		cutItem.setToolTipText("剪切");
		new ToolItem(toolBar, SWT.SEPARATOR);
		ToolItem copyItem = new ToolItem(toolBar, SWT.PUSH);
		copyItem.setText("copy");
		copyItem.setToolTipText("复制");

		underToolBarSeparator = new Label(shell, SWT.SEPARATOR | SWT.BORDER);

		bottomLabel = new Label(shell, SWT.BORDER);

		leftMenuTab = new Table(shell, SWT.BORDER);
		leftMenuTab.setLinesVisible(true);

		sash = new Sash(shell, SWT.VERTICAL);
		custInfoContent = new CustInfoContent(shell, SWT.BORDER);
		cardInfoContent = new CardInfoContent(shell, SWT.BORDER);

		TableItem itemCust = new TableItem(leftMenuTab, SWT.NULL);
		itemCust.setData(custInfoContent);
		itemCust.setText(custInfoContent.getTitle());

		TableItem itemCard = new TableItem(leftMenuTab, SWT.NULL);
		itemCard.setData(cardInfoContent);
		itemCard.setText(cardInfoContent.getTitle());

		leftMenuTab.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				super.widgetDefaultSelected(e);
				currentContent.setVisible(false);
				currentContent = (Composite) e.item.getData();
				currentContent.setVisible(true);
			}
		});

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

		FormData leftMenuFormData = new FormData();
		leftMenuFormData.left = new FormAttachment(0);
		leftMenuFormData.top = new FormAttachment(underToolBarSeparator);
		leftMenuFormData.right = new FormAttachment(sash);
		leftMenuFormData.bottom = new FormAttachment(bottomLabel);
		leftMenuTab.setLayoutData(leftMenuFormData);

		final FormData sashFormData = new FormData();
		sashFormData.left = new FormAttachment(30);
		sashFormData.top = new FormAttachment(underToolBarSeparator);
		sashFormData.bottom = new FormAttachment(bottomLabel);
		sash.setLayoutData(sashFormData);

		FormData contentFormData = new FormData();
		contentFormData.left = new FormAttachment(sash);
		contentFormData.right = new FormAttachment(100, -10);
		contentFormData.top = new FormAttachment(underToolBarSeparator);
		contentFormData.bottom = new FormAttachment(bottomLabel);
		custInfoContent.setLayoutData(contentFormData);
		currentContent = custInfoContent;
		cardInfoContent.setLayoutData(contentFormData);
		cardInfoContent.setVisible(false);

		FormData bottomLabelData = new FormData();
		bottomLabelData.left = new FormAttachment(0);
		bottomLabelData.right = new FormAttachment(100);
		bottomLabelData.bottom = new FormAttachment(100);
		bottomLabel.setLayoutData(bottomLabelData);

		sash.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.detail != SWT.DRAG) {
					sashFormData.left = new FormAttachment(0, e.x);
					shell.layout();
				}
				super.widgetSelected(e);

			}
		});

//		LoginDialog loginDlg = new LoginDialog(shell);
//		loginDlg.show();
//		if (!loginDlg.isLogin()) {
//			shell.dispose();
//			return;
//		}

		// shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

}
