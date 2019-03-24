package com.clzdl.crm.springboot.service.sys.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clzdl.crm.springboot.persistence.entity.SysRoleUser;
import com.clzdl.crm.springboot.service.sys.ISysUserRoleService;
import com.framework.mybatis.service.AbastractBizService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

/**
 * @classname: SysUserRoleServiceImpl
 * @description: 系统用户角色关系
 * @author java
 *
 */
@Service
public class SysUserRoleServiceImpl extends AbastractBizService<SysRoleUser> implements ISysUserRoleService {
	private final static Logger _logger = LoggerFactory.getLogger(SysUserRoleServiceImpl.class);

	protected SysUserRoleServiceImpl() {
		super(SysRoleUser.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<SysRoleUser> listByUserId(Long userId) throws Exception {
		return super.listAllByExample(
				Example.builder(SysRoleUser.class).where(Sqls.custom().andEqualTo("sysUserId", userId)).build());
	}

	@Override
	public void deleteByUserId(Long userId) throws Exception {
		super.deleteByExample(
				Example.builder(SysRoleUser.class).where(Sqls.custom().andEqualTo("sysUserId", userId)).build());
	}

	@Override
	public void insert(Long userId, Long roleId) throws Exception {
		SysRoleUser entity = new SysRoleUser();
		entity.setSysUserId(userId);
		entity.setSysRoleId(roleId);
		super.insert(entity);
	}

	@Override
	protected Map<Long, SysRoleUser> list2Map(List<SysRoleUser> list) {
		// TODO Auto-generated method stub
		return null;
	}

}
