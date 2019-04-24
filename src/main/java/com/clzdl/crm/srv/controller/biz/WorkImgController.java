package com.clzdl.crm.srv.controller.biz;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clzdl.crm.srv.persistence.entity.CmWorksImages;
import com.clzdl.crm.srv.service.biz.user.IWorksImageService;
import com.framework.mybatis.page.PageModel;
import com.framework.shrio.controller.AbstractShiroController;

@Controller
@RequestMapping("/panel/biz/workimg")
public class WorkImgController extends AbstractShiroController {
	@Resource
	private IWorksImageService workImageService;

	@RequestMapping("/save.json")
	public void save(@RequestBody CmWorksImages cmUserInfo, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		workImageService.save(cmUserInfo);
		ajaxSuccess(response);
	}

	@RequestMapping("/list.json")
	public void list(@RequestBody CmWorksImages entity, Integer pageNo, Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PageModel<CmWorksImages> pm = workImageService.listPageModel(entity, pageNo, pageSize);
		ajaxSuccess(response, pm);
	}

	@RequestMapping("/getbyid.json")
	public void getById(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CmWorksImages entity = workImageService.getById(id);
		ajaxSuccess(response, entity);
	}

	@RequestMapping("/deletebyid.json")
	public void deleteById(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		workImageService.deleteById(id);
		ajaxSuccess(response);
	}

}
