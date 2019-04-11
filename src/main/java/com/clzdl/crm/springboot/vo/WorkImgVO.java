package com.clzdl.crm.springboot.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.clzdl.crm.Constants;
import com.clzdl.crm.springboot.persistence.entity.CmWorksImages;

public class WorkImgVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3330679150807643813L;

	private String name;
	private String imgUrl;

	public static List<WorkImgVO> buildDataList(List<CmWorksImages> list) {
		List<WorkImgVO> result = new ArrayList<WorkImgVO>();
		if (CollectionUtils.isEmpty(list)) {
			return result;
		}

		WorkImgVO vo = null;
		for (CmWorksImages img : list) {
			vo = new WorkImgVO();
			vo.setName(img.getName());
			vo.setImgUrl(img.getImgUrl());
			result.add(vo);
		}

		return result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImgUrl() {
		if (StringUtils.isNoneBlank(imgUrl)) {
			return Constants.IMG_SITE + imgUrl;
		}
		return "";
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

}
