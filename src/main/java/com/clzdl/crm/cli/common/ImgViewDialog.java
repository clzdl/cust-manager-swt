package com.clzdl.crm.cli.common;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;

import com.clzdl.crm.Constants;

public class ImgViewDialog extends Shell {
	private Image image;
	private Canvas canvas;
	private ScrollBar hBar;
	private ScrollBar vBar;

	public ImgViewDialog(Display display, final String imgName) {
		super(display);
		setLayout(new FillLayout());
		setSize(1024, 736);

		ImageData imageData = new ImageLoader().load(System.getProperty("user.dir") + Constants.IMG_SITE + imgName)[0];
		if (null == imageData) {
			return;
		}
		Rectangle bounds = display.getPrimaryMonitor().getBounds();
		Rectangle rect = getBounds();
		int x = bounds.x + Math.max(0, (bounds.width - rect.width) / 2);
		int y = bounds.y + Math.max(0, (bounds.height - rect.height) / 2);

		int width = Math.min(rect.width, imageData.width);
		int height = Math.min(rect.height, imageData.height);
		setBounds(x, y, width, height);
		image = new Image(display, imageData);

		////
		final Point origin = new Point(0, 0);
		canvas = new Canvas(this, SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE | SWT.V_SCROLL | SWT.H_SCROLL);
		hBar = canvas.getHorizontalBar();
		hBar.addListener(SWT.Selection, e -> {
			int hSelection = hBar.getSelection();
			int destX = -hSelection - origin.x;
			Rectangle imgRect = image.getBounds();
			canvas.scroll(destX, 0, 0, 0, imgRect.width, imgRect.height, false);
			origin.x = -hSelection;
		});

		vBar = canvas.getVerticalBar();
		vBar.addListener(SWT.Selection, e -> {
			int vSelection = vBar.getSelection();
			int destY = -vSelection - origin.y;
			Rectangle imgRect = image.getBounds();
			canvas.scroll(0, destY, 0, 0, imgRect.width, imgRect.height, false);
			origin.y = -vSelection;
		});
		canvas.addListener(SWT.Resize, e -> {
			Rectangle imgRect = image.getBounds();
			Rectangle client = canvas.getClientArea();
			hBar.setMaximum(imgRect.width);
			vBar.setMaximum(imgRect.height);
			hBar.setThumb(Math.min(imgRect.width, client.width));
			vBar.setThumb(Math.min(imgRect.height, client.height));
			int hPage = imgRect.width - client.width;
			int vPage = imgRect.height - client.height;
			int hSelection = hBar.getSelection();
			int vSelection = vBar.getSelection();
			if (hSelection >= hPage) {
				if (hPage <= 0)
					hSelection = 0;
				origin.x = -hSelection;
			}
			if (vSelection >= vPage) {
				if (vPage <= 0)
					vSelection = 0;
				origin.y = -vSelection;
			}
			canvas.redraw();
		});
		canvas.addListener(SWT.Paint, e -> {
			GC gc = e.gc;
			gc.drawImage(image, origin.x, origin.y);
			Rectangle imgRect = image.getBounds();
			Rectangle client = canvas.getClientArea();
			int marginWidth = client.width - imgRect.width;
			if (marginWidth > 0) {
				gc.fillRectangle(imgRect.width, 0, marginWidth, client.height);
			}
			int marginHeight = client.height - imgRect.height;
			if (marginHeight > 0) {
				gc.fillRectangle(0, imgRect.height, client.width, marginHeight);
			}
		});

		open();
		while (!isDisposed()) {
			if (!getDisplay().readAndDispatch()) {
				getDisplay().sleep();
			}
		}
	}

	protected void checkSubclass() {
	}
}
