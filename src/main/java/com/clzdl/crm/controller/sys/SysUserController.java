package com.clzdl.crm.controller.sys;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clzdl.crm.controller.BaseController;
import com.clzdl.crm.dto.ResultDTO;
import com.clzdl.crm.service.sys.ISysUserService;

@Service
public class SysUserController extends BaseController {
	@Resource
	private ISysUserService sysUserService;

	public SysUserController() {
	}

	public ResultDTO login(String userName, String userPwd) {
		ResultDTO result = new ResultDTO();
		try {
			sysUserService.login(userName, userPwd);

		} catch (Exception e) {
			decorateResult4Exception(result, e);
		}

		return result;
	}

	public static SysUserController getBean() {
		return (SysUserController) getBeanFromContainer(SysUserController.class);
	}
}
