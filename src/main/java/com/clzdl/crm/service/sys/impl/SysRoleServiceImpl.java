package com.clzdl.crm.service.sys.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.mvc.page.PageModel;
import com.base.mvc.service.mybatis.AbastractEntityService;
import com.clzdl.crm.common.persistence.entity.SysRole;
import com.clzdl.crm.common.persistence.entity.SysUser;
import com.clzdl.crm.service.sys.ISysRoleService;

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

	protected SysRoleServiceImpl() {
		super(SysRole.class);
	}

	@Override
	@Transactional(readOnly = true)
	public PageModel<SysRole> listPageModel(SysRole entity, Integer pageNo, Integer pageSize) throws Exception {
		Builder builder = Example.builder(SysUser.class);
		if (null != entity.getId()) {
			builder.where(Sqls.custom().andEqualTo("id", entity.getId()));
		}

		builder.orderByDesc("id");
		return super.listPageModelByExample(builder.build(), pageNo, pageSize);
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

}
