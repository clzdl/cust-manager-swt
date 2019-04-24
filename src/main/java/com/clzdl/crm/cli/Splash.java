package com.clzdl.crm.cli;

import org.eclipse.swt.widgets.Display;

import com.clzdl.crm.App;
import com.clzdl.crm.cli.common.AbstractSplash;

public class Splash extends AbstractSplash {
	private App app;

	public Splash(Display display, App app) {
		super(display, App.getSplashImageFile());
		this.app = app;
	}

	@Override
	public void loadData() {

		try {
			refreshView(10);
			app.launchSpring();
			refreshView(60);
			Thread.sleep(10);
			refreshView(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
