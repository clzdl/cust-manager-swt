package com.clzdl.crm.springboot.persistence.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.framework.common.util.date.DateUtil;

@Table(name = "CM_WORKS_IMAGES")
public class CmWorksImages {
	/**
	 * 用户作品图片
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 图片名称
	 */
	@Column(name = "NAME")
	private String name;

	/**
	 * 图片地址
	 */
	@Column(name = "IMG_URL")
	private String imgUrl;

	/**
	 * 创建日期
	 */
	@Column(name = "CREATE_TIME", updatable = false)
	private Date createTime;

	@Transient
	private String createTimeOutput;

	@Column(name = "MODIFY_TIME")
	private Date modifyTime;

	/**
	 * 获取用户作品图片
	 *
	 * @return ID - 用户作品图片
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 设置用户作品图片
	 *
	 * @param id 用户作品图片
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获取图片名称
	 *
	 * @return NAME - 图片名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置图片名称
	 *
	 * @param name 图片名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取图片地址
	 *
	 * @return IMG_URL - 图片地址
	 */
	public String getImgUrl() {
		return imgUrl;
	}

	/**
	 * 设置图片地址
	 *
	 * @param imgUrl 图片地址
	 */
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
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