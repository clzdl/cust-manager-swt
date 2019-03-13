package com.clzdl.crm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.clzdl.crm.view.biz.LoginDialog;

public class MainWindow {
	private final Logger _logger = LoggerFactory.getLogger(MainWindow.class);
	private Display display;
	private static ApplicationContext context;
	private Shell shell;
	private static Boolean isLogin = false;
	private Label bottomLabel;
	private Label underToolBarSeparator;

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
		shell.setSize(592, 486);
		shell.setText("客户管理");

		/// 主屏幕显示位置
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + Math.max(0, (bounds.width - rect.width) / 2);
		int y = bounds.y + Math.max(0, (bounds.height - rect.height) / 2);
		shell.setBounds(x, y, rect.width, rect.height);

		FormLayout layout = new FormLayout();
		shell.setLayout(layout);

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

		/////
		Tree tree = new Tree(shell, SWT.BORDER);
		for (int i = 0; i < 4; i++) {
			TreeItem itemI = new TreeItem(tree, SWT.NULL);
			itemI.setText("Item " + i);
			for (int j = 0; j < 4; j++) {
				TreeItem itemJ = new TreeItem(itemI, SWT.NULL);
				itemJ.setText("Item " + i + " " + j);
				for (int k = 0; k < 4; k++) {
					TreeItem itemK = new TreeItem(itemJ, SWT.NULL);
					itemK.setText("Item " + i + " " + j + " " + k);
				}
			}
		}

		tree.setBackground(display.getSystemColor(SWT.COLOR_GRAY));

		FormData toolBarData = new FormData();
		toolBarData.left = new FormAttachment(0);
		toolBarData.top = new FormAttachment(0);
		toolBarData.right = new FormAttachment(100);
		toolBarData.bottom = new FormAttachment(6);
		toolBar.setLayoutData(toolBarData);

		FormData underToolBarSeparatorData = new FormData();
		underToolBarSeparatorData.left = new FormAttachment(0);
		underToolBarSeparatorData.top = new FormAttachment(6, 1);
		underToolBarSeparatorData.right = new FormAttachment(100);
		underToolBarSeparatorData.bottom = new FormAttachment(6, 2);
		underToolBarSeparator.setLayoutData(underToolBarSeparatorData);

		FormData treeData = new FormData();
		treeData.left = new FormAttachment(0);
		treeData.top = new FormAttachment(6, 3);
		treeData.right = new FormAttachment(20);
		treeData.bottom = new FormAttachment(100);
		tree.setLayoutData(treeData);

		FormData bottomLabelData = new FormData();
		bottomLabelData.left = new FormAttachment(0);
		bottomLabelData.right = new FormAttachment(100);
		bottomLabelData.bottom = new FormAttachment(100);
		bottomLabel.setLayoutData(bottomLabelData);

		LoginDialog loginDlg = new LoginDialog(shell);
		loginDlg.show();
		if (!loginDlg.isLogin()) {
			shell.dispose();
			return;
		}

//		Table table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
//		table.setHeaderVisible(true);
//		table.setLinesVisible(true);
//
//		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
//		tblclmnNewColumn.setWidth(100);
//		tblclmnNewColumn.setText("col1");
//
//		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
//		tblclmnNewColumn_1.setWidth(100);
//		tblclmnNewColumn_1.setText("col2");
//
//		Listener listener = new Listener() {
//			public void handleEvent(Event event) {
//				Button button = (Button) event.widget;
//				if (!button.getSelection())
//					return;
//				System.out.println("Arriving " + button.getText());
//			}
//		};
//		Button land = new Button(shell, SWT.RADIO);
//		land.setText("By Land");
//		land.addListener(SWT.Selection, listener);
//		Button sea = new Button(shell, SWT.RADIO);
//		sea.setText("By Sea");
//		sea.addListener(SWT.Selection, listener);
//		sea.setSelection(true);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

}
