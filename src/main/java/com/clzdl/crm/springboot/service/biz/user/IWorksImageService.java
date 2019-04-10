package com.clzdl.crm.springboot.service.biz.user;

import com.clzdl.crm.springboot.persistence.entity.CmWorksImages;
import com.framework.mybatis.service.IBizService;

public interface IWorksImageService extends IBizService<CmWorksImages> {

	/**
	 * 添加
	 * 
	 * @param userName
	 * @param userPhone
	 * @throws Exception
	 */
	void add(String userName, String imgUrl) throws Exception;

}