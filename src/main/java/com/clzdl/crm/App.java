package com.clzdl.crm;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Hello world!
 *
 */
public class App {
	private static Splash splash;
	private static Shell shell;

	public static void main(String[] args) throws InterruptedException {
		final Display display = new Display();
		shell = new Shell(display);
		shell.setSize(592, 486);
		shell.setText("客户管理");
		splash = new Splash(display);
		splash.createContents();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

	}
}
