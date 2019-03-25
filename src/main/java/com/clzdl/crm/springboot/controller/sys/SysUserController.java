package com.clzdl.crm.springboot.controller.sys;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clzdl.crm.springboot.persistence.entity.SysUser;
import com.clzdl.crm.springboot.service.sys.ISysUserService;
import com.framework.mybatis.page.PageModel;
import com.framework.shrio.controller.AbstractShiroController;
import com.framework.shrio.util.ShiroKit;

@Controller
@RequestMapping("/panel/profile/sysuser")
public class SysUserController extends AbstractShiroController {
	@Resource
	private ISysUserService sysUserService;

	@RequestMapping("/login.json")
	public void login(String userName, String userPwd, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		sysUserService.login(userName, userPwd);
		ajaxSuccess(response);
	}

	@RequestMapping("/list.json")
	public void list(@RequestBody SysUser entity, Integer pageNo, Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PageModel<SysUser> pm = sysUserService.listPageModel(entity, pageNo, pageSize);
		ajaxSuccess(response, pm);
	}

	@RequestMapping("/save.json")
	public void save(@RequestBody SysUser entity, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		sysUserService.save(entity);
		ajaxSuccess(response);
	}

	@RequestMapping("/getbyid.json")
	public void getById(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUser sysUser = sysUserService.getById(id);
		ajaxSuccess(response, sysUser);
	}

	@RequestMapping("/deletebyid.json")
	public void deleteById(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		sysUserService.deleteById(id);
		ajaxSuccess(response);
	}

	@RequestMapping("/havepermission.json")
	public void havePermission(String permission, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Boolean data = ShiroKit.hasPermission(permission);
		ajaxSuccess(response, data);
	}
}
