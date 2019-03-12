package com.clzdl.crm.view.biz;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class LoginDialog {
	private Display display;
	private Shell parent;
	private Shell dialog;
	private Boolean logStatus = false;

	public LoginDialog(Shell parent) {
		this.parent = parent;
		display = parent.getDisplay();
	}

	public Boolean show() {
		dialog = new Shell(parent);
		/// 主屏幕显示位置
		Rectangle bounds = display.getPrimaryMonitor().getBounds();
		Rectangle rect = dialog.getBounds();
		int x = bounds.x + Math.max(0, (bounds.width - rect.width) / 2);
		int y = bounds.y + Math.max(0, (bounds.height - rect.height) / 2);
		dialog.setBounds(x, y, rect.width, rect.height);
		dialog.pack();
		dialog.open();
		while (!dialog.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		return logStatus;
	}
}
