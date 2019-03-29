package com.clzdl.crm.springboot.controller.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clzdl.crm.springboot.service.biz.user.IUserInfoService;
import com.framework.shrio.controller.AbstractShiroController;

@Controller
@RequestMapping("/web/user")
public class WebUserController extends AbstractShiroController {
	@Resource
	private IUserInfoService userInfoService;

	@RequestMapping("/add.json")
	public void handleWebAdd(String userName, String userPhone, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		userInfoService.add(userName, userPhone);
		ajaxSuccess(response);
	}

}
