package com.clzdl.crm.cli.biz.panel.tool.content;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clzdl.crm.cli.common.AbstractComposite;
import com.clzdl.crm.cli.common.MsgBox;
import com.clzdl.crm.common.enums.EnumSysPermissionProfile;
import com.clzdl.crm.utils.FfmpegHandler;
import com.clzdl.crm.utils.FfmpegHandler.GrabCallbak;
import com.clzdl.crm.utils.ImageConvertor;
import com.framework.common.util.date.DateUtil;
import com.madgag.gif.fmsware.AnimatedGifEncoder;

public class Video2GIfContent extends AbstractComposite implements GrabCallbak {
	private final static Logger _logger = LoggerFactory.getLogger(Video2GIfContent.class);
	private final static String title = "视频转Gif";
	//// 延迟时间 ms
	private final static Integer _delayTime = 1000;
	private DateTime dtEndTime;
	private DateTime dtStartTime;
	private Label videoPreview;
	// 采样频率，数字越大，文件越小，丢失的帧越多，设置为1可保持原帧
	private int frequency = 8;
	private Text edtVideoFile;
	// 输出目录
	private Text edtOutPath;
	private Text edtFrequency;
	/// 多图转gif
	private Text edtImgInPath;

	public Video2GIfContent(Composite parent, int style) {
		super(parent, style, title, EnumSysPermissionProfile.NONE);
		setLayout(new FormLayout());

		Group commonGroup = new Group(this, SWT.BORDER);
		commonGroup.setLayout(new FormLayout());
		createCommonContent(commonGroup);

		Group videoGroup = new Group(this, SWT.BORDER);
		videoGroup.setLayout(new FormLayout());

		createVideoContent(videoGroup);
		Group imgsGroup = new Group(this, SWT.BORDER);

		imgsGroup.setLayout(new FormLayout());
		createImgContent(imgsGroup);

		videoPreview = new Label(this, SWT.NONE);

		FormData commonGroupFormData = new FormData();
		commonGroupFormData.top = new FormAttachment(0, 10);
		commonGroupFormData.left = new FormAttachment(0, 10);
		commonGroupFormData.right = new FormAttachment(100, -10);
		commonGroupFormData.bottom = new FormAttachment(20);
		commonGroup.setLayoutData(commonGroupFormData);

		FormData videoGroupFormData = new FormData();
		videoGroupFormData.top = new FormAttachment(commonGroup, 10);
		videoGroupFormData.left = new FormAttachment(0, 10);
		videoGroupFormData.right = new FormAttachment(100, -10);
		videoGroupFormData.bottom = new FormAttachment(50);
		videoGroup.setLayoutData(videoGroupFormData);

		FormData imgsGroupFormData = new FormData();
		imgsGroupFormData.top = new FormAttachment(videoGroup, 10);
		imgsGroupFormData.left = new FormAttachment(0, 10);
		imgsGroupFormData.right = new FormAttachment(100, -10);
		imgsGroupFormData.bottom = new FormAttachment(65);
		imgsGroup.setLayoutData(imgsGroupFormData);

		FormData videoPreviewFormData = new FormData();
		videoPreviewFormData.left = new FormAttachment(0, 10);
		videoPreviewFormData.right = new FormAttachment(100, -10);
		videoPreviewFormData.top = new FormAttachment(imgsGroup, 10);
		videoPreviewFormData.bottom = new FormAttachment(100);
		videoPreview.setLayoutData(videoPreviewFormData);
	}

	@Override
	public void showPictrue(BufferedImage image) {
		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				videoPreview.setImage(new Image(getDisplay(), ImageConvertor.convertToSWT(image)));
			}
		});
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

	private void createCommonContent(Group group) {
		Label txtOutPath = new Label(group, SWT.NONE);
		txtOutPath.setText("输出目录");
		edtOutPath = new Text(group, SWT.BORDER | SWT.READ_ONLY);

		Button btnSelPath = new Button(group, SWT.PUSH);
		btnSelPath.setText("...");
		btnSelPath.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				DirectoryDialog dialog = new DirectoryDialog(getShell());
				edtOutPath.setText(dialog.open());
			}
		});

		Label txtFrequency = new Label(group, SWT.NONE);
		txtFrequency.setText("采样率");
		edtFrequency = new Text(group, SWT.BORDER);
		edtFrequency.setText(String.valueOf(frequency));

		FormData txtOutPathFormData = new FormData();
		txtOutPathFormData.left = new FormAttachment(0, 10);
		txtOutPathFormData.top = new FormAttachment(0, 10);
		txtOutPath.setLayoutData(txtOutPathFormData);

		FormData edtOutPathFormData = new FormData();
		edtOutPathFormData.left = new FormAttachment(txtOutPath, 10);
		edtOutPathFormData.right = new FormAttachment(80);
		edtOutPathFormData.top = new FormAttachment(0, 10);
		edtOutPath.setLayoutData(edtOutPathFormData);

		FormData btnSelPathFormData = new FormData();
		btnSelPathFormData.left = new FormAttachment(edtOutPath, 10);
		btnSelPathFormData.top = new FormAttachment(0, 10);
		btnSelPath.setLayoutData(btnSelPathFormData);

		FormData txtFrequencyFormData = new FormData();
		txtFrequencyFormData.left = new FormAttachment(0, 10);
		txtFrequencyFormData.top = new FormAttachment(edtOutPath, 10);
		txtFrequency.setLayoutData(txtFrequencyFormData);

		FormData edtFrequencyFormData = new FormData();
		edtFrequencyFormData.left = new FormAttachment(txtFrequency, 10);
		edtFrequencyFormData.top = new FormAttachment(edtOutPath, 10);
		edtFrequencyFormData.right = new FormAttachment(txtFrequency, 100);
		edtFrequency.setLayoutData(edtFrequencyFormData);
	}

	private void createVideoContent(Group group) {
		Label txtVideoFile = new Label(group, SWT.NONE);
		txtVideoFile.setText("视频文件");

		edtVideoFile = new Text(group, SWT.BORDER | SWT.READ_ONLY);
		Button btnSelFile = new Button(group, SWT.PUSH);
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

		Label txtStartTime = new Label(group, SWT.NONE);
		txtStartTime.setText("开始时间");
		dtStartTime = new DateTime(group, SWT.TIME);
		dtStartTime.setTime(0, 0, 0);

		Label txtEndTime = new Label(group, SWT.NONE);
		txtEndTime.setText("结束时间");
		dtEndTime = new DateTime(group, SWT.TIME);
		dtEndTime.setTime(0, 0, 0);

		Button btnConvert = new Button(group, SWT.PUSH);
		btnConvert.setText("视频转GIF");

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

		FormData txtStartTimeFormData = new FormData();
		txtStartTimeFormData.left = new FormAttachment(0, 10);
		txtStartTimeFormData.top = new FormAttachment(txtVideoFile, 10);
		txtStartTime.setLayoutData(txtStartTimeFormData);

		FormData dtStartTimeFormData = new FormData();
		dtStartTimeFormData.left = new FormAttachment(txtStartTime, 10);
		dtStartTimeFormData.top = new FormAttachment(txtVideoFile, 10);
		dtStartTime.setLayoutData(dtStartTimeFormData);

		FormData txtEndTimeFormData = new FormData();
		txtEndTimeFormData.left = new FormAttachment(dtStartTime, 10);
		txtEndTimeFormData.top = new FormAttachment(txtVideoFile, 10);
		txtEndTime.setLayoutData(txtEndTimeFormData);

		FormData dtEndTimeFormData = new FormData();
		dtEndTimeFormData.left = new FormAttachment(txtEndTime, 10);
		dtEndTimeFormData.top = new FormAttachment(txtVideoFile, 10);
		dtEndTime.setLayoutData(dtEndTimeFormData);

		FormData btnConvertFormData = new FormData();
		btnConvertFormData.left = new FormAttachment(dtEndTime, 10);
		btnConvertFormData.top = new FormAttachment(txtVideoFile, 10);
		btnConvert.setLayoutData(btnConvertFormData);
	}

	private void createImgContent(Group group) {
		Label txtImgs = new Label(group, SWT.NONE);
		txtImgs.setText("图片目录");
		edtImgInPath = new Text(group, SWT.BORDER);
		Button btnImgConvert = new Button(group, SWT.PUSH);
		btnImgConvert.setText("多图转GIF");

		btnImgConvert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				if (edtOutPath.getText().trim().isEmpty()) {
					new MsgBox(getShell(), "请选择输出目录").open();
					return;
				}
				if (edtImgInPath.getText().trim().isEmpty()) {
					new MsgBox(getShell(), "请选择图片目录").open();
					return;
				}
				File file = new File(edtImgInPath.getText().trim());
				File[] fs = file.listFiles();
				final List<String> imgFiles = new ArrayList<String>();
				for (File f : fs) {
					if (f.isDirectory()) {
						continue;
					}
					imgFiles.add(f.getAbsolutePath());
				}

				if (imgFiles.isEmpty()) {
					new MsgBox(getShell(), "目录中无图片文件").open();
					return;
				}

				final AnimatedGifEncoder encoderGif = new AnimatedGifEncoder();
				encoderGif.setRepeat(0);
				encoderGif.start(edtOutPath.getText().trim() + "/" + DateUtil.getCurrentDateTime() + ".gif");
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							for (String fileName : imgFiles) {
								ImageData[] loadingImageData = new ImageLoader().load(fileName);
								Image image = new Image(getDisplay(), loadingImageData[0]);
								encoderGif.setDelay(_delayTime / 2);
								getDisplay().asyncExec(new Runnable() {
									@Override
									public void run() {
										videoPreview.setImage(image);
									}
								});

								Thread.sleep(100);

								encoderGif.addFrame(ImageConvertor.convertToAWT(loadingImageData[0]));
								image.dispose();
							}
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}).start();

				encoderGif.finish();
				new MsgBox(getShell(), "文件生成完毕").open();
			}
		});

		Button btnDirSel = new Button(group, SWT.PUSH);
		btnDirSel.setText("...");
		btnDirSel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				DirectoryDialog dialog = new DirectoryDialog(getShell());
				edtImgInPath.setText(dialog.open());
			}
		});

		FormData txtImgsFormData = new FormData();
		txtImgsFormData.left = new FormAttachment(0, 10);
		txtImgsFormData.top = new FormAttachment(0, 10);
		txtImgs.setLayoutData(txtImgsFormData);

		FormData edtImgInPathFormData = new FormData();
		edtImgInPathFormData.left = new FormAttachment(txtImgs, 10);
		edtImgInPathFormData.right = new FormAttachment(60);
		edtImgInPathFormData.top = new FormAttachment(0, 10);
		edtImgInPath.setLayoutData(edtImgInPathFormData);

		FormData btnDirSelFormData = new FormData();
		btnDirSelFormData.left = new FormAttachment(edtImgInPath, 10);
		btnDirSelFormData.top = new FormAttachment(0, 10);
		btnDirSel.setLayoutData(btnDirSelFormData);

		FormData btnImgConvertFormData = new FormData();
		btnImgConvertFormData.left = new FormAttachment(btnDirSel, 10);
		btnImgConvertFormData.top = new FormAttachment(0, 10);
		btnImgConvert.setLayoutData(btnImgConvertFormData);
	}
}
