package com.clzdl.crm.springboot.controller.biz;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clzdl.crm.springboot.persistence.entity.CmUserInfo;
import com.clzdl.crm.springboot.service.biz.user.IUserInfoService;
import com.framework.mybatis.page.PageModel;
import com.framework.shrio.controller.AbstractShiroController;

@Controller
@RequestMapping("/panel/biz/customer")
public class UserInfoController extends AbstractShiroController {
	@Resource
	private IUserInfoService userInfoService;

	@RequestMapping("/save.json")
	public void save(@RequestBody CmUserInfo cmUserInfo, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		userInfoService.save(cmUserInfo);
		ajaxSuccess(response);
	}

	@RequestMapping("/list.json")
	public void list(@RequestBody CmUserInfo entity, Integer pageNo, Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PageModel<CmUserInfo> pm = userInfoService.listPageModel(entity, pageNo, pageSize);
		ajaxSuccess(response, pm);
	}

	@RequestMapping("/getbyid.json")
	public void getById(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CmUserInfo entity = userInfoService.getById(id);
		ajaxSuccess(response, entity);
	}

	@RequestMapping("/deletebyid.json")
	public void deleteById(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		userInfoService.deleteById(id);
		ajaxSuccess(response);
	}

}
