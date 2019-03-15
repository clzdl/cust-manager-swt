package com.clzdl.crm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.clzdl.crm.view.common.LoadingDialog;
import com.clzdl.crm.view.common.LoadingDialog.TaskLoading;

/**
 * 客户管理程序入口类
 *
 */
public class App {
	private final static String _version = "0.0.1";
	public final static String _launchSplashFile = "images/launch.jpg";
	private final static Logger _logger = LoggerFactory.getLogger(App.class);
	public static ApplicationContext context;
	public static Display display;
	public static Image[] loadingImages;
	private MainWindow mainWindow;

	public App() {
	}

	public void launchSpring() {
		try {
			context = new ClassPathXmlApplicationContext(new String[] { "classpath:/applicationContext.xml" });
		} catch (Exception e) {
			_logger.error("{}:{}", e.getMessage(), e);
		}
	}

	public void show() {
		LoadingDialog loading = new LoadingDialog(display, loadingImages);
		loading.start(new TaskLoading() {
			@Override
			public void doing() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		mainWindow = new MainWindow(display);
		while (!mainWindow.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public void convertImageDataToImages(Color defaultBackground) {
		ImageData[] loadingImageData = new ImageLoader()
				.load(this.getClass().getResource("/").getPath() + "/images/loading.gif");
		loadingImages = new Image[loadingImageData.length];
		// Step 1: Determine the size of the resulting images.
		int width = 0, height = 0;
		for (int i = 0; i < loadingImageData.length; ++i) {
			ImageData id = loadingImageData[i];
			width = Math.max(width, id.x + id.width);
			height = Math.max(height, id.y + id.height);
		}
		// Step 2: Construct each image.
		int transition = SWT.DM_FILL_BACKGROUND;
		for (int i = 0; i < loadingImageData.length; ++i) {
			ImageData id = loadingImageData[i];
			loadingImages[i] = new Image(display, width, height);
			GC gc = new GC(loadingImages[i]);
			// Do the transition from the previous image.
			switch (transition) {
			case SWT.DM_FILL_NONE:
			case SWT.DM_UNSPECIFIED:
				// Start from last image.
				gc.drawImage(loadingImages[i - 1], 0, 0);
				break;
			case SWT.DM_FILL_PREVIOUS:
				// Start from second last image.
				gc.drawImage(loadingImages[i - 2], 0, 0);
				break;
			default:
				// DM_FILL_BACKGROUND or anything else,
				// just fill with default background.
				gc.setBackground(defaultBackground);
				gc.fillRectangle(0, 0, width, height);
				break;
			}
			// Draw the current image and clean up.
			Image img = new Image(display, id);
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

	public static void main(String[] args) throws InterruptedException {
		display = new Display();
		App app = new App();
		app.convertImageDataToImages(display.getSystemColor(SWT.COLOR_GRAY));
		Display.setAppName("客户管理程序");
		Display.setAppVersion(_version);
		new Splash(display, app).createContents();
		app.show();
		display.dispose();
	}

}
