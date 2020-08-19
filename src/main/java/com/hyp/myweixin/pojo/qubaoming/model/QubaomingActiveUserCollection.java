package com.hyp.myweixin.pojo.qubaoming.model;

import lombok.Data;

import javax.persistence.*;
@Data
@Table(name = "qubaoming_active_user_collection")
public class QubaomingActiveUserCollection {


    public static QubaomingActiveUserCollection init() {
        QubaomingActiveUserCollection qubaomingActiveUserCollection = new QubaomingActiveUserCollection();
        qubaomingActiveUserCollection.setUserId(0);
        qubaomingActiveUserCollection.setActiveId(0);
        qubaomingActiveUserCollection.setCreateTime(System.currentTimeMillis());
        qubaomingActiveUserCollection.setExt1("");
        qubaomingActiveUserCollection.setExt2("");
        qubaomingActiveUserCollection.setExt3("");
        qubaomingActiveUserCollection.setExt4("");
        qubaomingActiveUserCollection.setExt5("");
        return qubaomingActiveUserCollection;
    }


    @Id
    private Integer id;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 活动ID
     */
    @Column(name = "active_id")
    private Integer activeId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Long createTime;

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