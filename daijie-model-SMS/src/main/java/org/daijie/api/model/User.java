package org.daijie.api.model;

import java.util.Date;
import javax.persistence.*;

public class User {
    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 邮箱(验证通过后才会存在该值)
     */
    private String email;

    /**
     * 手机号(验证通过后才会存在该值)
     */
    private String mobile;

    /**
     * 昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 密码
     */
    private String password;

    /**
     * 新密码
     */
    private String passwd;

    /**
     * 登录key
     */
    @Column(name = "login_key")
    private String loginKey;

    /**
     * 用户类型：PERSONAL(个人)、COMPANY(企业)
     */
    @Column(name = "user_type")
    private String userType;

    /**
     * 禁用状态：1，启用；0，禁用；
     */
    private Boolean enable;

    /**
     * 拉黑状态：1，未拉黑；0，已拉黑
     */
    private Boolean defriend;

    /**
     * 是否虚拟账号：0，否；1，是
     */
    private Boolean fictitious;

    /**
     * 有效状态：0，已删除；1，有效
     */
    private Boolean status;

    /**
     * 注册时间
     */
    @Column(name = "reg_time")
    private Date regTime;

    /**
     * 注册ip
     */
    @Column(name = "registe_ip")
    private String registeIp;

    /**
     * 注册设备类型
     */
    @Column(name = "registe_device_type")
    private String registeDeviceType;

    /**
     * 注册时所用的应用版本
     */
    @Column(name = "registe_user_agent")
    private String registeUserAgent;

    /**
     * 注册渠道
     */
    private String channel;

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
     * 获取邮箱(验证通过后才会存在该值)
     *
     * @return email - 邮箱(验证通过后才会存在该值)
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱(验证通过后才会存在该值)
     *
     * @param email 邮箱(验证通过后才会存在该值)
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 获取手机号(验证通过后才会存在该值)
     *
     * @return mobile - 手机号(验证通过后才会存在该值)
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号(验证通过后才会存在该值)
     *
     * @param mobile 手机号(验证通过后才会存在该值)
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * 获取昵称
     *
     * @return nick_name - 昵称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 设置昵称
     *
     * @param nickName 昵称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 获取新密码
     *
     * @return passwd - 新密码
     */
    public String getPasswd() {
        return passwd;
    }

    /**
     * 设置新密码
     *
     * @param passwd 新密码
     */
    public void setPasswd(String passwd) {
        this.passwd = passwd == null ? null : passwd.trim();
    }

    /**
     * 获取登录key
     *
     * @return login_key - 登录key
     */
    public String getLoginKey() {
        return loginKey;
    }

    /**
     * 设置登录key
     *
     * @param loginKey 登录key
     */
    public void setLoginKey(String loginKey) {
        this.loginKey = loginKey == null ? null : loginKey.trim();
    }

    /**
     * 获取用户类型：PERSONAL(个人)、COMPANY(企业)
     *
     * @return user_type - 用户类型：PERSONAL(个人)、COMPANY(企业)
     */
    public String getUserType() {
        return userType;
    }

    /**
     * 设置用户类型：PERSONAL(个人)、COMPANY(企业)
     *
     * @param userType 用户类型：PERSONAL(个人)、COMPANY(企业)
     */
    public void setUserType(String userType) {
        this.userType = userType == null ? null : userType.trim();
    }

    /**
     * 获取禁用状态：1，启用；0，禁用；
     *
     * @return enable - 禁用状态：1，启用；0，禁用；
     */
    public Boolean getEnable() {
        return enable;
    }

    /**
     * 设置禁用状态：1，启用；0，禁用；
     *
     * @param enable 禁用状态：1，启用；0，禁用；
     */
    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    /**
     * 获取拉黑状态：1，未拉黑；0，已拉黑
     *
     * @return defriend - 拉黑状态：1，未拉黑；0，已拉黑
     */
    public Boolean getDefriend() {
        return defriend;
    }

    /**
     * 设置拉黑状态：1，未拉黑；0，已拉黑
     *
     * @param defriend 拉黑状态：1，未拉黑；0，已拉黑
     */
    public void setDefriend(Boolean defriend) {
        this.defriend = defriend;
    }

    /**
     * 获取是否虚拟账号：0，否；1，是
     *
     * @return fictitious - 是否虚拟账号：0，否；1，是
     */
    public Boolean getFictitious() {
        return fictitious;
    }

    /**
     * 设置是否虚拟账号：0，否；1，是
     *
     * @param fictitious 是否虚拟账号：0，否；1，是
     */
    public void setFictitious(Boolean fictitious) {
        this.fictitious = fictitious;
    }

    /**
     * 获取有效状态：0，已删除；1，有效
     *
     * @return status - 有效状态：0，已删除；1，有效
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * 设置有效状态：0，已删除；1，有效
     *
     * @param status 有效状态：0，已删除；1，有效
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * 获取注册时间
     *
     * @return reg_time - 注册时间
     */
    public Date getRegTime() {
        return regTime;
    }

    /**
     * 设置注册时间
     *
     * @param regTime 注册时间
     */
    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    /**
     * 获取注册ip
     *
     * @return registe_ip - 注册ip
     */
    public String getRegisteIp() {
        return registeIp;
    }

    /**
     * 设置注册ip
     *
     * @param registeIp 注册ip
     */
    public void setRegisteIp(String registeIp) {
        this.registeIp = registeIp == null ? null : registeIp.trim();
    }

    /**
     * 获取注册设备类型
     *
     * @return registe_device_type - 注册设备类型
     */
    public String getRegisteDeviceType() {
        return registeDeviceType;
    }

    /**
     * 设置注册设备类型
     *
     * @param registeDeviceType 注册设备类型
     */
    public void setRegisteDeviceType(String registeDeviceType) {
        this.registeDeviceType = registeDeviceType == null ? null : registeDeviceType.trim();
    }

    /**
     * 获取注册时所用的应用版本
     *
     * @return registe_user_agent - 注册时所用的应用版本
     */
    public String getRegisteUserAgent() {
        return registeUserAgent;
    }

    /**
     * 设置注册时所用的应用版本
     *
     * @param registeUserAgent 注册时所用的应用版本
     */
    public void setRegisteUserAgent(String registeUserAgent) {
        this.registeUserAgent = registeUserAgent == null ? null : registeUserAgent.trim();
    }

    /**
     * 获取注册渠道
     *
     * @return channel - 注册渠道
     */
    public String getChannel() {
        return channel;
    }

    /**
     * 设置注册渠道
     *
     * @param channel 注册渠道
     */
    public void setChannel(String channel) {
        this.channel = channel == null ? null : channel.trim();
    }
}