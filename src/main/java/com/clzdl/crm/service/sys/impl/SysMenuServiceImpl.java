package com.clzdl.crm.service.sys.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.exception.BizException;
import com.base.mvc.page.PageModel;
import com.base.mvc.service.mybatis.AbastractEntityService;
import com.clzdl.crm.ExceptionMessage;
import com.clzdl.crm.common.persistence.entity.SysMenu;
import com.clzdl.crm.common.persistence.entity.SysUser;
import com.clzdl.crm.service.sys.ISysMenuService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Builder;
import tk.mybatis.mapper.util.Sqls;

/**
 * @classname: SysMenuServiceImpl
 * @description: 系统菜单
 * @author java
 *
 */
@Service
public class SysMenuServiceImpl extends AbastractEntityService<SysMenu> implements ISysMenuService {
	private final static Logger _logger = LoggerFactory.getLogger(SysMenuServiceImpl.class);

	protected SysMenuServiceImpl() {
		super(SysMenu.class);
	}

	@Override
	@Transactional(readOnly = true)
	public PageModel<SysMenu> listPageModel(SysMenu entity, Integer pageNo, Integer pageSize) throws Exception {
		Builder builder = Example.builder(SysUser.class);
		if (null != entity.getId()) {
			builder.where(Sqls.custom().andEqualTo("id", entity.getId()));
		}

		builder.orderByDesc("id");
		return super.listPageModelByExample(builder.build(), pageNo, pageSize);
	}

	@Override
	public boolean deleteById(Long id) throws Exception {
		Integer cnt = super.countByExample(
				Example.builder(SysMenu.class).where(Sqls.custom().andEqualTo("parentId", id)).build());
		if (cnt > 0) {
			throw new BizException(ExceptionMessage.EXIST_SUBORDINATE_DELETE_WRONG);
		}
		return super.deleteById(id);
	}

	@Override
	public boolean fakeDeleteById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fakeDeleteEntity(SysMenu entity) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Map<Long, SysMenu> list2Map(List<SysMenu> list) {
		// TODO Auto-generated method stub
		return null;
	}

}
