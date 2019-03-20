package com.clzdl.crm.service.sys.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.mvc.page.PageModel;
import com.base.mvc.service.mybatis.AbastractEntityService;
import com.clzdl.crm.common.persistence.entity.SysMenu;
import com.clzdl.crm.common.persistence.entity.SysRole;
import com.clzdl.crm.common.persistence.entity.SysRoleUser;
import com.clzdl.crm.service.sys.ISysMenuService;
import com.clzdl.crm.service.sys.ISysRoleMenuService;
import com.clzdl.crm.service.sys.ISysRoleService;
import com.clzdl.crm.service.sys.ISysUserRoleService;
import com.clzdl.crm.view.common.tree.TreeNodeData;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Builder;
import tk.mybatis.mapper.util.Sqls;

/**
 * @classname: SysRoleServiceImpl
 * @description: 系统角色
 * @author java
 *
 */
@Service
public class SysRoleServiceImpl extends AbastractEntityService<SysRole> implements ISysRoleService {
	private final static Logger _logger = LoggerFactory.getLogger(SysRoleServiceImpl.class);

	@Resource
	private ISysUserRoleService sysUserRoleService;
	@Resource
	private ISysMenuService sysMenuService;
	@Resource
	private ISysRoleMenuService sysRoleMenuService;

	protected SysRoleServiceImpl() {
		super(SysRole.class);
	}

	@Override
	@Transactional(readOnly = true)
	public PageModel<SysRole> listPageModel(SysRole entity, Integer pageNo, Integer pageSize) throws Exception {
		Builder builder = Example.builder(SysRole.class);
		if (null != entity.getId()) {
			builder.where(Sqls.custom().andEqualTo("id", entity.getId()));
		}

		builder.orderByDesc("id");
		return super.listPageModelByExample(builder.build(), pageNo, pageSize);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TreeNodeData> listRoleAuth(Long roleId) throws Exception {
		List<TreeNodeData> result = new ArrayList<TreeNodeData>();
		List<SysMenu> menuList = sysMenuService.listAll();
		if (roleId != null) {
			Set<Long> setRoleMenuIds = sysRoleMenuService.setByRoleId(roleId);

			if (CollectionUtils.isNotEmpty(menuList)) {
				for (SysMenu menu : menuList) {
					result.add(new TreeNodeData(menu.getId(), menu.getParentId(), menu.getName(),
							setRoleMenuIds.contains(menu.getId())));
				}
			}
		}
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<SysRole> listUserRole(Long userId) throws Exception {
		List<SysRole> roleList = super.listAll();
		if (userId != null) {
			Set<Long> setMyRuleId = listMyRoleId(userId);
			for (SysRole sysRole : roleList) {
				sysRole.setSelected(setMyRuleId.contains(sysRole.getId()));
			}
		}
		return roleList;
	}

	@Override
	public void save(SysRole entity) throws Exception {
		if (entity.getId() != null) {
			sysRoleMenuService.deleteByRoleId(entity.getId());
			super.update(entity);
		} else {
			super.insert(entity);
		}
		if (CollectionUtils.isNotEmpty(entity.getMenuIds())) {
			for (Long menuId : entity.getMenuIds()) {
				sysRoleMenuService.insert(entity.getId(), menuId);
			}
		}
	}

	@Override
	public boolean fakeDeleteById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fakeDeleteEntity(SysRole entity) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Map<Long, SysRole> list2Map(List<SysRole> list) {
		// TODO Auto-generated method stub
		return null;
	}

	private Set<Long> listMyRoleId(Long userId) throws Exception {
		Set<Long> result = new HashSet<Long>();
		List<SysRoleUser> myList = sysUserRoleService.listByUserId(userId);

		if (CollectionUtils.isNotEmpty(myList)) {
			for (SysRoleUser userRole : myList) {
				result.add(userRole.getSysRoleId());
			}
		}
		return result;
	}
}
