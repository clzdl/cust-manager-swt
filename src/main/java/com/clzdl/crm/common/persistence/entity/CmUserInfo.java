package com.clzdl.crm.common.persistence.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.base.mvc.enums.EnumUserSex;
import com.base.util.date.DateUtil;

@Table(name = "CM_USER_INFO")
public class CmUserInfo {
	/**
	 * 用户信息表
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 用户名
	 */
	@Column(name = "NAME")
	private String name;

	/**
	 * 电话
	 */
	@Column(name = "PHONE")
	private String phone;

	/**
	 * 性别
	 */
	@Column(name = "SEX")
	private Byte sex;

	@Transient
	private String sexOutput;

	/**
	 * 邮箱
	 */
	@Column(name = "EMAIL")
	private String email;

	/**
	 * 备注
	 */
	@Column(name = "REMARK")
	private String remark;

	/**
	 * 创建日期
	 */
	@Column(name = "CREATE_TIME")
	private Date createTime;

	@Transient
	private String createTimeOutput;

	@Column(name = "MODIFY_TIME")
	private Date modifyTime;

	/**
	 * 获取用户信息表
	 *
	 * @return ID - 用户信息表
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 设置用户信息表
	 *
	 * @param id 用户信息表
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获取用户名
	 *
	 * @return NAME - 用户名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置用户名
	 *
	 * @param name 用户名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取电话
	 *
	 * @return PHONE - 电话
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置电话
	 *
	 * @param phone 电话
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取性别
	 *
	 * @return SEX - 性别
	 */
	public Byte getSex() {
		return sex;
	}

	/**
	 * 设置性别
	 *
	 * @param sex 性别
	 */
	public void setSex(Byte sex) {
		this.sex = sex;
	}

	/**
	 * 获取邮箱
	 *
	 * @return EMAIL - 邮箱
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 设置邮箱
	 *
	 * @param email 邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 获取备注
	 *
	 * @return REMARK - 备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置备注
	 *
	 * @param remark 备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取创建日期
	 *
	 * @return CREATE_TIME - 创建日期
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置创建日期
	 *
	 * @param createTime 创建日期
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return MODIFY_TIME
	 */
	public Date getModifyTime() {
		return modifyTime;
	}

	/**
	 * @param modifyTime
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getSexOutput() {
		EnumUserSex enumSex = EnumUserSex.getEnum(sex.intValue());
		if (null != enumSex) {
			return enumSex.getName();
		}
		return "";
	}

	public void setSexOutput(String sexOutput) {
		this.sexOutput = sexOutput;
	}

	public String getCreateTimeOutput() {
		return DateUtil.formatDate(DateUtil._DATE_TIME_FORMAT1, createTime);
	}

	public void setCreateTimeOutput(String createTimeOutput) {
		this.createTimeOutput = createTimeOutput;
	}

}