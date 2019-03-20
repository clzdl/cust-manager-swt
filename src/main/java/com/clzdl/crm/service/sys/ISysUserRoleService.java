package com.clzdl.crm.service.sys;

import java.util.List;

import com.base.mvc.service.mybatis.IEntityService;
import com.clzdl.crm.common.persistence.entity.SysRoleUser;

public interface ISysUserRoleService extends IEntityService<SysRoleUser> {

	/**
	 * 获取我的角色
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	List<SysRoleUser> listByUserId(Long userId) throws Exception;

	/**
	 * 删除用户拥有的角色
	 * 
	 * @param userId
	 * @throws Exception
	 */
	void deleteByUserId(Long userId) throws Exception;

	/**
	 * 插入
	 * 
	 * @param userId
	 * @param roleId
	 * @throws Exception
	 */
	void insert(Long userId, Long roleId) throws Exception;

}