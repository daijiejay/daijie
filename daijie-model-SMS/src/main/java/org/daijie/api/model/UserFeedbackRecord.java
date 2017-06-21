package org.daijie.api.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "user_feedback_record")
public class UserFeedbackRecord {
    @Id
    private Integer id;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 操作
     */
    private String operation;

    /**
     * 意见反馈ID
     */
    @Column(name = "feedback_id")
    private String feedbackId;

    /**
     * 是否禁用
     */
    private Boolean enabled;

    private Integer version;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取用户ID
     *
     * @return user_id - 用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取操作
     *
     * @return operation - 操作
     */
    public String getOperation() {
        return operation;
    }

    /**
     * 设置操作
     *
     * @param operation 操作
     */
    public void setOperation(String operation) {
        this.operation = operation == null ? null : operation.trim();
    }

    /**
     * 获取意见反馈ID
     *
     * @return feedback_id - 意见反馈ID
     */
    public String getFeedbackId() {
        return feedbackId;
    }

    /**
     * 设置意见反馈ID
     *
     * @param feedbackId 意见反馈ID
     */
    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId == null ? null : feedbackId.trim();
    }

    /**
     * 获取是否禁用
     *
     * @return enabled - 是否禁用
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * 设置是否禁用
     *
     * @param enabled 是否禁用
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}