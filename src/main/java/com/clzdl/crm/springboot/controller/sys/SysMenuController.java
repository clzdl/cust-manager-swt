package com.clzdl.crm.springboot.controller.sys;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clzdl.crm.springboot.persistence.entity.SysMenu;
import com.clzdl.crm.springboot.service.sys.ISysMenuService;
import com.framework.mybatis.page.PageModel;
import com.framework.shrio.controller.AbstractShiroController;

@Controller
@RequestMapping("/panel/profile/sysmenu")
public class SysMenuController extends AbstractShiroController {

	@Resource
	private ISysMenuService sysMenuService;

	@RequestMapping("/list.json")
	public void list(@RequestBody SysMenu entity, Integer pageNo, Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PageModel<SysMenu> pm = sysMenuService.listPageModel(entity, pageNo, pageSize);
		ajaxSuccess(response, pm);
	}

	@RequestMapping("/save.json")
	public void save(@RequestBody SysMenu entity, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		sysMenuService.save(entity);
		ajaxSuccess(response);
	}

	@RequestMapping("/getbyid.json")
	public void getById(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysMenu sysMenu = sysMenuService.getById(id);
		ajaxSuccess(response, sysMenu);
	}

	@RequestMapping("/listall.json")
	public void listAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<SysMenu> list = sysMenuService.listAll();
		ajaxSuccess(response, list);
	}

	@RequestMapping("/deletebyid.json")
	public void deleteById(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		sysMenuService.deleteById(id);
		ajaxSuccess(response);
	}

}
