package com.clzdl.crm.service.sys;

import java.util.List;

import com.base.mvc.service.mybatis.IEntityService;
import com.clzdl.crm.common.persistence.entity.SysRole;
import com.clzdl.crm.view.common.tree.TreeNodeData;

public interface ISysRoleService extends IEntityService<SysRole> {

	/**
	 * 获取我的权限
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	List<TreeNodeData> listRoleAuth(Long userId) throws Exception;

	/**
	 * 获取我的角色
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	List<SysRole> listUserRole(Long userId) throws Exception;

}