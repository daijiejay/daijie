package org.daijie.api.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "user_feedback")
public class UserFeedback {
    @Id
    private Integer id;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 反馈内容
     */
    private String content;

    /**
     * 联系手机号
     */
    private String mobile;

    /**
     * 反馈状态
     */
    private String status;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 回访记录
     */
    private String revisit;

    /**
     * 解决方案
     */
    private String programme;

    /**
     * 标签集
     */
    private String labels;

    /**
     * 是否禁用
     */
    private Boolean enabled;

    private Integer version;

    /**
     * 反馈图片
     */
    private String pic;

    /**
     * 问题反馈来源设备
     */
    @Column(name = "device_type")
    private String deviceType;

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

    /**
     * 获取反馈内容
     *
     * @return content - 反馈内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置反馈内容
     *
     * @param content 反馈内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * 获取联系手机号
     *
     * @return mobile - 联系手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置联系手机号
     *
     * @param mobile 联系手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * 获取反馈状态
     *
     * @return status - 反馈状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置反馈状态
     *
     * @param status 反馈状态
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
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
     * 获取回访记录
     *
     * @return revisit - 回访记录
     */
    public String getRevisit() {
        return revisit;
    }

    /**
     * 设置回访记录
     *
     * @param revisit 回访记录
     */
    public void setRevisit(String revisit) {
        this.revisit = revisit == null ? null : revisit.trim();
    }

    /**
     * 获取解决方案
     *
     * @return programme - 解决方案
     */
    public String getProgramme() {
        return programme;
    }

    /**
     * 设置解决方案
     *
     * @param programme 解决方案
     */
    public void setProgramme(String programme) {
        this.programme = programme == null ? null : programme.trim();
    }

    /**
     * 获取标签集
     *
     * @return labels - 标签集
     */
    public String getLabels() {
        return labels;
    }

    /**
     * 设置标签集
     *
     * @param labels 标签集
     */
    public void setLabels(String labels) {
        this.labels = labels == null ? null : labels.trim();
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
     * 获取反馈图片
     *
     * @return pic - 反馈图片
     */
    public String getPic() {
        return pic;
    }

    /**
     * 设置反馈图片
     *
     * @param pic 反馈图片
     */
    public void setPic(String pic) {
        this.pic = pic == null ? null : pic.trim();
    }

    /**
     * 获取问题反馈来源设备
     *
     * @return device_type - 问题反馈来源设备
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * 设置问题反馈来源设备
     *
     * @param deviceType 问题反馈来源设备
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType == null ? null : deviceType.trim();
    }
}