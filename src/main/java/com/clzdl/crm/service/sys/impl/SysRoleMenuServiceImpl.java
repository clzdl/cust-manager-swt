package com.clzdl.crm.service.sys.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.mvc.service.mybatis.AbastractEntityService;
import com.clzdl.crm.common.persistence.entity.SysRoleMenu;
import com.clzdl.crm.service.sys.ISysRoleMenuService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

/**
 * @classname: SysRoleMenuServiceImpl
 * @description: 系统角色菜单关系
 * @author java
 *
 */
@Service
public class SysRoleMenuServiceImpl extends AbastractEntityService<SysRoleMenu> implements ISysRoleMenuService {
	private final static Logger _logger = LoggerFactory.getLogger(SysRoleMenuServiceImpl.class);

	protected SysRoleMenuServiceImpl() {
		super(SysRoleMenu.class);
	}

	@Override
	@Transactional(readOnly = true)
	public Set<Long> listByRoleIds(Collection<Long> roleIds) throws Exception {
		Set<Long> result = new HashSet<Long>();
		List<SysRoleMenu> list = super.listAllByExample(
				Example.builder(SysRoleMenu.class).where(Sqls.custom().andIn("sysRoleId", roleIds)).build());
		if (CollectionUtils.isNotEmpty(list)) {
			for (SysRoleMenu roleMenu : list) {
				result.add(roleMenu.getSysMenuId());
			}
		}
		return result;
	}

	@Override
	public Set<Long> setByRoleId(Long ruleId) throws Exception {
		Set<Long> result = new HashSet<Long>();
		List<SysRoleMenu> list = super.listAllByExample(
				Example.builder(SysRoleMenu.class).where(Sqls.custom().andEqualTo("sysRoleId", ruleId)).build());
		if (CollectionUtils.isNotEmpty(list)) {
			for (SysRoleMenu roleMenu : list) {
				result.add(roleMenu.getSysMenuId());
			}
		}
		return result;
	}

	@Override
	public void deleteByRoleId(Long ruleId) throws Exception {
		super.deleteByExample(
				Example.builder(SysRoleMenu.class).where(Sqls.custom().andEqualTo("sysRoleId", ruleId)).build());
	}

	@Override
	public void insert(Long roleId, Long menuId) throws Exception {
		SysRoleMenu entity = new SysRoleMenu();
		entity.setSysRoleId(roleId);
		entity.setSysMenuId(menuId);
		super.insert(entity);
	}

	@Override
	public boolean fakeDeleteById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fakeDeleteEntity(SysRoleMenu entity) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Map<Long, SysRoleMenu> list2Map(List<SysRoleMenu> list) {
		return null;
	}

}
