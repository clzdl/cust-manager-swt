package com.clzdl.crm.springboot.controller.sys;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clzdl.crm.springboot.persistence.entity.SysRole;
import com.clzdl.crm.springboot.service.sys.ISysRoleService;
import com.clzdl.crm.view.common.tree.TreeNodeData;
import com.framework.mybatis.page.PageModel;
import com.framework.shrio.controller.AbstractShiroController;

@Controller
@RequestMapping("/panel/profile/sysrole")
public class SysRoleController extends AbstractShiroController {

	@Resource
	private ISysRoleService sysRoleService;

	@RequestMapping("/list.json")
	public void list(SysRole entity, Integer pageNo, Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PageModel<SysRole> pm = sysRoleService.listPageModel(entity, pageNo, pageSize);
		ajaxSuccess(response, pm);
	}

	@RequestMapping("/save.json")
	public void save(SysRole entity, HttpServletRequest request, HttpServletResponse response) throws Exception {
		sysRoleService.save(entity);
		ajaxSuccess(response);
	}

	@RequestMapping("/getbyid.json")
	public void getById(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysRole sysRole = sysRoleService.getById(id);
		ajaxSuccess(response, sysRole);
	}

	@RequestMapping("/deletebyid.json")
	public void deleteById(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		sysRoleService.deleteById(id);
		ajaxSuccess(response);
	}

	@RequestMapping("/listroleauth.json")
	public void listRoleAuth(Long roleId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<TreeNodeData> data = sysRoleService.listRoleAuth(roleId);
		ajaxSuccess(response, data);
	}

	@RequestMapping("/listuserrole.json")
	public void listUserRole(Long userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<SysRole> data = sysRoleService.listUserRole(userId);
		ajaxSuccess(response, data);
	}
}
