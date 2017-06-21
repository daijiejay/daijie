package org.daijie.api.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "user_addr_change_log")
public class UserAddrChangeLog {
    /**
     * 用户收货地址变更记录id
     */
    @Id
    private Integer id;

    /**
     * 修改内容
     */
    private String change;

    /**
     * 用户收货地址id
     */
    @Column(name = "user_delivery_addr_id")
    private Integer userDeliveryAddrId;

    /**
     * 操作人员id
     */
    @Column(name = "operator_id")
    private Integer operatorId;

    /**
     * 操作人员名
     */
    private String operator;

    /**
     * 操作时间
     */
    @Column(name = "operator_time")
    private Date operatorTime;

    /**
     * 是否是用户自己操作
     */
    private Boolean self;

    /**
     * 获取用户收货地址变更记录id
     *
     * @return id - 用户收货地址变更记录id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置用户收货地址变更记录id
     *
     * @param id 用户收货地址变更记录id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取修改内容
     *
     * @return change - 修改内容
     */
    public String getChange() {
        return change;
    }

    /**
     * 设置修改内容
     *
     * @param change 修改内容
     */
    public void setChange(String change) {
        this.change = change == null ? null : change.trim();
    }

    /**
     * 获取用户收货地址id
     *
     * @return user_delivery_addr_id - 用户收货地址id
     */
    public Integer getUserDeliveryAddrId() {
        return userDeliveryAddrId;
    }

    /**
     * 设置用户收货地址id
     *
     * @param userDeliveryAddrId 用户收货地址id
     */
    public void setUserDeliveryAddrId(Integer userDeliveryAddrId) {
        this.userDeliveryAddrId = userDeliveryAddrId;
    }

    /**
     * 获取操作人员id
     *
     * @return operator_id - 操作人员id
     */
    public Integer getOperatorId() {
        return operatorId;
    }

    /**
     * 设置操作人员id
     *
     * @param operatorId 操作人员id
     */
    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * 获取操作人员名
     *
     * @return operator - 操作人员名
     */
    public String getOperator() {
        return operator;
    }

    /**
     * 设置操作人员名
     *
     * @param operator 操作人员名
     */
    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    /**
     * 获取操作时间
     *
     * @return operator_time - 操作时间
     */
    public Date getOperatorTime() {
        return operatorTime;
    }

    /**
     * 设置操作时间
     *
     * @param operatorTime 操作时间
     */
    public void setOperatorTime(Date operatorTime) {
        this.operatorTime = operatorTime;
    }

    /**
     * 获取是否是用户自己操作
     *
     * @return self - 是否是用户自己操作
     */
    public Boolean getSelf() {
        return self;
    }

    /**
     * 设置是否是用户自己操作
     *
     * @param self 是否是用户自己操作
     */
    public void setSelf(Boolean self) {
        this.self = self;
    }
}