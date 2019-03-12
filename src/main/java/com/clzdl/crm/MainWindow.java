package com.clzdl.crm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import swing2swt.layout.FlowLayout;

public class MainWindow {
	private final Logger _logger = LoggerFactory.getLogger(MainWindow.class);
	private Display display;
	private static ApplicationContext context;
	private Shell shell;

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
		shell.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		/// 主屏幕显示位置
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + Math.max(0, (bounds.width - rect.width) / 2);
		int y = bounds.y + Math.max(0, (bounds.height - rect.height) / 2);
		shell.setBounds(x, y, rect.width, rect.height);

		Table table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("col1");

		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(100);
		tblclmnNewColumn_1.setText("col2");

		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				Button button = (Button) event.widget;
				if (!button.getSelection())
					return;
				System.out.println("Arriving " + button.getText());
			}
		};
		Button land = new Button(shell, SWT.RADIO);
		land.setText("By Land");
		land.addListener(SWT.Selection, listener);
		Button sea = new Button(shell, SWT.RADIO);
		sea.setText("By Sea");
		sea.addListener(SWT.Selection, listener);
		sea.setSelection(true);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

}
