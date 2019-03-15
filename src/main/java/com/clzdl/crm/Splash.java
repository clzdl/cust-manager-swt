package com.clzdl.crm;

import org.eclipse.swt.widgets.Display;

import com.clzdl.crm.view.common.AbstractSplash;

public class Splash extends AbstractSplash {
	private App app;

	public Splash(Display display, App app) {
		super(display, app._launchSplashFile);
		this.app = app;
	}

	@Override
	public void loadData() {

		try {
			refreshView(10);
			app.launchSpring();
			refreshView(30);
			Thread.sleep(10);
			refreshView(70);
			Thread.sleep(10);
			refreshView(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
