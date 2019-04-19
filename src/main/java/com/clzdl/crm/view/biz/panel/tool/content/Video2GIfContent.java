package com.clzdl.crm.view.biz.panel.tool.content;

import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clzdl.crm.springboot.auth.EnumSysPermissionProfile;
import com.clzdl.crm.utils.FfmpegHandler;
import com.clzdl.crm.utils.FfmpegHandler.GrabCallbak;
import com.clzdl.crm.view.common.AbstractComposite;
import com.clzdl.crm.view.common.MsgBox;
import com.framework.common.util.date.DateUtil;

public class Video2GIfContent extends AbstractComposite implements GrabCallbak {
	private final static Logger _logger = LoggerFactory.getLogger(Video2GIfContent.class);
	private final static String title = "视频转Gif";
	private DateTime dtEndTime;
	private DateTime dtStartTime;
	private Label videoPreview;
	// 采样频率，数字越大，文件越小，丢失的帧越多，设置为1可保持原帧
	private int frequency = 8;
	private Text edtVideoFile;
	private Text edtOutPath;
	private Text edtFrequency;

	public Video2GIfContent(Composite parent, int style) {
		super(parent, style, title, EnumSysPermissionProfile.NONE);
		setLayout(new FormLayout());
		videoPreview = new Label(this, SWT.NONE);
		Label txtVideoFile = new Label(this, SWT.NONE);
		txtVideoFile.setText("视频文件");

		edtVideoFile = new Text(this, SWT.BORDER | SWT.READ_ONLY);
		Button btnSelFile = new Button(this, SWT.PUSH);
		btnSelFile.setText("...");
		btnSelFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
				String[] filterNames = new String[] { "video Files", "All Files (*)" };
				String[] filterExtensions = new String[] { "*.mp4;*.rmvb;*.avi;*.mkv;", "*" };
				dialog.setFilterNames(filterNames);
				dialog.setFilterExtensions(filterExtensions);
				edtVideoFile.setText(dialog.open());
			}
		});

		Label txtOutPath = new Label(this, SWT.NONE);
		txtOutPath.setText("输出目录");
		edtOutPath = new Text(this, SWT.BORDER | SWT.READ_ONLY);
		Button btnSelPath = new Button(this, SWT.PUSH);
		btnSelPath.setText("...");
		btnSelPath.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				DirectoryDialog dialog = new DirectoryDialog(getShell());
				edtOutPath.setText(dialog.open());
			}
		});
		Label txtStartTime = new Label(this, SWT.NONE);
		txtStartTime.setText("开始时间");
		dtStartTime = new DateTime(this, SWT.TIME);
		dtStartTime.setTime(0, 0, 0);

		Label txtEndTime = new Label(this, SWT.NONE);
		txtEndTime.setText("结束时间");
		dtEndTime = new DateTime(this, SWT.TIME);
		dtEndTime.setTime(0, 0, 0);

		Label txtFrequency = new Label(this, SWT.NONE);
		txtFrequency.setText("采样率");
		edtFrequency = new Text(this, SWT.BORDER);
		edtFrequency.setText(String.valueOf(frequency));

		Button btnConvert = new Button(this, SWT.PUSH);
		btnConvert.setText("转换");

		btnConvert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				final Integer startDur = dtStartTime.getHours() * 60 * 60 + dtStartTime.getMinutes() * 60
						+ dtStartTime.getSeconds();

				final Integer endDur = dtEndTime.getHours() * 60 * 60 + dtEndTime.getMinutes() * 60
						+ dtEndTime.getSeconds();

				if (startDur >= endDur) {
					new MsgBox(getShell(), "请正确选择时间").open();
					return;
				}

				if (edtVideoFile.getText().isEmpty()) {
					new MsgBox(getShell(), "请选择视频文件").open();
					return;
				}

				if (edtOutPath.getText().isEmpty()) {
					new MsgBox(getShell(), "请Gif文件输出路径").open();
					return;
				}
				if (!edtFrequency.getText().trim().isEmpty()) {
					frequency = Integer.valueOf(edtFrequency.getText().trim());
				}
				// 设置循环模式，0为无限循环 这里没有使用源文件的播放次数
				final String gifPathFile = edtOutPath.getText().trim() + "/" + DateUtil.getCurrentDateTime() + ".gif";
				final String videoFile = edtVideoFile.getText().trim();
				final FfmpegHandler handler = new FfmpegHandler();

				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							handler.openMediaStream(videoFile);
							handler.openVideoDecoder();
							handler.grabPicture2Gif(startDur, endDur, gifPathFile, null, frequency,
									Video2GIfContent.this);
						} catch (Exception e) {
							_logger.error(e.getMessage(), e);
						} finally {
							handler.free();
						}
					}
				}).start();
			}
		});
		FormData txtVideoFileFormData = new FormData();
		txtVideoFileFormData.left = new FormAttachment(0, 10);
		txtVideoFileFormData.top = new FormAttachment(0, 10);
		txtVideoFile.setLayoutData(txtVideoFileFormData);

		FormData edtVideoFileFormData = new FormData();
		edtVideoFileFormData.left = new FormAttachment(txtVideoFile, 10);
		edtVideoFileFormData.right = new FormAttachment(80);
		edtVideoFileFormData.top = new FormAttachment(0, 10);
		edtVideoFile.setLayoutData(edtVideoFileFormData);

		FormData btnSelFileFormData = new FormData();
		btnSelFileFormData.left = new FormAttachment(edtVideoFile, 10);
		btnSelFileFormData.top = new FormAttachment(0, 10);
		btnSelFile.setLayoutData(btnSelFileFormData);

		FormData txtOutPathFormData = new FormData();
		txtOutPathFormData.left = new FormAttachment(0, 10);
		txtOutPathFormData.top = new FormAttachment(txtVideoFile, 10);
		txtOutPath.setLayoutData(txtOutPathFormData);

		FormData edtOutPathFormData = new FormData();
		edtOutPathFormData.left = new FormAttachment(txtOutPath, 10);
		edtOutPathFormData.right = new FormAttachment(80);
		edtOutPathFormData.top = new FormAttachment(txtVideoFile, 10);
		edtOutPath.setLayoutData(edtOutPathFormData);

		FormData btnSelPathFormData = new FormData();
		btnSelPathFormData.left = new FormAttachment(edtOutPath, 10);
		btnSelPathFormData.top = new FormAttachment(txtVideoFile, 10);
		btnSelPath.setLayoutData(btnSelPathFormData);

		FormData txtStartTimeFormData = new FormData();
		txtStartTimeFormData.left = new FormAttachment(0, 10);
		txtStartTimeFormData.top = new FormAttachment(txtOutPath, 10);
		txtStartTime.setLayoutData(txtStartTimeFormData);

		FormData dtStartTimeFormData = new FormData();
		dtStartTimeFormData.left = new FormAttachment(txtStartTime, 10);
		dtStartTimeFormData.top = new FormAttachment(txtOutPath, 10);
		dtStartTime.setLayoutData(dtStartTimeFormData);

		FormData txtEndTimeFormData = new FormData();
		txtEndTimeFormData.left = new FormAttachment(dtStartTime, 10);
		txtEndTimeFormData.top = new FormAttachment(txtOutPath, 10);
		txtEndTime.setLayoutData(txtEndTimeFormData);

		FormData dtEndTimeFormData = new FormData();
		dtEndTimeFormData.left = new FormAttachment(txtEndTime, 10);
		dtEndTimeFormData.top = new FormAttachment(txtOutPath, 10);
		dtEndTime.setLayoutData(dtEndTimeFormData);

		FormData txtFrequencyFormData = new FormData();
		txtFrequencyFormData.left = new FormAttachment(dtEndTime, 10);
		txtFrequencyFormData.top = new FormAttachment(txtOutPath, 10);
		txtFrequency.setLayoutData(txtFrequencyFormData);

		FormData edtFrequencyFormData = new FormData();
		edtFrequencyFormData.left = new FormAttachment(txtFrequency, 10);
		edtFrequencyFormData.top = new FormAttachment(txtOutPath, 10);
		edtFrequencyFormData.right = new FormAttachment(txtFrequency, 100);
		edtFrequency.setLayoutData(edtFrequencyFormData);

		FormData btnConvertFormData = new FormData();
		btnConvertFormData.left = new FormAttachment(edtFrequency, 10);
		btnConvertFormData.top = new FormAttachment(txtOutPath, 10);

		btnConvert.setLayoutData(btnConvertFormData);

		FormData videoPreviewFormData = new FormData();
		videoPreviewFormData.left = new FormAttachment(0, 10);
		videoPreviewFormData.right = new FormAttachment(100, -10);
		videoPreviewFormData.top = new FormAttachment(txtStartTime, 10);
		videoPreviewFormData.bottom = new FormAttachment(100);
		videoPreview.setLayoutData(videoPreviewFormData);
	}

	@Override
	public void showPictrue(BufferedImage image) {
		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				videoPreview.setImage(new Image(getDisplay(), convertToSWT(image)));
			}
		});
	}

	private ImageData convertToSWT(BufferedImage bufferedImage) {
		if (bufferedImage.getColorModel() instanceof DirectColorModel) {
			DirectColorModel colorModel = (DirectColorModel) bufferedImage.getColorModel();
			PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(),
					colorModel.getBlueMask());
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(),
					colorModel.getPixelSize(), palette);
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[3];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					int pixel = palette.getPixel(new RGB(pixelArray[0], pixelArray[1], pixelArray[2]));
					data.setPixel(x, y, pixel);
				}
			}
			return data;
		} else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
			IndexColorModel colorModel = (IndexColorModel) bufferedImage.getColorModel();
			int size = colorModel.getMapSize();
			byte[] reds = new byte[size];
			byte[] greens = new byte[size];
			byte[] blues = new byte[size];
			colorModel.getReds(reds);
			colorModel.getGreens(greens);
			colorModel.getBlues(blues);
			RGB[] rgbs = new RGB[size];
			for (int i = 0; i < rgbs.length; i++) {
				rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
			}
			PaletteData palette = new PaletteData(rgbs);
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(),
					colorModel.getPixelSize(), palette);
			data.transparentPixel = colorModel.getTransparentPixel();
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[1];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					data.setPixel(x, y, pixelArray[0]);
				}
			}
			return data;
		} else if (bufferedImage.getColorModel() instanceof ComponentColorModel) {
			ComponentColorModel colorModel = (ComponentColorModel) bufferedImage.getColorModel();
			// ASSUMES: 3 BYTE BGR IMAGE TYPE
			PaletteData palette = new PaletteData(0x0000FF, 0x00FF00, 0xFF0000);
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(),
					colorModel.getPixelSize(), palette);
			// This is valid because we are using a 3-byte Data model with no transparent
			// pixels
			data.transparentPixel = -1;
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[3];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					int pixel = palette.getPixel(new RGB(pixelArray[0], pixelArray[1], pixelArray[2]));
					data.setPixel(x, y, pixel);
				}
			}
			return data;
		}
		return null;
	}

	@Override
	public void grabDone() {
		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				new MsgBox(getShell(), "抓取完成").open();
			}
		});
	}
}
