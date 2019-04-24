package com.clzdl.crm.srv.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.clzdl.crm.srv.persistence.entity.CmUserInfo;

public class UserVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3330679150807643813L;

	private String userName;
	private String userPhone;

	public static List<UserVO> buildDataList(List<CmUserInfo> list) {
		List<UserVO> result = new ArrayList<UserVO>();
		if (CollectionUtils.isEmpty(list)) {
			return result;
		}

		UserVO vo = null;
		for (CmUserInfo user : list) {
			vo = new UserVO();
			vo.setUserName(user.getName());
			vo.setUserPhone(user.getPhone());
			result.add(vo);
		}

		return result;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

}
