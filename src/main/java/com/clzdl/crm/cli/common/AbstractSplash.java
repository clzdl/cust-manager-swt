package com.clzdl.crm.cli.common;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import com.clzdl.crm.App;

public abstract class AbstractSplash {
	private String splashImageFile;
	protected Display display;
	private Shell splashShell;
	private ProgressBar bar;
	private Image image;
	private Color color;
	private Label label;

	public AbstractSplash(Display display, String splashImageFile) {
		this.display = display;
		this.splashImageFile = splashImageFile;
	}

	/**
	 * Create contents of the window
	 */
	// 加载后台数据，返回完成值，用户更新进度条
	public abstract void loadData();

	/**
	 * 更新启动界面
	 */
	protected void refreshView(final int progressBasPos) {
		/// 等待ui线程执行完毕
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				if (bar.isDisposed())
					return;
				bar.setSelection(progressBasPos);
			}
		});
	}

	private void closeView() {
		color.dispose();
		image.dispose();
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				splashShell.close();
				splashShell.dispose();
			}
		});
	}

	/**
	 * @throws InterruptedException
	 * @wbp.parser.entryPoint
	 */
	public void createContents() throws InterruptedException {
		// 启动页面
		image = new Image(display, this.getClass().getClassLoader().getResourceAsStream(splashImageFile));
		splashShell = new Shell(display, SWT.NONE);
		RowLayout layout = new RowLayout();
		layout.type = SWT.VERTICAL;
		layout.fill = true;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginTop = 0;
		layout.marginBottom = 0;
		layout.spacing = 0;
		splashShell.setLayout(layout);
		splashShell.setImage(App.getAppImage());

		label = new Label(splashShell, SWT.NONE);
		label.setImage(image);

		bar = new ProgressBar(splashShell, SWT.NONE);
		color = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
		bar.setMinimum(0);
		bar.setMaximum(100);
		bar.setForeground(color);

		splashShell.pack();

		Rectangle splashRect = splashShell.getBounds();
		Rectangle displayRect = display.getPrimaryMonitor().getBounds();

		int x = (displayRect.width - splashRect.width) / 2;
		int y = (displayRect.height - splashRect.height) / 2;
		splashShell.setLocation(x, y);
		splashShell.open();

		//// 加载数据
		new Thread() {
			@Override
			public void run() {
				loadData();
				closeView();
			}

		}.start();

		while (!splashShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

}
