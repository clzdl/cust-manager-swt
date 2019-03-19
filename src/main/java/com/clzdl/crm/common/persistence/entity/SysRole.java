package com.clzdl.crm.common.persistence.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "sys_role")
public class SysRole {
	/**
	 * 系统角色表
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 名称
	 */
	@Column(name = "role_name")
	private String roleName;

	/**
	 * 说明
	 */
	private String description;

	/**
	 * 创建日期
	 */
	@Column(name = "create_time", updatable = false)
	private Date createTime;

	@Transient
	private String createTimeOutput;

	@Column(name = "modify_time")
	private Date modifyTime;

	/**
	 * 获取系统角色表
	 *
	 * @return id - 系统角色表
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 设置系统角色表
	 *
	 * @param id 系统角色表
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获取名称
	 *
	 * @return role_name - 名称
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * 设置名称
	 *
	 * @param roleName 名称
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * 获取说明
	 *
	 * @return description - 说明
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置说明
	 *
	 * @param description 说明
	 */
	public void setDescription(String description) {
		this.description = description;
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
		return createTimeOutput;
	}

	public void setCreateTimeOutput(String createTimeOutput) {
		this.createTimeOutput = createTimeOutput;
	}

}