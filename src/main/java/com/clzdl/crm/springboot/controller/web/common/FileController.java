package com.clzdl.crm.springboot.controller.web.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.clzdl.crm.ExceptionMessage;
import com.clzdl.crm.springboot.service.biz.common.IFileService;
import com.clzdl.crm.springboot.vo.UploadResultVO;
import com.framework.common.exception.BizException;
import com.framework.shrio.controller.AbstractShiroController;

@Controller
@RequestMapping("/web/file")
public class FileController extends AbstractShiroController {
	private static List<String> extList = new ArrayList<String>(); // 定义允许上传的文件扩展名
	static {
		extList.add("gif");
		extList.add("jpg");
		extList.add("jpeg");
		extList.add("png");
		extList.add("bmp");
	}

	@Resource
	private IFileService imgService;

	@RequestMapping("/uploadimg.json")
	public void handleUploadImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
		UploadResultVO data = null;
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iter = multiRequest.getFileNames();
		if (iter.hasNext()) {
			MultipartFile file = multiRequest.getFile(iter.next());
			// 验证附件格式和大小
			checkFile(file);

			data = imgService.insertImgFile(file);
		}
		ajaxSuccess(response, data);
	}

	private void checkFile(MultipartFile file) throws Exception {
		String fileName = file.getOriginalFilename();
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		if (!extList.contains(fileExt)) {
			throw new BizException(ExceptionMessage.FILE_NAME_EXT_ERROR);
		}
	}
}
