package com.clzdl.crm.springboot.service.biz.common.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.clzdl.crm.springboot.service.biz.common.IFileService;
import com.clzdl.crm.springboot.vo.UploadResultVO;
import com.framework.common.util.cipher.MD5Util;
import com.framework.common.util.file.FileUtil;
import com.framework.common.util.properties.PropUtil;
import com.framework.common.util.string.StringUtil;

/**
 * @classname: FileServiceImpl
 * @description: 文件服务
 * @author java
 *
 */
@Service
public class FileServiceImpl implements IFileService {

	@Override
	public UploadResultVO insertImgFile(MultipartFile file) throws Exception {
		UploadResultVO result = null;
		if (file != null && StringUtil.isNotBlank(file.getOriginalFilename())) {
			String fileName = insertSourceFile(file.getOriginalFilename(), file.getSize(), file.getContentType(),
					file.getBytes(), PropUtil.getInstance().get("img.path"));
			result = new UploadResultVO(fileName, file.getContentType(), fileName);
		}
		return result;
	}

	private String insertSourceFile(String file_name, long file_size, String context_type, byte[] bs,
			String resource_folder) throws Exception {
		String uuid = MD5Util.MD5EncodeWithUtf8(new String(bs));
		String suffix = file_name.substring(file_name.lastIndexOf("."), file_name.length());
		String targetPath = resource_folder + uuid + suffix;
		FileUtil.writeFile(targetPath, bs);
		return uuid + suffix;
	}

}
