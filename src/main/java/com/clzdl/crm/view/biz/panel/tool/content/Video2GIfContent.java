package com.clzdl.crm.view.biz.panel.tool.content;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
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

import com.clzdl.crm.App;
import com.clzdl.crm.springboot.auth.EnumSysPermissionProfile;
import com.clzdl.crm.utils.ImageConvertor;
import com.clzdl.crm.view.common.AbstractComposite;
import com.clzdl.crm.view.common.LoadingDialog;
import com.clzdl.crm.view.common.LoadingDialog.TaskLoading;
import com.clzdl.crm.view.common.MsgBox;
import com.framework.common.util.date.DateUtil;
import com.madgag.gif.fmsware.AnimatedGifEncoder;

import io.humble.video.Decoder;
import io.humble.video.Demuxer;
import io.humble.video.Demuxer.SeekFlag;
import io.humble.video.DemuxerStream;
import io.humble.video.Global;
import io.humble.video.MediaDescriptor;
import io.humble.video.MediaPacket;
import io.humble.video.MediaPicture;
import io.humble.video.Rational;
import io.humble.video.awt.MediaPictureConverter;
import io.humble.video.awt.MediaPictureConverterFactory;

public class Video2GIfContent extends AbstractComposite {
	private final static Logger _logger = LoggerFactory.getLogger(Video2GIfContent.class);
	private final static Integer _demuxer_in_buffer_size = 4 * 1024 * 1024;
	private final static String title = "视频转Gif";
	private DateTime dtEndTime;
	private DateTime dtStartTime;
	private Label videoPreview;
	private BufferedImage currShowImage = null;
	// 采样频率，数字越大，文件越小，丢失的帧越多，设置为1可保持原帧
	private int frequency = 8;
	private int count = 0;
	private long recordStartFrmNumber = 0;
	private long recordEndFrmNumber = 0;
	private long leftRecordFrmNumber = 0;
	private Text edtVideoFile;
	private Text edtOutPath;
	private Text edtFrequency;
	private String gifFileName;
	//// 解析到截取，同步使用
	private CountDownLatch countDownLatch;
	AnimatedGifEncoder encoderGif = new AnimatedGifEncoder();

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
				clear();
				// 设置循环模式，0为无限循环 这里没有使用源文件的播放次数
				encoderGif.setRepeat(0);
				gifFileName = DateUtil.getCurrentDateTime() + ".gif";
				encoderGif.start(edtOutPath.getText().trim() + "/" + gifFileName);
				final String videoFile = edtVideoFile.getText().trim();

				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							convertVideo2Gif(videoFile, startDur, endDur);
						} catch (InterruptedException | IOException e1) {
							_logger.error(e1.getMessage(), e1);
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

	private void convertVideo2Gif(String videoFile, Integer startDur, Integer endDur)
			throws InterruptedException, IOException {
		countDownLatch = null;
		countDownLatch = new CountDownLatch(1);

		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				LoadingDialog loading = new LoadingDialog(App.getMainWindow(), App.getLoadingImgages());
				loading.start(new TaskLoading() {

					@Override
					public void doing() {
						try {
							countDownLatch.await();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});

		/*
		 * Start by creating a container object, in this case a demuxer since we are
		 * reading, to get video data from.
		 */
		Demuxer demuxer = Demuxer.make();
		demuxer.setInputBufferLength(_demuxer_in_buffer_size);
		/*
		 * Open the demuxer with the filename passed on.
		 */
		demuxer.open(videoFile, null, false, true, null, null);

		/*
		 * Query how many streams the call to open found
		 */
		int numStreams = demuxer.getNumStreams();

		/*
		 * Iterate through the streams to find the first video stream
		 */
		int videoStreamId = -1;
		long streamStartTime = Global.NO_PTS;
		Decoder videoDecoder = null;
		for (int i = 0; i < numStreams; i++) {
			final DemuxerStream stream = demuxer.getStream(i);
			streamStartTime = stream.getStartTime();
			final Decoder decoder = stream.getDecoder();
			if (decoder != null && decoder.getCodecType() == MediaDescriptor.Type.MEDIA_VIDEO) {
				videoStreamId = i;
				videoDecoder = decoder;
				// stop at the first one.
				break;
			}
		}
		if (videoStreamId == -1)
			throw new RuntimeException("could not find video stream in container: " + videoFile);

		/*
		 * Now we have found the video stream in this file. Let's open up our decoder so
		 * it can do work.
		 */
		videoDecoder.open(null, null);
		final MediaPicture picture = MediaPicture.make(videoDecoder.getWidth(), videoDecoder.getHeight(),
				videoDecoder.getPixelFormat());
		/**
		 * A converter object we'll use to convert the picture in the video to a BGR_24
		 * format that Java Swing can work with. You can still access the data directly
		 * in the MediaPicture if you prefer, but this abstracts away from this demo
		 * most of that byte-conversion work. Go read the source code for the converters
		 * if you're a glutton for punishment.
		 */
		final MediaPictureConverter converter = MediaPictureConverterFactory
				.createConverter(MediaPictureConverterFactory.HUMBLE_BGR_24, picture);
		/**
		 * This is the Window we will display in. See the code for this if you're
		 * curious, but to keep this demo clean we're 'simplifying' Java AWT UI updating
		 * code. This method just creates a single window on the UI thread, and blocks
		 * until it is displayed.
		 */

		// All the MediaPicture objects decoded from the videoDecoder will share this
		// timebase.
		final Rational streamTimebase = videoDecoder.getTimeBase();
		recordStartFrmNumber = convertDuration2PictureFrmNumber(streamTimebase, startDur);
		recordEndFrmNumber = convertDuration2PictureFrmNumber(streamTimebase, endDur);
		leftRecordFrmNumber = recordEndFrmNumber - recordStartFrmNumber + 1;

		int ret = demuxer.seek(videoStreamId, 0, recordStartFrmNumber, recordEndFrmNumber,
				SeekFlag.SEEK_FRAME.swigValue());
		System.out.println("recordStartFrmNumber=" + recordStartFrmNumber + ",recordEndFrmNumber:" + recordEndFrmNumber
				+ ",demuxer in buffer:" + demuxer.getInputBufferLength() + ",ret:" + ret);
		countDownLatch.countDown();
		/**
		 * Now, we start walking through the container looking at each packet. This is a
		 * decoding loop, and as you work with Humble you'll write a lot of these.
		 * 
		 * Notice how in this loop we reuse all of our objects to avoid reallocating
		 * them. Each call to Humble resets objects to avoid unnecessary reallocation.
		 */
		final MediaPacket packet = MediaPacket.make();
		while (demuxer.read(packet) >= 0) {
			/**
			 * Now we have a packet, let's see if it belongs to our video stream
			 */
			if (packet.getStreamIndex() == videoStreamId) {
				/**
				 * A packet can actually contain multiple sets of samples (or frames of samples
				 * in decoding speak). So, we may need to call decode multiple times at
				 * different offsets in the packet's data. We capture that here.
				 */
				int offset = 0;
				int bytesRead = 0;
				do {
					bytesRead += videoDecoder.decode(picture, packet, offset);
					if (picture.isComplete()) {
						displayVideoAtCorrectTime(streamStartTime, picture, converter, streamTimebase);
						if (0 >= leftRecordFrmNumber--) {
							// 截取结束
							break;
						}
					}
					offset += bytesRead;
				} while (offset < packet.getSize());
				if (0 >= leftRecordFrmNumber) {
					// 截取结束
					break;
				}
			}
		}

		// Some video decoders (especially advanced ones) will cache
		// video data before they begin decoding, so when you are done you need
		// to flush them. The convention to flush Encoders or Decoders in Humble Video
		// is to keep passing in null until incomplete samples or packets are returned.
		do {
			videoDecoder.decode(picture, null, 0);
			if (picture.isComplete()) {
				displayVideoAtCorrectTime(streamStartTime, picture, converter, streamTimebase);
			}
		} while (picture.isComplete());

		// It is good practice to close demuxers when you're done to free
		// up file handles. Humble will EVENTUALLY detect if nothing else
		// references this demuxer and close it then, but get in the habit
		// of cleaning up after yourself, and your future girlfriend/boyfriend
		// will appreciate it.
		demuxer.close();
		encoderGif.finish();

		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				new MsgBox(getShell(), "Gif文件[" + gifFileName + "]截取成功").open();
			}
		});
	}

	/**
	 * Takes the video picture and displays it at the right time.
	 */
	private void displayVideoAtCorrectTime(long streamStartTime, final MediaPicture picture,
			final MediaPictureConverter converter, final Rational streamTimebase) throws InterruptedException {
		System.out.println(picture.getTimeStamp());
		Long s = streamTimebase.rescale((long) (1000 * streamTimebase.getValue()), Rational.make(1, 24));
		if (count++ % frequency == 0) {
			// finally, convert the image from Humble format into Java images.
			currShowImage = converter.toImage(currShowImage, picture);
			// And ask the UI thread to repaint with the new image.
			getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					videoPreview.setImage(new Image(getDisplay(), ImageConvertor.convertToSWT(currShowImage)));
				}
			});
			encoderGif.addFrame(currShowImage);
		}
		System.out.println("s:" + s + ",streamBase:" + streamTimebase.toString());
		Thread.sleep(s);
	}

	private BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	private long convertDuration2PictureFrmNumber(Rational streamTimebase, long duration) {
		return (long) ((duration / streamTimebase.getValue())
				/ (Rational.make(1, 24).getValue() / streamTimebase.getValue()));
	}

	private void clear() {
		count = 0;
		recordStartFrmNumber = 0;
		recordEndFrmNumber = 0;
		leftRecordFrmNumber = 0;
		gifFileName = "";
	}
}
