package com.clzdl.crm.springboot.service.sys.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clzdl.crm.ExceptionMessage;
import com.clzdl.crm.springboot.persistence.entity.SysMenu;
import com.clzdl.crm.springboot.persistence.entity.SysUser;
import com.clzdl.crm.springboot.service.sys.ISysMenuService;
import com.framework.common.exception.BizException;
import com.framework.mybatis.page.PageModel;
import com.framework.mybatis.service.AbastractBizService;

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
public class SysMenuServiceImpl extends AbastractBizService<SysMenu> implements ISysMenuService {
	private final static Logger _logger = LoggerFactory.getLogger(SysMenuServiceImpl.class);

	protected SysMenuServiceImpl() {
		super(SysMenu.class);
	}

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
	protected Map<Long, SysMenu> list2Map(List<SysMenu> list) {
		// TODO Auto-generated method stub
		return null;
	}

}
