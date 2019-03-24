package com.clzdl.crm.springboot.service.sys;

import java.util.Collection;
import java.util.Set;

import com.clzdl.crm.springboot.persistence.entity.SysRoleMenu;
import com.framework.mybatis.service.IBizService;

public interface ISysRoleMenuService extends IBizService<SysRoleMenu> {

	/**
	 * 获取角色菜单
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	Set<Long> listByRoleIds(Collection<Long> roleIds) throws Exception;

	/**
	 * 角色菜单Id
	 * 
	 * @param ruleId
	 * @return
	 * @throws Exception
	 */
	Set<Long> setByRoleId(Long ruleId) throws Exception;

	/**
	 * 删除角色菜单
	 * 
	 * @param ruleId
	 * @throws Exception
	 */
	void deleteByRoleId(Long ruleId) throws Exception;

	/**
	 * 插入
	 * 
	 * @param roleId
	 * @param menuId
	 * @throws Exception
	 */
	void insert(Long roleId, Long menuId) throws Exception;
}