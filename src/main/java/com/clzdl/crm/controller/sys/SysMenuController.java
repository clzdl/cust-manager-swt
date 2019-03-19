package com.clzdl.crm.controller.sys;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.base.mvc.page.PageModel;
import com.clzdl.crm.common.persistence.entity.SysMenu;
import com.clzdl.crm.controller.BaseController;
import com.clzdl.crm.dto.ResultDTO;
import com.clzdl.crm.service.sys.ISysMenuService;

@Service
public class SysMenuController extends BaseController {
	private final static String _PERMISSION_DELETE_BY_ID = "/sysmenu/deletebyid";

	@Resource
	private ISysMenuService sysMenuService;

	public SysMenuController() {
	}

	public ResultDTO<PageModel<SysMenu>> list(SysMenu entity, Integer pageNo, Integer pageSize) {
		ResultDTO<PageModel<SysMenu>> result = new ResultDTO<PageModel<SysMenu>>();
		try {
			PageModel<SysMenu> pm = sysMenuService.listPageModel(entity, pageNo, pageSize);
			result.setData(pm);
		} catch (Exception e) {
			decorateResult4Exception(result, e);
		}

		return result;
	}

	public ResultDTO<?> save(SysMenu entity) {
		ResultDTO<?> result = new ResultDTO<Object>();
		try {
			sysMenuService.save(entity);
		} catch (Exception e) {
			decorateResult4Exception(result, e);
		}

		return result;
	}

	public ResultDTO<SysMenu> getById(Long id) {
		ResultDTO<SysMenu> result = new ResultDTO<SysMenu>();
		try {
			SysMenu sysMenu = sysMenuService.getById(id);
			result.setData(sysMenu);
		} catch (Exception e) {
			decorateResult4Exception(result, e);
		}

		return result;
	}

	public ResultDTO<?> deleteById(Long id) {

		ResultDTO<?> result = new ResultDTO<Object>();
		try {
//			validatePermissions(_PERMISSION_DELETE_BY_ID);
			sysMenuService.deleteById(id);

		} catch (Exception e) {
			decorateResult4Exception(result, e);
		}

		return result;
	}

	public static SysMenuController getBean() {
		return (SysMenuController) getBeanFromContainer(SysMenuController.class);
	}
}
