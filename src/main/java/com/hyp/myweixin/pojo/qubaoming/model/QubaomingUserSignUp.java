package com.hyp.myweixin.pojo.qubaoming.model;

import lombok.Data;

import javax.persistence.*;
@Data
@Table(name = "qubaoming_user_sign_up")
public class QubaomingUserSignUp {

    public static QubaomingUserSignUp init() {
        QubaomingUserSignUp qubaomingUserSignUp = new QubaomingUserSignUp();
        qubaomingUserSignUp.setActiveId(0);
        qubaomingUserSignUp.setUserId(0);
        qubaomingUserSignUp.setSignUpInfo("");
        qubaomingUserSignUp.setCreateTime(System.currentTimeMillis());
        qubaomingUserSignUp.setUpdateTime(System.currentTimeMillis());
        qubaomingUserSignUp.setExt1("");
        qubaomingUserSignUp.setExt2("");
        qubaomingUserSignUp.setExt3("");
        qubaomingUserSignUp.setExt4("");
        qubaomingUserSignUp.setExt5("");
        return qubaomingUserSignUp;
    }


    @Id
    private Integer id;

    /**
     * 创建时间
     */
    @Column(name = "active_id")
    private Integer activeId;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 用户报名数据
     */
    @Column(name = "sign_up_info")
    private String signUpInfo;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Long createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Long updateTime;

    /**
     * 备用字段
     */
    private String ext1;

    /**
     * 备用字段
     */
    private String ext2;

    /**
     * 备用字段
     */
    private String ext3;

    /**
     * 备用字段
     */
    private String ext4;

    /**
     * 备用字段
     */
    private String ext5;

}