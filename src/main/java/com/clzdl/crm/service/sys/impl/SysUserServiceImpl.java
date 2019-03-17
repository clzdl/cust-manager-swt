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
import com.base.util.cipher.MD5Util;
import com.clzdl.crm.ExceptionMessage;
import com.clzdl.crm.common.persistence.entity.SysUser;
import com.clzdl.crm.service.sys.ISysUserService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Builder;
import tk.mybatis.mapper.util.Sqls;

/**
 * @classname: SysUserServiceImpl
 * @description: 系统管理用户
 * @author java
 *
 */
@Service
public class SysUserServiceImpl extends AbastractEntityService<SysUser> implements ISysUserService {
	private final static Logger _logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

	protected SysUserServiceImpl() {
		super(SysUser.class);
	}

	@Override
	@Transactional(readOnly = true)
	public PageModel<SysUser> listPageModel(SysUser entity, Integer pageNo, Integer pageSize) throws Exception {
		Builder builder = Example.builder(SysUser.class);
		if (null != entity.getId()) {
			builder.where(Sqls.custom().andEqualTo("id", entity.getId()));
		}

		builder.orderByDesc("id");
		return super.listPageModelByExample(builder.build(), pageNo, pageSize);
	}

	@Override
	public void login(String loginName, String loginPwd) throws Exception {
		SysUser sysUser = super.getByExample(
				Example.builder(SysUser.class).where(Sqls.custom().andEqualTo("loginName", loginName)).build());
		if (null == sysUser || !sysUser.getLoginPwd().equalsIgnoreCase(MD5Util.MD5Encode(loginPwd))) {
			throw new BizException(ExceptionMessage.USER_LOGIN_ERROR);
		}
		_logger.info("用户:{} 登录成功", loginName);
	}

	@Override
	public boolean fakeDeleteById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fakeDeleteEntity(SysUser entity) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Map<Long, SysUser> list2Map(List<SysUser> list) {
		// TODO Auto-generated method stub
		return null;
	}

}
