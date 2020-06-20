package com.hyp.myweixin.pojo.modal;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 用户对于某个用户作品的投票信息
 */
@Table(name = "weixin_vote_user_work")
@Mapper
@Data
public class WeixinVoteUserWork {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 头像地址
     */
    @NotNull(message = "用户头像信息不可以为空")
    @Column(name = "avatar_url")
    private String avatarUrl = "";

    /**
     * 昵称
     */
    @NotNull(message = "用户昵称信息不可以为空")
    @Column(name = "nick_name")
    private String nickName = "";

    /**
     * 性别
     */
    private String gender = "";

    /**
     * 城市
     */
    private String city = "";

    /**
     * 省份
     */
    private String province = "";

    /**
     * 城市
     */
    private String country = "";

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

    /**
     * 对应投票作品的Id
     */
    @NotNull(message = "用户投票作品的Id不可以为空")
    @Column(name = "work_id")
    private Integer workId;


    /**
     * 微信用户唯一标识
     */
    @NotNull(message = "用户唯一标识不可以为空")
    @Column(name = "open_id")
    private String openId;

    /**
     * 所属IP(Ip地址string转long类型方便查询)
     */
    private Long ip;

    private String ext1 = "";

    private String ext2 = "";

    private String ext3 = "";

    private String ext4 = "";

    private String ext5 = "";

}