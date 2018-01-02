package org.daijie.social.login.sina.model;

import org.daijie.social.login.LoginResult;

/**
 * 用户信息
 * @author daijie_jay
 * @since 2017年11月28日
 */
public class SinaUserInfo implements LoginResult {
	
	/** 用户UID **/
	Long	id;
	
	/** 字符串型的用户UID **/
	String	idstr;
	
	/** 用户昵称 **/
	String	screen_name;
	
	/** 友好显示名称 **/
	String	name;
	
	/** 用户所在省级ID **/
	Integer	province;
	
	/** 用户所在城市ID **/
	Integer	city;
	
	/** 用户所在城市ID */
	String	location;
	
	/** 用户个人描述 **/
	String	description;
	
	/** 用户博客地址 **/
	String	url;
	
	/** 用户头像地址（中图），50×50像素 **/
	String	profile_image_url;
	
	/** 用户的微博统一URL地址 **/
	String	profile_url;
	
	/** 用户的个性化域名 **/
	String	domain;
	
	/** 用户的微号 **/
	String	weihao;
	
	/** 性别，m：男、f：女、n：未知 **/
	String	gender;
	
	/** 粉丝数 **/
	Integer	followers_count;
	
	/** 关注数 **/
	Integer	friends_count;
	
	/** 微博数 **/
	Integer	statuses_count;
	
	/** 收藏数 **/
	Integer	favourites_count;
	
	/** 用户创建（注册）时间 **/
	String	created_at;
	
	/** 暂未支持 **/
	boolean	following;
	
	/** 是否允许所有人给我发私信，true：是，false：否 **/
	boolean	allow_all_act_msg;
	
	/** 是否允许标识用户的地理位置，true：是，false：否 **/
	boolean	geo_enabled;
	
	/** 是否是微博认证用户，即加V用户，true：是，false：否 **/
	boolean	verified;
	
	/** 暂未支持 **/
	Integer	verified_type;
	
	/** 用户备注信息，只有在查询用户关系时才返回此字段 **/
	String	remark;
	
	// object status ; // 用户的最近一条微博信息字段 详细
	
	/** 是否允许所有人对我的微博进行评论，true：是，false：否 **/
	boolean	allow_all_comment;
	
	/** 用户头像地址（大图），180×180像素 **/
	String	avatar_large;
	
	/** 用户头像地址（高清），高清头像原图 **/
	String	avatar_hd;
	
	/** 认证原因 **/
	String	verified_reason;
	
	/** 该用户是否关注当前登录用户，true：是，false：否 **/
	boolean	follow_me;
	
	/** 用户的在线状态，0：不在线、1：在线 **/
	Integer	online_status;
	
	/** 用户的互粉数 **/
	Integer	bi_followers_count;
	
	/** 用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语 **/
	String	lang;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdstr() {
		return idstr;
	}

	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}

	public String getScreen_name() {
		return screen_name;
	}

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getProvince() {
		return province;
	}

	public void setProvince(Integer province) {
		this.province = province;
	}

	public Integer getCity() {
		return city;
	}

	public void setCity(Integer city) {
		this.city = city;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	public String getProfile_url() {
		return profile_url;
	}

	public void setProfile_url(String profile_url) {
		this.profile_url = profile_url;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getWeihao() {
		return weihao;
	}

	public void setWeihao(String weihao) {
		this.weihao = weihao;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getFollowers_count() {
		return followers_count;
	}

	public void setFollowers_count(Integer followers_count) {
		this.followers_count = followers_count;
	}

	public Integer getFriends_count() {
		return friends_count;
	}

	public void setFriends_count(Integer friends_count) {
		this.friends_count = friends_count;
	}

	public Integer getStatuses_count() {
		return statuses_count;
	}

	public void setStatuses_count(Integer statuses_count) {
		this.statuses_count = statuses_count;
	}

	public Integer getFavourites_count() {
		return favourites_count;
	}

	public void setFavourites_count(Integer favourites_count) {
		this.favourites_count = favourites_count;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public boolean isFollowing() {
		return following;
	}

	public void setFollowing(boolean following) {
		this.following = following;
	}

	public boolean isAllow_all_act_msg() {
		return allow_all_act_msg;
	}

	public void setAllow_all_act_msg(boolean allow_all_act_msg) {
		this.allow_all_act_msg = allow_all_act_msg;
	}

	public boolean isGeo_enabled() {
		return geo_enabled;
	}

	public void setGeo_enabled(boolean geo_enabled) {
		this.geo_enabled = geo_enabled;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public Integer getVerified_type() {
		return verified_type;
	}

	public void setVerified_type(Integer verified_type) {
		this.verified_type = verified_type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isAllow_all_comment() {
		return allow_all_comment;
	}

	public void setAllow_all_comment(boolean allow_all_comment) {
		this.allow_all_comment = allow_all_comment;
	}

	public String getAvatar_large() {
		return avatar_large;
	}

	public void setAvatar_large(String avatar_large) {
		this.avatar_large = avatar_large;
	}

	public String getAvatar_hd() {
		return avatar_hd;
	}

	public void setAvatar_hd(String avatar_hd) {
		this.avatar_hd = avatar_hd;
	}

	public String getVerified_reason() {
		return verified_reason;
	}

	public void setVerified_reason(String verified_reason) {
		this.verified_reason = verified_reason;
	}

	public boolean isFollow_me() {
		return follow_me;
	}

	public void setFollow_me(boolean follow_me) {
		this.follow_me = follow_me;
	}

	public Integer getOnline_status() {
		return online_status;
	}

	public void setOnline_status(Integer online_status) {
		this.online_status = online_status;
	}

	public Integer getBi_followers_count() {
		return bi_followers_count;
	}

	public void setBi_followers_count(Integer bi_followers_count) {
		this.bi_followers_count = bi_followers_count;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	@Override
	public Boolean getResult() {
		return true;
	}}
