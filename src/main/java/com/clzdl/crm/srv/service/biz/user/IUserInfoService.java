package com.clzdl.crm.srv.service.biz.user;

import com.clzdl.crm.srv.persistence.entity.CmUserInfo;
import com.clzdl.crm.srv.vo.UserVO;
import com.framework.mybatis.page.AjaxData;
import com.framework.mybatis.service.IBizService;

public interface IUserInfoService extends IBizService<CmUserInfo> {

	/**
	 * 添加
	 * 
	 * @param userName
	 * @param userPhone
	 * @throws Exception
	 */
	void add(String userName, String userPhone) throws Exception;

	/**
	 * 列表数据
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	AjaxData<UserVO> listVo(Integer pageIndex, Integer pageSize) throws Exception;
}