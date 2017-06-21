package org.daijie.api.model;

import javax.persistence.*;

@Table(name = "user_verify")
public class UserVerify {
    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 身份证是否验证
     */
    @Column(name = "idcard_validate")
    private Boolean idcardValidate;

    /**
     * 邮箱是否已经通过验证
     */
    @Column(name = "email_validate")
    private Boolean emailValidate;

    /**
     * 手机是否已经通过验证
     */
    @Column(name = "mobile_validate")
    private Boolean mobileValidate;

    /**
     * 是否开通理财顾问
     */
    @Column(name = "is_financial_advisor")
    private Boolean isFinancialAdvisor;

    /**
     * 是否开通信贷顾问
     */
    @Column(name = "is_credit_advisor")
    private Boolean isCreditAdvisor;

    /**
     * 是否开通担保人
     */
    @Column(name = "is_guarantee")
    private Boolean isGuarantee;

    @Column(name = "pay_password")
    private String payPassword;

    @Column(name = "sign_password")
    private String signPassword;

    /**
     * 获取id
     *
     * @return id - id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
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
     * 获取身份证是否验证
     *
     * @return idcard_validate - 身份证是否验证
     */
    public Boolean getIdcardValidate() {
        return idcardValidate;
    }

    /**
     * 设置身份证是否验证
     *
     * @param idcardValidate 身份证是否验证
     */
    public void setIdcardValidate(Boolean idcardValidate) {
        this.idcardValidate = idcardValidate;
    }

    /**
     * 获取邮箱是否已经通过验证
     *
     * @return email_validate - 邮箱是否已经通过验证
     */
    public Boolean getEmailValidate() {
        return emailValidate;
    }

    /**
     * 设置邮箱是否已经通过验证
     *
     * @param emailValidate 邮箱是否已经通过验证
     */
    public void setEmailValidate(Boolean emailValidate) {
        this.emailValidate = emailValidate;
    }

    /**
     * 获取手机是否已经通过验证
     *
     * @return mobile_validate - 手机是否已经通过验证
     */
    public Boolean getMobileValidate() {
        return mobileValidate;
    }

    /**
     * 设置手机是否已经通过验证
     *
     * @param mobileValidate 手机是否已经通过验证
     */
    public void setMobileValidate(Boolean mobileValidate) {
        this.mobileValidate = mobileValidate;
    }

    /**
     * 获取是否开通理财顾问
     *
     * @return is_financial_advisor - 是否开通理财顾问
     */
    public Boolean getIsFinancialAdvisor() {
        return isFinancialAdvisor;
    }

    /**
     * 设置是否开通理财顾问
     *
     * @param isFinancialAdvisor 是否开通理财顾问
     */
    public void setIsFinancialAdvisor(Boolean isFinancialAdvisor) {
        this.isFinancialAdvisor = isFinancialAdvisor;
    }

    /**
     * 获取是否开通信贷顾问
     *
     * @return is_credit_advisor - 是否开通信贷顾问
     */
    public Boolean getIsCreditAdvisor() {
        return isCreditAdvisor;
    }

    /**
     * 设置是否开通信贷顾问
     *
     * @param isCreditAdvisor 是否开通信贷顾问
     */
    public void setIsCreditAdvisor(Boolean isCreditAdvisor) {
        this.isCreditAdvisor = isCreditAdvisor;
    }

    /**
     * 获取是否开通担保人
     *
     * @return is_guarantee - 是否开通担保人
     */
    public Boolean getIsGuarantee() {
        return isGuarantee;
    }

    /**
     * 设置是否开通担保人
     *
     * @param isGuarantee 是否开通担保人
     */
    public void setIsGuarantee(Boolean isGuarantee) {
        this.isGuarantee = isGuarantee;
    }

    /**
     * @return pay_password
     */
    public String getPayPassword() {
        return payPassword;
    }

    /**
     * @param payPassword
     */
    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword == null ? null : payPassword.trim();
    }

    /**
     * @return sign_password
     */
    public String getSignPassword() {
        return signPassword;
    }

    /**
     * @param signPassword
     */
    public void setSignPassword(String signPassword) {
        this.signPassword = signPassword == null ? null : signPassword.trim();
    }
}