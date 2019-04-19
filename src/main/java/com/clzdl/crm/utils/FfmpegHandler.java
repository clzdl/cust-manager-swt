package com.clzdl.crm.utils;

import static org.bytedeco.ffmpeg.global.avcodec.av_packet_unref;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_alloc_context3;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_close;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_find_decoder;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_flush_buffers;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_free_context;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_open2;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_parameters_to_context;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_receive_frame;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_send_packet;
import static org.bytedeco.ffmpeg.global.avformat.AVSEEK_FLAG_BACKWARD;
import static org.bytedeco.ffmpeg.global.avformat.av_dump_format;
import static org.bytedeco.ffmpeg.global.avformat.av_read_frame;
import static org.bytedeco.ffmpeg.global.avformat.av_seek_frame;
import static org.bytedeco.ffmpeg.global.avformat.avformat_close_input;
import static org.bytedeco.ffmpeg.global.avformat.avformat_find_stream_info;
import static org.bytedeco.ffmpeg.global.avformat.avformat_open_input;
import static org.bytedeco.ffmpeg.global.avutil.AVMEDIA_TYPE_VIDEO;
import static org.bytedeco.ffmpeg.global.avutil.AV_PIX_FMT_BGR24;
import static org.bytedeco.ffmpeg.global.avutil.av_frame_alloc;
import static org.bytedeco.ffmpeg.global.avutil.av_frame_free;
import static org.bytedeco.ffmpeg.global.avutil.av_image_fill_arrays;
import static org.bytedeco.ffmpeg.global.avutil.av_image_get_buffer_size;
import static org.bytedeco.ffmpeg.global.avutil.av_malloc;
import static org.bytedeco.ffmpeg.global.avutil.av_q2d;
import static org.bytedeco.ffmpeg.global.swscale.SWS_FAST_BILINEAR;
import static org.bytedeco.ffmpeg.global.swscale.sws_getContext;
import static org.bytedeco.ffmpeg.global.swscale.sws_scale;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.bytedeco.ffmpeg.avcodec.AVCodec;
import org.bytedeco.ffmpeg.avcodec.AVCodecContext;
import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avformat.AVStream;
import org.bytedeco.ffmpeg.avutil.AVFrame;
import org.bytedeco.ffmpeg.swscale.SwsContext;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.PointerPointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clzdl.crm.ExceptionMessage;
import com.framework.common.exception.BizException;
import com.madgag.gif.fmsware.AnimatedGifEncoder;

public class FfmpegHandler {
	private final static Logger logger = LoggerFactory.getLogger(FfmpegHandler.class);
	private AVFormatContext fmtCtx = null;
	private Integer videoStreamIndex = -1;
	private AVStream videoStream = null; /// 视频流
	private AVCodecContext codecCtx = null;

	public static interface GrabCallbak {
		/**
		 * 抓取一帧图片后的回调
		 * 
		 * @param image
		 */
		void showPictrue(BufferedImage image);

		/**
		 * 抓取完成后回调
		 */
		void grabDone();

	}

	/**
	 * 打开多媒体文件流
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	public void openMediaStream(String filePath) throws Exception {
		fmtCtx = new AVFormatContext(null);
		if (avformat_open_input(fmtCtx, filePath, null, null) < 0) {
			logger.error("Open video file {} failed", filePath);
			throw new BizException(ExceptionMessage.FFMPEG_HANLE_WRONG);
		}
		// i dont know but without this function, sws_getContext does not work
		if (avformat_find_stream_info(fmtCtx, (PointerPointer) null) < 0) {
			throw new BizException(ExceptionMessage.FFMPEG_HANLE_WRONG);
		}
		av_dump_format(fmtCtx, 0, filePath, 0);
	}

	/**
	 * 打开视频流解码器 offset: 偏移秒数
	 * 
	 * @throws Exception
	 */
	public void openVideoDecoder() throws Exception {
		for (int i = 0; i < fmtCtx.nb_streams(); i++) {
			if (fmtCtx.streams(i).codecpar().codec_type() == AVMEDIA_TYPE_VIDEO) {
				videoStreamIndex = i;
				break;
			}
		}
		if (videoStreamIndex == -1) {
			logger.error("Cannot find video stream");
			throw new BizException(ExceptionMessage.FFMPEG_HANLE_WRONG);
		} else {
			videoStream = fmtCtx.streams(videoStreamIndex);
			logger.info("Video stream {} with resolution {}*{}\n", videoStreamIndex, videoStream.codecpar().width(),
					videoStream.codecpar().height());
		}

		codecCtx = avcodec_alloc_context3(null);
		avcodec_parameters_to_context(codecCtx, videoStream.codecpar());

		AVCodec codec = avcodec_find_decoder(codecCtx.codec_id());
		if (codec == null) {
			logger.error("Unsupported codec for video file");
			throw new BizException(ExceptionMessage.FFMPEG_HANLE_WRONG);
		}
		if (avcodec_open2(codecCtx, codec, (PointerPointer) null) < 0) {
			logger.error("Can not open codec");
			throw new BizException(ExceptionMessage.FFMPEG_HANLE_WRONG);
		}
		logger.info("openVideoDecoder success.");
	}

	/**
	 * 抓取一段视频转换成gif文件
	 * 
	 * @param begSec
	 * @param endSec
	 * @param gifFile
	 * @param gifDelayTime
	 * @throws Exception
	 */
	public void grabPicture2Gif(int begSec, int endSec, String gifFile, Integer gifDelayTime, Integer frequency,
			GrabCallbak cb) throws Exception {
		long move2Pts = 0;
		AnimatedGifEncoder encoderGif = new AnimatedGifEncoder();
		AVFrame frm = av_frame_alloc();
		// Allocate an AVFrame structure
		AVFrame pFrameRGB = av_frame_alloc();
		try {
			encoderGif.setRepeat(0);
			encoderGif.start(gifFile);

			if (begSec > 0) {
				move2Pts = (long) (begSec / av_q2d(videoStream.time_base()));
				move2Pts += videoStream.start_time();
				logger.info("move2Pts:{}", move2Pts);
				int retSeek = av_seek_frame(fmtCtx, videoStreamIndex, move2Pts, AVSEEK_FLAG_BACKWARD);
				if (retSeek < 0) {
					logger.error("av_seek_frame fail");
				}
				avcodec_flush_buffers(codecCtx);
			}

			long endSecBaseStream = (long) (endSec / av_q2d(videoStream.time_base()));

			// Determine required buffer size and allocate buffer
			int numBytes = av_image_get_buffer_size(AV_PIX_FMT_BGR24, codecCtx.width(), codecCtx.height(), 1);
			BytePointer buffer = new BytePointer(av_malloc(numBytes));
			SwsContext sws_ctx = sws_getContext(codecCtx.width(), codecCtx.height(), codecCtx.pix_fmt(),
					codecCtx.width(), codecCtx.height(), AV_PIX_FMT_BGR24, SWS_FAST_BILINEAR, null, null,
					(DoublePointer) null);

			if (sws_ctx == null) {
				logger.error("Can not use sws");
				throw new BizException(ExceptionMessage.FFMPEG_HANLE_WRONG);
			}

			av_image_fill_arrays(pFrameRGB.data(), pFrameRGB.linesize(), buffer, AV_PIX_FMT_BGR24, codecCtx.width(),
					codecCtx.height(), 1);

			int ret1 = -1, ret2 = -1;
			long cur = 0;
			AVPacket pkt = new AVPacket();
			while (av_read_frame(fmtCtx, pkt) >= 0) {
				if (pkt.stream_index() == videoStreamIndex) {
					ret1 = avcodec_send_packet(codecCtx, pkt);
					ret2 = avcodec_receive_frame(codecCtx, frm);
					logger.info("ret1 {} ret2 {}", ret1, ret2);
					// if not check ret2, error occur [swscaler @ 0x1cb3c40] bad src image pointers
					// ret2 same as fi
					if (ret2 >= 0) {
						logger.info(
								"frm:{}, duration (on base stream unit):{} ,move2Pts:{},(frm.pts() + frm.pkt_duration()):{}",
								frm.pts(), frm.pkt_duration(), move2Pts, (frm.pts() + frm.pkt_duration()));
						if (frm.pts() < move2Pts && (frm.pts() + frm.pkt_duration()) < move2Pts) {
							continue;
						}
						logger.info("gif qts:{},duration:{}", frm.pts(), pkt.duration());
						if (frequency >= 1 && cur++ % frequency != 0) {
							continue;
						}
						sws_scale(sws_ctx, frm.data(), frm.linesize(), 0, codecCtx.height(), pFrameRGB.data(),
								pFrameRGB.linesize());
						BufferedImage image = toImage(pFrameRGB, codecCtx.width(), codecCtx.height());
						if (null != cb) {
							cb.showPictrue(image);
						}
						encoderGif.addFrame(image);
						int delay = gifDelayTime == null ? (int) (frm.pkt_duration() * av_q2d(videoStream.time_base()))
								: gifDelayTime;
						encoderGif.setDelay(delay);
						if (frm.pts() > endSecBaseStream) {
							break;
						}
					}
				}
				av_packet_unref(pkt);
			}
			encoderGif.finish();
			if (null != cb) {
				cb.grabDone();
			}
			logger.info("gif file build success");
		} finally {
			av_frame_free(frm);
			av_frame_free(pFrameRGB);
		}
	}

	/**
	 * 释放资源
	 */
	public void free() {
		avcodec_close(codecCtx);
		avcodec_free_context(codecCtx);
		avformat_close_input(fmtCtx);
	}

	/**
	 * Write image data using simple image format ppm
	 * 
	 * @see https://en.wikipedia.org/wiki/Netpbm_format
	 */
	private BufferedImage toImage(AVFrame pFrame, int width, int height) throws IOException {
		ByteBuffer byteBuffer = convertAvFrame2ByteBuffer(pFrame, width, height);
		return BGR2BufferedImage(byteBuffer, width, height);
	}

	private ByteBuffer convertAvFrame2ByteBuffer(AVFrame pFrame, int width, int height) {
		BytePointer data = pFrame.data(0);
		int size = width * height * 3;
		ByteBuffer buf = data.position(0).limit(size).asBuffer();
		return buf;
	}

	/**
	 * 24位BGR转BufferedImage
	 * 
	 * @param src   -源数据
	 * @param width -宽度
	 * @param       height-高度
	 * @return
	 */
	private BufferedImage BGR2BufferedImage(ByteBuffer src, int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		Raster ra = image.getRaster();
		DataBuffer out = ra.getDataBuffer();
		DataBufferByte db = (DataBufferByte) out;
		ByteBuffer.wrap(db.getData()).put(src);
		return image;
	}

//	public static void main(String[] args) throws Exception {
//		FfmpegHandler handler = new FfmpegHandler();
//		try {
//			// handler.openMediaStream("/home/java/阳光电影www.ygdy8.com.检察方的罪人.BD.720p.日语中字.mkv");
//			handler.openMediaStream("/home/java/093006300116dbb404063082.mp4");
//			handler.openVideoDecoder();
//			handler.grabPicture2Gif(4, 5, "/home/java/bbbbbbb.gif", null, 1, null);
//		} finally {
//			handler.free();
//		}
//	}
}
