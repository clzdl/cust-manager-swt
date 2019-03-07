package com.clzdl.crm;

import org.eclipse.swt.widgets.Display;

/**
 * Hello world!
 *
 */
public class App {
	private static MainWindow mainWindow;

	public static void main(String[] args) throws InterruptedException {
		final Display display = new Display();
		mainWindow = new MainWindow(display);
		new Splash(display, mainWindow).createContents();
		mainWindow.show();

		display.dispose();
	}
}
