package com.clzdl.crm.view.common;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

public abstract class AbstractSplash {
	private String splashImageFile;

	public AbstractSplash(String splashImageFile) {
		this.splashImageFile = splashImageFile;
	}

	/**
	 * Create contents of the window
	 */
	// 在进度条时加载后台数据。
	public abstract void loadData(ProgressBar bar); // 滚动条滚动值需自行实现

	/// 执行显示主页面
	public abstract void openMain();

	/**
	 * @throws InterruptedException
	 * @wbp.parser.entryPoint
	 */
	public void createContents(Display display) throws InterruptedException {

		// 启动页面
		final Image image = new Image(display, this.getClass().getClassLoader().getResourceAsStream(splashImageFile));
		final Shell splashShell = new Shell(SWT.ON_TOP);

		final ProgressBar bar = new ProgressBar(splashShell, SWT.NONE);
		final Color color = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
		bar.setMinimum(0);
		bar.setMaximum(100);
		bar.setForeground(color);

		final Label label = new Label(splashShell, SWT.NONE);
		label.setImage(image);
		FormLayout layout = new FormLayout();
		splashShell.setLayout(layout);
		FormData labelData = new FormData();
		labelData.right = new FormAttachment(100, 0);
		labelData.bottom = new FormAttachment(100, 0);
		label.setLayoutData(labelData);
		FormData progressData = new FormData();
		progressData.left = new FormAttachment(0, 5);
		progressData.right = new FormAttachment(100, -5);
		progressData.bottom = new FormAttachment(114, -35);
		bar.setLayoutData(progressData);
		splashShell.pack();

		Rectangle splashRect = splashShell.getBounds();
		Rectangle displayRect = display.getBounds();

		int x = (displayRect.width - splashRect.width) / 2;
		int y = (displayRect.height - splashRect.height) / 2;
		splashShell.setLocation(x, y);
		splashShell.open();
		display.asyncExec(new Runnable() { // 异步执行一个线程
			public void run() {
				for (int i = 1; i < 2; i++) {
					try {
						Thread.sleep(1000);
					} catch (Throwable e) {
					}
					loadData(bar);
				}
				splashShell.close();
				color.dispose();
				image.dispose();
				// 主页面
				openMain();
			}
		});
	}

}
