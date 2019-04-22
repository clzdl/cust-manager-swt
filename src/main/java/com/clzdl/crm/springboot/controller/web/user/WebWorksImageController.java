package com.clzdl.crm.springboot.controller.web.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clzdl.crm.springboot.persistence.entity.CmWorksImages;
import com.clzdl.crm.springboot.service.biz.user.IWorksImageService;
import com.clzdl.crm.springboot.vo.WorkImgVO;
import com.framework.mybatis.page.AjaxData;
import com.framework.shrio.controller.AbstractShiroController;

@Controller
@RequestMapping("/web/worksimg")
public class WebWorksImageController extends AbstractShiroController {
	@Resource
	private IWorksImageService worksImageService;

	@RequestMapping("/add.json")
	public void handleWebAdd(String userName, String imgUrl, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		worksImageService.add(userName, imgUrl);
		ajaxSuccess(response);
	}

	@RequestMapping("/list.json")
	public void handleList(CmWorksImages entity, Integer pageIndex, Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		AjaxData<WorkImgVO> data = worksImageService.listVo(entity, pageIndex, pageSize);
		ajaxSuccess(response, data);
	}

	@RequestMapping("/info.json")
	public void handleInfo(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		WorkImgVO data = worksImageService.getVoById(id);
		ajaxSuccess(response, data);
	}

}
