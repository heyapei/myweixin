package com.hyp.myweixin.pojo.modal;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author heyapei
 */
@Table(name = "weixin_vote_user")
@Mapper
@Data
public class WeixinVoteUser {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 用户微信中的唯一标识
     */
    @Column(name = "open_id")
    private String openId;

    /**
     * 头像地址
     */
    @Column(name = "avatar_url")
    private String avatarUrl;

    /**
     * 昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 城市
     */
    private String city;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String country;

    /**
     * 是否允许使用 0允许 1不允许
     */
    private Integer enable;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime = new Date();

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime = new Date();

}