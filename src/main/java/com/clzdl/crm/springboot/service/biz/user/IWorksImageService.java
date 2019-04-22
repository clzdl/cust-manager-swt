package com.clzdl.crm.springboot.service.biz.user;

import com.clzdl.crm.springboot.persistence.entity.CmWorksImages;
import com.clzdl.crm.springboot.vo.WorkImgVO;
import com.framework.mybatis.page.AjaxData;
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

	/**
	 * 列表
	 * 
	 * @param entity
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	AjaxData<WorkImgVO> listVo(CmWorksImages entity, Integer pageIndex, Integer pageSize) throws Exception;

	/**
	 * 详情
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	WorkImgVO getVoById(Long id) throws Exception;
}