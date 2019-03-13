package com.clzdl.crm.view.biz;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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

	public void show() {
		dialog = new Shell(parent, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		dialog.setSize(400, 300);
		dialog.setText("登录");

		dialog.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				/// 关闭程序
				parent.dispose();
			}
		});

		/// 主屏幕显示位置
		Rectangle bounds = parent.getBounds();
		Rectangle rect = dialog.getBounds();
//		Rectangle  = display.map(dialog, , dialogRect);
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
	}

	public Boolean isLogin() {
		return logStatus;
	}
}
