package com.clzdl.crm.view.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;

public class LoadingDialog extends Shell {
	private final Integer _defaultDelayTime = 20;
	private ImageData[] loadingImageData;
	private Image[] images;
	private Integer currentImage = 0;
	private Boolean slowAnimation = false;
	private Integer animationLoopCount = 0;
	private static ExecutorService executorService;

	public interface TaskLoading {
		void doing();
	}

	static {
		executorService = Executors.newSingleThreadExecutor();
	}

	public LoadingDialog(Shell parent) {
		super(parent, SWT.PRIMARY_MODAL);
		createContent(parent);
	}

	public void start(final TaskLoading task) {
		setSize(loadingImageData[0].width, loadingImageData[0].height);
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				Image img = getCurrentImage();
				// Display the image, then erase the rest
				e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
				e.gc.drawImage(img, 0, 0);
			}
		});
		/// 主屏幕显示位置
		Rectangle bounds = getParent().getBounds();
		Rectangle rect = getBounds();
		int x = bounds.x + Math.max(0, (bounds.width - rect.width) / 2);
		int y = bounds.y + Math.max(0, (bounds.height - rect.height) / 2);
		setBounds(x, y, loadingImageData[0].width, loadingImageData[0].height);
		open();
		startAnimationTimer();
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
		while (!isDisposed()) {
			if (!getDisplay().readAndDispatch()) {
				getDisplay().sleep();
			}
		}
	}

	private void createContent(Shell parent) {
		setLayout(new FillLayout());
		loadingImageData = new ImageLoader().load(this.getClass().getResource("/").getPath() + "/images/loading.gif");
		convertImageDataToImages(getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));

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
		if (slowAnimation) {
			return loadingImageData[currentImage].delayTime * 10 + 30;
		} else {
			return loadingImageData[currentImage].delayTime * 10;
		}
	}

	private void convertImageDataToImages(Color defaultBackground) {
		images = new Image[loadingImageData.length];
		// Step 1: Determine the size of the resulting images.
		int width = 0, height = 0;
		for (int i = 0; i < loadingImageData.length; ++i) {
			ImageData id = loadingImageData[i];
			width = Math.max(width, id.x + id.width);
			height = Math.max(height, id.y + id.height);
			if (id.delayTime == 0) {
				id.delayTime = _defaultDelayTime;
			}
		}
		// Step 2: Construct each image.
		int transition = SWT.DM_FILL_BACKGROUND;
		for (int i = 0; i < loadingImageData.length; ++i) {
			ImageData id = loadingImageData[i];
			images[i] = new Image(getDisplay(), width, height);
			GC gc = new GC(images[i]);
			// Do the transition from the previous image.
			switch (transition) {
			case SWT.DM_FILL_NONE:
			case SWT.DM_UNSPECIFIED:
				// Start from last image.
				gc.drawImage(images[i - 1], 0, 0);
				break;
			case SWT.DM_FILL_PREVIOUS:
				// Start from second last image.
				gc.drawImage(images[i - 2], 0, 0);
				break;
			default:
				// DM_FILL_BACKGROUND or anything else,
				// just fill with default background.
				gc.setBackground(defaultBackground);
				gc.fillRectangle(0, 0, width, height);
				break;
			}
			// Draw the current image and clean up.
			Image img = new Image(getDisplay(), id);
			gc.drawImage(img, 0, 0, id.width, id.height, id.x, id.y, id.width, id.height);
			img.dispose();
			gc.dispose();
			// Compute the next transition.
			// Special case: Can't do DM_FILL_PREVIOUS on the
			// second image since there is no "second last"
			// image to use.
			transition = id.disposalMethod;
			if (i == 0 && transition == SWT.DM_FILL_PREVIOUS)
				transition = SWT.DM_FILL_NONE;
		}
	}

	// 继承shell 需要此函数
	protected void checkSubclass() {
	}

}
