package com.clzdl.crm.service.biz.user.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.mvc.page.PageModel;
import com.base.mvc.service.mybatis.AbastractEntityService;
import com.clzdl.crm.common.persistence.entity.CmUserInfo;
import com.clzdl.crm.service.biz.user.IUserInfoService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Builder;
import tk.mybatis.mapper.util.Sqls;

/**
 * @classname: UserInfoServiceImpl
 * @description: 用户
 * @author java
 *
 */
@Service
public class UserInfoServiceImpl extends AbastractEntityService<CmUserInfo> implements IUserInfoService {
	private final static Logger _logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

	protected UserInfoServiceImpl() {
		super(CmUserInfo.class);
	}

	@Override
	@Transactional(readOnly = true)
	public PageModel<CmUserInfo> listPageModel(CmUserInfo entity, Integer pageNo, Integer pageSize) throws Exception {
		Builder builder = Example.builder(CmUserInfo.class);
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
	public boolean fakeDeleteEntity(CmUserInfo entity) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Map<Long, CmUserInfo> list2Map(List<CmUserInfo> list) {
		// TODO Auto-generated method stub
		return null;
	}

}
