package com.clzdl.crm.controller.sys;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.base.mvc.page.PageModel;
import com.clzdl.crm.common.persistence.entity.SysUser;
import com.clzdl.crm.controller.BaseController;
import com.clzdl.crm.dto.ResultDTO;
import com.clzdl.crm.service.sys.ISysUserService;

@Service
public class SysUserController extends BaseController {
	private final static String _PERMISSION_DELETE_BY_ID = "/sysuser/deletebyid";
	@Resource
	private ISysUserService sysUserService;

	public SysUserController() {
	}

	public ResultDTO<?> login(String userName, String userPwd) {
		ResultDTO<?> result = new ResultDTO<Object>();
		try {
			sysUserService.login(userName, userPwd);

		} catch (Exception e) {
			decorateResult4Exception(result, e);
		}

		return result;
	}

	public ResultDTO<PageModel<SysUser>> list(SysUser entity, Integer pageNo, Integer pageSize) {
		ResultDTO<PageModel<SysUser>> result = new ResultDTO<PageModel<SysUser>>();
		try {
			PageModel<SysUser> pm = sysUserService.listPageModel(entity, pageNo, pageSize);
			result.setData(pm);
		} catch (Exception e) {
			decorateResult4Exception(result, e);
		}

		return result;
	}

	public ResultDTO<?> save(SysUser entity) {
		ResultDTO<PageModel<SysUser>> result = new ResultDTO<PageModel<SysUser>>();
		try {
			sysUserService.save(entity);
		} catch (Exception e) {
			decorateResult4Exception(result, e);
		}

		return result;
	}

	public ResultDTO<SysUser> getById(Long id) {
		ResultDTO<SysUser> result = new ResultDTO<SysUser>();
		try {
			SysUser sysUser = sysUserService.getById(id);
			result.setData(sysUser);
		} catch (Exception e) {
			decorateResult4Exception(result, e);
		}

		return result;
	}

	public ResultDTO<?> deleteById(Long id) {

		ResultDTO<?> result = new ResultDTO<Object>();
		try {
			validatePermissions(_PERMISSION_DELETE_BY_ID);
			sysUserService.deleteById(id);

		} catch (Exception e) {
			decorateResult4Exception(result, e);
		}

		return result;
	}

	public static SysUserController getBean() {
		return (SysUserController) getBeanFromContainer(SysUserController.class);
	}
}
