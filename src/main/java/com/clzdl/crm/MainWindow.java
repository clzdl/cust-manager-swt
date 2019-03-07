package com.clzdl.crm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
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
		Table table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("col1");

		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(100);
		tblclmnNewColumn_1.setText("col2");
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

}
