package com.clzdl.crm.srv.service.biz.common;

import org.springframework.web.multipart.MultipartFile;

import com.clzdl.crm.srv.vo.UploadResultVO;

/**
 * 上传文件
 * 
 */
public interface IFileService {

	/**
	 * 上传图片
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	UploadResultVO insertImgFile(MultipartFile file) throws Exception;
}
