package com.clzdl.crm;

import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clzdl.crm.view.common.AbstractSplash;

public class Splash extends AbstractSplash {
	private final static Logger _logger = LoggerFactory.getLogger(Splash.class);
	private MainWindow mainWindow;

	public Splash(Display display) {
		super(display, "images/launch.jpg");
	}

	@Override
	public void loadData() {
		for (int i = 0; i <= 100; ++i) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
			}
			refreshView(i);
		}
		mainWindow = new MainWindow(display);
		mainWindow.launch();

	}

	@Override
	public void openMain() {
		mainWindow.show();
	}
}
