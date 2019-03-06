package com.clzdl.crm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainWindow {
	private Shell shell;
	private static Table table;
	private static ApplicationContext context;

	public MainWindow(Shell shell) {
		this.shell = shell;
	}

	public void launch() {
		context = new ClassPathXmlApplicationContext(new String[] { "classpath:/applicationContext.xml" });
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void show() {
		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("col1");

		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(100);
		tblclmnNewColumn_1.setText("col2");

		shell.open();
	}

}
