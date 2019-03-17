package com.clzdl.crm.controller.biz;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.base.mvc.page.PageModel;
import com.clzdl.crm.common.persistence.entity.CmUserInfo;
import com.clzdl.crm.controller.BaseController;
import com.clzdl.crm.dto.ResultDTO;
import com.clzdl.crm.service.biz.user.IUserInfoService;

@Service
public class UserInfoController extends BaseController {
	@Resource
	private IUserInfoService userInfoService;

	public UserInfoController() {
	}

	public ResultDTO save(CmUserInfo cmUserInfo) {
		ResultDTO result = new ResultDTO();
		try {
			userInfoService.save(cmUserInfo);

		} catch (Exception e) {
			decorateResult4Exception(result, e);
		}

		return result;
	}

	public ResultDTO<PageModel<CmUserInfo>> list(CmUserInfo entity, Integer pageNo, Integer pageSize) {
		ResultDTO<PageModel<CmUserInfo>> result = new ResultDTO<PageModel<CmUserInfo>>();
		try {
			PageModel<CmUserInfo> pm = userInfoService.listPageModel(entity, pageNo, pageSize);
			result.setData(pm);
		} catch (Exception e) {
			decorateResult4Exception(result, e);
		}

		return result;
	}

	public static UserInfoController getBean() {
		return (UserInfoController) getBeanFromContainer(UserInfoController.class);
	}
}
