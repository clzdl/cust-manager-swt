package com.clzdl.crm.srv.service.sys;

import java.util.List;

import com.clzdl.crm.srv.persistence.entity.SysMenu;
import com.clzdl.crm.srv.persistence.entity.SysRole;
import com.clzdl.crm.srv.persistence.entity.SysUser;
import com.framework.mybatis.service.IBizService;

public interface ISysUserService extends IBizService<SysUser> {

	/**
	 * 登录
	 * 
	 * @param loginName
	 * @param loginPwd
	 * @throws Exception
	 */
	void login(String loginName, String loginPwd) throws Exception;

	/**
	 * 根据登录名获取系统用户
	 * 
	 * @param loginName
	 * @return
	 * @throws Exception
	 */
	SysUser findSysUserByLoginName(String loginName) throws Exception;

	/**
	 * 获取用户菜单权限
	 * 
	 * @param sysUserId
	 * @return
	 * @throws Exception
	 */
	List<SysMenu> listSysAllMenuByUserId(Long sysUserId) throws Exception;

	/**
	 * 获取用户角色
	 * 
	 * @param sysUserId
	 * @return
	 * @throws Exception
	 */
	List<SysRole> listSysRoleByUserId(Long sysUserId) throws Exception;
}