package com.clzdl.crm.srv.persistence.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_operation_log")
public class SysOperationLog {
    /**
     * 系统操作日志表
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户id
     */
    @Column(name = "sys_user_id")
    private Long sysUserId;

    /**
     * 日志
     */
    private String contents;

    /**
     * 创建日期
     */
    @Column(name = "create_time")
    private Integer createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 获取系统操作日志表
     *
     * @return id - 系统操作日志表
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置系统操作日志表
     *
     * @param id 系统操作日志表
     */
    public void setId(Long id) {
        this.id = id;
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
     * 获取日志
     *
     * @return contents - 日志
     */
    public String getContents() {
        return contents;
    }

    /**
     * 设置日志
     *
     * @param contents 日志
     */
    public void setContents(String contents) {
        this.contents = contents;
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