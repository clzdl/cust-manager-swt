package com.clzdl.crm.view.common;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

import com.clzdl.crm.Constants;

public class ImgViewDialog extends Shell {
	private Shell parent;
	private Image image;

	public ImgViewDialog(Shell parent, final String imgName) {
		super(parent);
		this.parent = parent;
		ImageData imageData = new ImageLoader().load(System.getProperty("user.dir") + Constants.IMG_SITE + imgName)[0];
		if (null == imageData) {
			return;
		}
		image = new Image(parent.getDisplay(), imageData);
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				// Display the image, then erase the rest
				e.gc.drawImage(image, 0, 0);
			}
		});
		Rectangle bounds = parent.getBounds();
		Rectangle rect = getBounds();
		int x = bounds.x + Math.max(0, (bounds.width - rect.width) / 2);
		int y = bounds.y + Math.max(0, (bounds.height - rect.height) / 2);
		setBounds(x, y, imageData.width, imageData.height);
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
