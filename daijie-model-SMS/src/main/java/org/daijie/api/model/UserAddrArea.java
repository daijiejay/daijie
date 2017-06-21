package org.daijie.api.model;

import javax.persistence.*;

@Table(name = "user_addr_area")
public class UserAddrArea {
    /**
     * 地区ID
     */
    @Id
    private Integer id;

    /**
     * 地区代码
     */
    private String code;

    /**
     * 父级代码
     */
    @Column(name = "parentId")
    private String parentid;

    /**
     * 地区名称
     */
    private String name;

    /**
     * 地区等级
     */
    private Boolean level;

    /**
     * 获取地区ID
     *
     * @return id - 地区ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置地区ID
     *
     * @param id 地区ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取地区代码
     *
     * @return code - 地区代码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置地区代码
     *
     * @param code 地区代码
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 获取父级代码
     *
     * @return parentId - 父级代码
     */
    public String getParentid() {
        return parentid;
    }

    /**
     * 设置父级代码
     *
     * @param parentid 父级代码
     */
    public void setParentid(String parentid) {
        this.parentid = parentid == null ? null : parentid.trim();
    }

    /**
     * 获取地区名称
     *
     * @return name - 地区名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置地区名称
     *
     * @param name 地区名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取地区等级
     *
     * @return level - 地区等级
     */
    public Boolean getLevel() {
        return level;
    }

    /**
     * 设置地区等级
     *
     * @param level 地区等级
     */
    public void setLevel(Boolean level) {
        this.level = level;
    }
}