package org.daijie.jdbc;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * @author jerry.dai
 * @version SDP_V2.00.00
 * @date 2019/8/29
 */
public class UserDetailVo {

    @Column(table = "user", name = "user_id")
    private Integer userId;

    @Column(table = "user", name = "user_name")
    private String userName;

    @Column(table = "user", name = "remark")
    private String remark;

    @Column(table = "user", name = "create_date")
    private Date createDate;

    @Column(table = "user_info", name = "mobile")
    private String mobile;

    @Column(table = "user_info", name = "email")
    private String email;

    @Column(table = "user_linkman")
    private List<UserLinkman> userLinkmens;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<UserLinkman> getUserLinkmens() {
        return userLinkmens;
    }

    public void setUserLinkmens(List<UserLinkman> userLinkmens) {
        this.userLinkmens = userLinkmens;
    }
}
