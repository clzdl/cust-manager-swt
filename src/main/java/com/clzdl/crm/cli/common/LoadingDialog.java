package com.clzdl.crm.cli.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;

public class LoadingDialog extends Shell {
	public final static Integer _defaultLoadingDelayTime = 200;
	private static ExecutorService executorService;
	private Image[] images;
	private Integer currentImage = 0;
	private Boolean slowAnimation = false;
	private Integer animationLoopCount = 0;
	private Shell parent;

	public interface TaskLoading {
		void doing();
	}

	static {
		executorService = Executors.newSingleThreadExecutor();
	}

	public LoadingDialog(Shell parent, Image[] images) {
		super(parent, SWT.PRIMARY_MODAL);
		this.parent = parent;
		this.images = images;
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				Image img = getCurrentImage();
				// Display the image, then erase the rest
				e.gc.drawImage(img, 0, 0);
			}
		});
	}

	public void start(final TaskLoading task) {
		if (null != parent && parent.isVisible()) {
			Integer imgWidth = images[0].getImageData().width;
			Integer imgHeight = images[0].getImageData().height;
			setLayout(new FillLayout());
			setSize(imgWidth, imgHeight);
			/// 主屏幕显示位置
			Rectangle bounds = parent.getBounds();
			Rectangle rect = getBounds();
			int x = bounds.x + Math.max(0, (bounds.width - rect.width) / 2);
			int y = bounds.y + Math.max(0, (bounds.height - rect.height) / 2);
			setBounds(x, y, imgWidth, imgHeight);
			open();
			startAnimationTimer();
		}
		run(task);
		while (!isDisposed()) {
			if (!getDisplay().readAndDispatch()) {
				getDisplay().sleep();
			}
		}

	}

	public static void closeExecutor() {
		executorService.shutdownNow();
	}

	// 继承shell 需要此函数
	protected void checkSubclass() {
	}

	private void startAnimationTimer() {
		// If there is only one image, don't start a timer.
		if (images.length < 2)
			return;
		int delayTime = getDelayTime();
		getDisplay().timerExec(delayTime, new Runnable() {
			public void run() {
				if (isDisposed()) {
					return;
				}
				currentImage = (currentImage + 1) % images.length;
				redraw();
				// If this is the last image in the
				// animation, check if we are looping
				// forever, or still have more loops to
				// do. If not, don't restart the timer.
				if (currentImage + 1 == images.length && animationLoopCount != 0 && --animationLoopCount <= 0) {
					return;
				}
				getDisplay().timerExec(getDelayTime(), this);
			}
		});
	}

	private Image getCurrentImage() {
		if (images == null) {
			return null;
		}
		return images[currentImage];
	}

	private int getDelayTime() {
		Integer delayTime = null;
		if (slowAnimation) {
			delayTime = images[currentImage].getImageData().delayTime * 10 + 30;
		} else {
			delayTime = images[currentImage].getImageData().delayTime * 10;
		}

		return delayTime > 0 ? delayTime : _defaultLoadingDelayTime;
	}

	private void run(final TaskLoading task) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					task.doing();
				} catch (Exception e) {
					e.printStackTrace();
				}
				getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						dispose();
					}
				});
			}
		});
	}

}
