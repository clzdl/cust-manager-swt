package com.clzdl.crm.service.sys;

import com.base.mvc.service.mybatis.IEntityService;
import com.clzdl.crm.common.persistence.entity.SysUser;

public interface ISysUserService extends IEntityService<SysUser> {

	/**
	 * 登录
	 * 
	 * @param loginName
	 * @param loginPwd
	 * @throws Exception
	 */
	void login(String loginName, String loginPwd) throws Exception;

}