package com.clzdl.crm.springboot.persistence.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.clzdl.crm.enums.EnumUserSex;
import com.framework.common.util.date.DateUtil;

@Table(name = "sys_user")
public class SysUser {
	/**
	 * 系统用户表
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 手机
	 */
	private String phone;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 性别，1男 0女
	 */
	private String sex;

	@Transient
	private String sexOutput;

	/**
	 * 登陆名
	 */
	@Column(name = "login_name")
	private String loginName;

	/**
	 * 密码
	 */
	@Column(name = "login_pwd")
	private String loginPwd;

	/**
	 * 用户类型;0-系统，1-商户
	 */
	@Column(name = "user_type")
	private Byte userType;

	/**
	 * 创建日期
	 */
	@Column(name = "create_time", updatable = false)
	private Date createTime;

	@Transient
	private String createTimeOutput;

	@Column(name = "modify_time")
	private Date modifyTime;

	@Transient
	private Set<Long> userRoleIds;

	/**
	 * 获取系统用户表
	 *
	 * @return id - 系统用户表
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 设置系统用户表
	 *
	 * @param id 系统用户表
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获取姓名
	 *
	 * @return name - 姓名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置姓名
	 *
	 * @param name 姓名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取手机
	 *
	 * @return phone - 手机
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置手机
	 *
	 * @param phone 手机
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取邮箱
	 *
	 * @return email - 邮箱
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
	 * 获取性别，1男 0女
	 *
	 * @return sex - 性别，1男 0女
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * 设置性别，1男 0女
	 *
	 * @param sex 性别，1男 0女
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * 获取登陆名
	 *
	 * @return login_name - 登陆名
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * 设置登陆名
	 *
	 * @param loginName 登陆名
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * 获取密码
	 *
	 * @return login_pwd - 密码
	 */
	public String getLoginPwd() {
		return loginPwd;
	}

	/**
	 * 设置密码
	 *
	 * @param loginPwd 密码
	 */
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	/**
	 * 获取用户类型;0-系统，1-商户
	 *
	 * @return user_type - 用户类型;0-系统，1-商户
	 */
	public Byte getUserType() {
		return userType;
	}

	/**
	 * 设置用户类型;0-系统，1-商户
	 *
	 * @param userType 用户类型;0-系统，1-商户
	 */
	public void setUserType(Byte userType) {
		this.userType = userType;
	}

	/**
	 * 获取创建日期
	 *
	 * @return create_time - 创建日期
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
	 * @return modify_time
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

	public String getCreateTimeOutput() {
		return DateUtil.formatDate(DateUtil._DATE_TIME_FORMAT1, createTime);
	}

	public void setCreateTimeOutput(String createTimeOutput) {
		this.createTimeOutput = createTimeOutput;
	}

	public String getSexOutput() {
		EnumUserSex enumSex = EnumUserSex.getEnum(Integer.valueOf(sex));
		if (null != enumSex) {
			return enumSex.getName();
		}
		return "";
	}

	public void setSexOutput(String sexOutput) {
		this.sexOutput = sexOutput;
	}

	public Set<Long> getUserRoleIds() {
		return userRoleIds;
	}

	public void setUserRoleIds(Set<Long> userRoleIds) {
		this.userRoleIds = userRoleIds;
	}

}