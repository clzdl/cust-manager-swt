package com.clzdl.crm.controller.sys;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.base.mvc.page.PageModel;
import com.clzdl.crm.common.persistence.entity.SysRole;
import com.clzdl.crm.controller.BaseController;
import com.clzdl.crm.dto.ResultDTO;
import com.clzdl.crm.service.sys.ISysRoleService;

@Service
public class SysRoleController extends BaseController {
	private final static String _PERMISSION_DELETE_BY_ID = "/sysrole/deletebyid";

	@Resource
	private ISysRoleService sysRoleService;

	public SysRoleController() {
	}

	public ResultDTO<PageModel<SysRole>> list(SysRole entity, Integer pageNo, Integer pageSize) {
		ResultDTO<PageModel<SysRole>> result = new ResultDTO<PageModel<SysRole>>();
		try {
			PageModel<SysRole> pm = sysRoleService.listPageModel(entity, pageNo, pageSize);
			result.setData(pm);
		} catch (Exception e) {
			decorateResult4Exception(result, e);
		}

		return result;
	}

	public ResultDTO<?> save(SysRole entity) {
		ResultDTO<?> result = new ResultDTO<Object>();
		try {
			sysRoleService.save(entity);
		} catch (Exception e) {
			decorateResult4Exception(result, e);
		}

		return result;
	}

	public ResultDTO<SysRole> getById(Long id) {
		ResultDTO<SysRole> result = new ResultDTO<SysRole>();
		try {
			SysRole sysRole = sysRoleService.getById(id);
			result.setData(sysRole);
		} catch (Exception e) {
			decorateResult4Exception(result, e);
		}

		return result;
	}

	public ResultDTO<?> deleteById(Long id) {

		ResultDTO<?> result = new ResultDTO<Object>();
		try {
//			validatePermissions(_PERMISSION_DELETE_BY_ID);
			sysRoleService.deleteById(id);

		} catch (Exception e) {
			decorateResult4Exception(result, e);
		}

		return result;
	}

	public static SysRoleController getBean() {
		return (SysRoleController) getBeanFromContainer(SysRoleController.class);
	}
}
