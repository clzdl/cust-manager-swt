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
import static org.bytedeco.ffmpeg.global.avformat.AVSEEK_FLAG_ANY;
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

import com.madgag.gif.fmsware.AnimatedGifEncoder;

public class ReadFewFrame {

	/**
	 * Write image data using simple image format ppm
	 * 
	 * @see https://en.wikipedia.org/wiki/Netpbm_format
	 */
	static void save_frame(AVFrame pFrame, int width, int height, AnimatedGifEncoder encoderGif) throws IOException {
		ByteBuffer byteBuffer = convertAvFrame2ByteBuffer(pFrame, width, height);
		encoderGif.addFrame(BGR2BufferedImage(byteBuffer, width, height));
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Read few frame and write to image");
		AnimatedGifEncoder encoderGif = new AnimatedGifEncoder();
		encoderGif.setRepeat(0);
		encoderGif.setDelay(40);
		encoderGif.start("/home/java/fffffffffff.gif");
		int ret = -1, i = 0, v_stream_idx = -1;
//		String vf_path = "/home/java/093006300116dbb404063082.mp4";
		String vf_path = "/home/java/阳光电影www.ygdy8.com.检察方的罪人.BD.720p.日语中字.mkv";

		AVFormatContext fmt_ctx = new AVFormatContext(null);

		ret = avformat_open_input(fmt_ctx, vf_path, null, null);
		if (ret < 0) {
			System.out.printf("Open video file %s failed \n", vf_path);
			throw new IllegalStateException();
		}

		// i dont know but without this function, sws_getContext does not work
		if (avformat_find_stream_info(fmt_ctx, (PointerPointer) null) < 0) {
			System.exit(-1);
		}

		av_dump_format(fmt_ctx, 0, vf_path, 0);

		for (i = 0; i < fmt_ctx.nb_streams(); i++) {
			if (fmt_ctx.streams(i).codecpar().codec_type() == AVMEDIA_TYPE_VIDEO) {
				v_stream_idx = i;
				break;
			}
		}
		if (v_stream_idx == -1) {
			System.out.println("Cannot find video stream");
			throw new IllegalStateException();
		} else {
			System.out.printf("Video stream %d with resolution %dx%d\n", v_stream_idx,
					fmt_ctx.streams(i).codecpar().width(), fmt_ctx.streams(i).codecpar().height());
		}
//		av_seek_frame(fmt_ctx, v_stream_idx, 10L, AVSEEK_FLAG_FRAME);

		AVCodecContext codec_ctx = avcodec_alloc_context3(null);
		avcodec_parameters_to_context(codec_ctx, fmt_ctx.streams(v_stream_idx).codecpar());

		AVCodec codec = avcodec_find_decoder(codec_ctx.codec_id());
		if (codec == null) {
			System.out.println("Unsupported codec for video file");
			throw new IllegalStateException();
		}
		ret = avcodec_open2(codec_ctx, codec, (PointerPointer) null);
		if (ret < 0) {
			System.out.println("Can not open codec");
			throw new IllegalStateException();
		}

		AVFrame frm = av_frame_alloc();

		// Allocate an AVFrame structure
		AVFrame pFrameRGB = av_frame_alloc();
		if (pFrameRGB == null) {
			System.exit(-1);
		}
		AVStream videoStream = fmt_ctx.streams(v_stream_idx);
		int t = 183;
		long k = (long) (t / av_q2d(videoStream.time_base()));
		System.out.println("k:" + k);
		System.out.println("startTime:" + videoStream.start_time());

		int retSeek = av_seek_frame(fmt_ctx, v_stream_idx, k, AVSEEK_FLAG_ANY);
		avcodec_flush_buffers(codec_ctx);
		System.out.println("retSeek:" + retSeek);
		System.out.println("time_base:" + videoStream.time_base().num() + "/" + videoStream.time_base().den());
		// Determine required buffer size and allocate buffer
		int numBytes = av_image_get_buffer_size(AV_PIX_FMT_BGR24, codec_ctx.width(), codec_ctx.height(), 1);
		BytePointer buffer = new BytePointer(av_malloc(numBytes));

		SwsContext sws_ctx = sws_getContext(codec_ctx.width(), codec_ctx.height(), codec_ctx.pix_fmt(),
				codec_ctx.width(), codec_ctx.height(), AV_PIX_FMT_BGR24, SWS_FAST_BILINEAR, null, null,
				(DoublePointer) null);

		if (sws_ctx == null) {
			System.out.println("Can not use sws");
			throw new IllegalStateException();
		}

		av_image_fill_arrays(pFrameRGB.data(), pFrameRGB.linesize(), buffer, AV_PIX_FMT_BGR24, codec_ctx.width(),
				codec_ctx.height(), 1);

		int ret1 = -1, ret2 = -1;
		i = 0;
		AVPacket pkt = new AVPacket();
		while (av_read_frame(fmt_ctx, pkt) >= 0) {
			if (pkt.stream_index() == v_stream_idx) {
				ret1 = avcodec_send_packet(codec_ctx, pkt);
				ret2 = avcodec_receive_frame(codec_ctx, frm);
				System.out.printf("ret1 %d ret2 %d\n", ret1, ret2);
				// if not check ret2, error occur [swscaler @ 0x1cb3c40] bad src image pointers
				// ret2 same as fi
				if (ret2 >= 0) {
					System.out.println("qts:" + frm.pts());
					sws_scale(sws_ctx, frm.data(), frm.linesize(), 0, codec_ctx.height(), pFrameRGB.data(),
							pFrameRGB.linesize());

					save_frame(pFrameRGB, codec_ctx.width(), codec_ctx.height(), encoderGif);
					// save_frame(frm, codec_ctx.width(), codec_ctx.height(), i);
					if (i++ > 40) {
						break;
					}
				}
			}

			av_packet_unref(pkt);

		}

		av_frame_free(frm);

		avcodec_close(codec_ctx);
		avcodec_free_context(codec_ctx);

		avformat_close_input(fmt_ctx);
		encoderGif.finish();
		System.out.println("Shutdown");
		System.exit(0);
	}

	public static ByteBuffer convertAvFrame2ByteBuffer(AVFrame pFrame, int width, int height) {
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
	public static BufferedImage BGR2BufferedImage(ByteBuffer src, int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		Raster ra = image.getRaster();
		DataBuffer out = ra.getDataBuffer();
		DataBufferByte db = (DataBufferByte) out;
		ByteBuffer.wrap(db.getData()).put(src);
		return image;
	}

}
