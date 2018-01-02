package org.daijie.social.login.baidu.model;

import org.daijie.social.login.LoginResult;

/**
 * 用户信息
 * @author daijie_jay
 * @since 2017年11月28日
 */
public class BaiduUserInfo implements LoginResult {
	
	/** 当前登录用户的数字ID **/
	String	userid;
	
	/** 当前登录用户的用户名，值可能为空。 **/
	String	username;
	
	/** 用户真实姓名，可能为空。 **/
	String	realname;
	
	/** 当前登录用户的头像 **/
	String	portrait;
	
	/** 自我简介，可能为空。 */
	String	userdetail;
	
	/** 生日，以yyyy-mm-dd格式显示。 **/
	String	birthday;
	
	/** 婚姻状况 **/
	String	marriage;
	
	/** 性别。"1"表示男，"0"表示女。 */
	String	sex;
	
	/** 血型 */
	String	blood;
	
	/** 体型 **/
	String	figure;
	
	/** 星座 **/
	String	constellation;
	
	/** 学历 **/
	String	education;
	
	/** 当前职业 */
	String	trade;
	
	/** 职位 **/
	String	job;
	
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public String getUserdetail() {
		return userdetail;
	}

	public void setUserdetail(String userdetail) {
		this.userdetail = userdetail;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getMarriage() {
		return marriage;
	}

	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBlood() {
		return blood;
	}

	public void setBlood(String blood) {
		this.blood = blood;
	}

	public String getFigure() {
		return figure;
	}

	public void setFigure(String figure) {
		this.figure = figure;
	}

	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getTrade() {
		return trade;
	}

	public void setTrade(String trade) {
		this.trade = trade;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	@Override
	public Boolean getResult() {
		return true;
	}}
