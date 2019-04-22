package com.clzdl.crm.springboot.service.biz.user.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clzdl.crm.Constants;
import com.clzdl.crm.springboot.persistence.entity.CmUserInfo;
import com.clzdl.crm.springboot.persistence.entity.CmWorksImages;
import com.clzdl.crm.springboot.service.biz.user.IWorksImageService;
import com.clzdl.crm.springboot.vo.WorkImgVO;
import com.framework.common.util.file.FileUtil;
import com.framework.mybatis.page.AjaxData;
import com.framework.mybatis.page.PageModel;
import com.framework.mybatis.service.AbastractBizService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Builder;
import tk.mybatis.mapper.util.Sqls;

/**
 * @classname: UserInfoServiceImpl
 * @description: 用户
 * @author java
 *
 */
@Service
public class WorksImageServiceImpl extends AbastractBizService<CmWorksImages> implements IWorksImageService {
	private final static Logger _logger = LoggerFactory.getLogger(WorksImageServiceImpl.class);

	protected WorksImageServiceImpl() {
		super(CmWorksImages.class);
	}

	@Override
	@Transactional(readOnly = true)
	public PageModel<CmWorksImages> listPageModel(CmWorksImages entity, Integer pageNo, Integer pageSize)
			throws Exception {
		Builder builder = Example.builder(CmUserInfo.class);
		if (null != entity.getId()) {
			builder.where(Sqls.custom().andEqualTo("id", entity.getId()));
		}
		builder.orderByDesc("id");
		return super.listPageModelByExample(builder.build(), pageNo, pageSize);
	}

	@Override
	public void add(String userName, String imgUrl) throws Exception {
		CmWorksImages entity = new CmWorksImages();
		entity.setName(userName);
		entity.setImgUrl(imgUrl);
		super.insert(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public AjaxData<WorkImgVO> listVo(CmWorksImages entity, Integer pageIndex, Integer pageSize) throws Exception {
		PageModel<CmWorksImages> pm = super.listPageModelByExample(
				Example.builder(CmWorksImages.class).orderByDesc("id").build(), pageIndex, pageSize);
		return new AjaxData<WorkImgVO>(pm.getTotalRecords(), WorkImgVO.buildDataList(pm.getList()));
	}

	@Override
	@Transactional(readOnly = true)
	public WorkImgVO getVoById(Long id) throws Exception {
		CmWorksImages entity = super.getById(id);
		return WorkImgVO.buildDataInfo(entity);
	}

	@Override
	public boolean deleteById(Long id) throws Exception {
		CmWorksImages entity = super.getById(id);
		if (null != entity) {
			String fileAbsPath = System.getProperty("user.dir") + Constants.IMG_SITE + entity.getImgUrl();
			if (FileUtil.fileExist(fileAbsPath)) {
				FileUtil.delFile(fileAbsPath);
			}

		}
		return super.deleteById(id);

	}

	@Override
	protected Map<Long, CmWorksImages> list2Map(List<CmWorksImages> list) {
		// TODO Auto-generated method stub
		return null;
	}

}
