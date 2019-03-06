package com.clzdl.crm;

import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import com.clzdl.crm.view.common.AbstractSplash;

public class Splash extends AbstractSplash {
	private MainWindow mainWindow;
	private Shell shell;

	public Splash(Shell shell) {
		super("images/cat-splash.jpg");
		this.shell = shell;
	}

	@Override
	public void loadData(ProgressBar bar) {
		mainWindow = new MainWindow(shell);
		mainWindow.launch();
	}

	@Override
	public void openMain() {

		mainWindow.show();
	}
}
