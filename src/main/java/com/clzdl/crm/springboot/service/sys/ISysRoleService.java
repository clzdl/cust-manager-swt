package com.clzdl.crm.springboot.service.sys;

import java.util.List;

import com.clzdl.crm.springboot.persistence.entity.SysRole;
import com.clzdl.crm.view.common.tree.TreeNodeData;
import com.framework.mybatis.service.IBizService;

public interface ISysRoleService extends IBizService<SysRole> {

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