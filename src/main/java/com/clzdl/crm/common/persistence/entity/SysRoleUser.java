package com.clzdl.crm.common.persistence.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_role_user")
public class SysRoleUser {
    /**
     * 系统角色用户关系表
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 角色id
     */
    @Column(name = "sys_role_id")
    private Long sysRoleId;

    /**
     * 用户id
     */
    @Column(name = "sys_user_id")
    private Long sysUserId;

    /**
     * 创建日期
     */
    @Column(name = "create_time")
    private Integer createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 获取系统角色用户关系表
     *
     * @return id - 系统角色用户关系表
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置系统角色用户关系表
     *
     * @param id 系统角色用户关系表
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取角色id
     *
     * @return sys_role_id - 角色id
     */
    public Long getSysRoleId() {
        return sysRoleId;
    }

    /**
     * 设置角色id
     *
     * @param sysRoleId 角色id
     */
    public void setSysRoleId(Long sysRoleId) {
        this.sysRoleId = sysRoleId;
    }

    /**
     * 获取用户id
     *
     * @return sys_user_id - 用户id
     */
    public Long getSysUserId() {
        return sysUserId;
    }

    /**
     * 设置用户id
     *
     * @param sysUserId 用户id
     */
    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    /**
     * 获取创建日期
     *
     * @return create_time - 创建日期
     */
    public Integer getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建日期
     *
     * @param createTime 创建日期
     */
    public void setCreateTime(Integer createTime) {
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
}