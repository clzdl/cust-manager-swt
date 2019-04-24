package com.clzdl.crm.srv.persistence.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.clzdl.crm.common.enums.EnumSysMenuType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.framework.common.util.date.DateUtil;

@JsonInclude(Include.NON_NULL)
@Table(name = "sys_menu")
public class SysMenu {
	/**
	 * 系统菜单表
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 菜单链接地址
	 */
	private String href;

	/**
	 * 父节点id
	 */
	@Column(name = "parent_id")
	private Long parentId;

	/**
	 * 菜单icon
	 */
	private String icon;

	/**
	 * 节点路径
	 */
	private String path;

	/**
	 * 排序号
	 */
	@Column(name = "sort_no")
	private Integer sortNo;

	/**
	 * 类型；0-菜单权限;1-功能权限
	 */
	@Column(name = "menu_type")
	private Byte menuType;

	@Transient
	private String menuTypeOutput;

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
	 * 获取系统菜单表
	 *
	 * @return id - 系统菜单表
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 设置系统菜单表
	 *
	 * @param id 系统菜单表
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获取菜单名称
	 *
	 * @return name - 菜单名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置菜单名称
	 *
	 * @param name 菜单名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取菜单链接地址
	 *
	 * @return href - 菜单链接地址
	 */
	public String getHref() {
		return href;
	}

	/**
	 * 设置菜单链接地址
	 *
	 * @param href 菜单链接地址
	 */
	public void setHref(String href) {
		this.href = href;
	}

	/**
	 * 获取父节点id
	 *
	 * @return parent_id - 父节点id
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * 设置父节点id
	 *
	 * @param parentId 父节点id
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * 获取菜单icon
	 *
	 * @return icon - 菜单icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * 设置菜单icon
	 *
	 * @param icon 菜单icon
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * 获取节点路径
	 *
	 * @return path - 节点路径
	 */
	public String getPath() {
		return path;
	}

	/**
	 * 设置节点路径
	 *
	 * @param path 节点路径
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 获取排序号
	 *
	 * @return sort_no - 排序号
	 */
	public Integer getSortNo() {
		return sortNo;
	}

	/**
	 * 设置排序号
	 *
	 * @param sortNo 排序号
	 */
	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	/**
	 * 获取类型；0-菜单权限;1-功能权限
	 *
	 * @return menu_type - 类型；0-菜单权限;1-功能权限
	 */
	public Byte getMenuType() {
		return menuType;
	}

	/**
	 * 设置类型；0-菜单权限;1-功能权限
	 *
	 * @param menuType 类型；0-菜单权限;1-功能权限
	 */
	public void setMenuType(Byte menuType) {
		this.menuType = menuType;
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

	public String getMenuTypeOutput() {
		if (null == menuType) {
			return "";
		}
		EnumSysMenuType enumType = EnumSysMenuType.getEnum(menuType.intValue());
		if (enumType != null) {
			return enumType.getName();
		}
		return "";
	}

	public void setMenuTypeOutput(String menuTypeOutput) {
		this.menuTypeOutput = menuTypeOutput;
	}

	public String getCreateTimeOutput() {
		if (null == createTime) {
			return "";
		}
		return DateUtil.formatDate(DateUtil._DATE_TIME_FORMAT1, createTime);
	}

	public void setCreateTimeOutput(String createTimeOutput) {
		this.createTimeOutput = createTimeOutput;
	}

}