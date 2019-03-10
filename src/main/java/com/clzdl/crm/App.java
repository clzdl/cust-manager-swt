package com.clzdl.crm;

import org.eclipse.swt.widgets.Display;

/**
 * Hello world!
 *
 */
public class App {
	private static MainWindow mainWindow;
	private static final String _version = "0.0.1";
	public static Display display;

	public static void main(String[] args) throws InterruptedException {
		display = new Display();
		Display.setAppName("客户管理程序");
		Display.setAppVersion(_version);

		mainWindow = new MainWindow(display);
		new Splash(display, mainWindow).createContents();
		mainWindow.show();

		display.dispose();
	}
}
