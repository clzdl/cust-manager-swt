package com.clzdl.crm.srv.service.biz.user.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clzdl.crm.srv.persistence.entity.CmUserInfo;
import com.clzdl.crm.srv.service.biz.user.IUserInfoService;
import com.clzdl.crm.srv.vo.UserVO;
import com.framework.mybatis.page.AjaxData;
import com.framework.mybatis.page.PageModel;
import com.framework.mybatis.service.AbastractBizService;

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
public class UserInfoServiceImpl extends AbastractBizService<CmUserInfo> implements IUserInfoService {
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
	public void add(String userName, String userPhone) throws Exception {
		CmUserInfo entity = new CmUserInfo();
		entity.setName(userName);
		entity.setPhone(userPhone);
		super.insert(entity);
	}

	@Override
	public AjaxData<UserVO> listVo(Integer pageIndex, Integer pageSize) throws Exception {

		PageModel<CmUserInfo> pm = super.listPageModelByExample(
				Example.builder(CmUserInfo.class).orderByDesc("id").build(), pageIndex, pageSize);
		return new AjaxData<UserVO>(pm.getTotalRecords(), UserVO.buildDataList(pm.getList()));
	}

	@Override
	protected Map<Long, CmUserInfo> list2Map(List<CmUserInfo> list) {
		// TODO Auto-generated method stub
		return null;
	}

}
