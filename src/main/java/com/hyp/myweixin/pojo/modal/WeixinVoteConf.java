package com.hyp.myweixin.pojo.modal;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import javax.persistence.*;

@Table(name = "weixin_vote_conf")
@Mapper
@Data
public class WeixinVoteConf {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 对应的投票基础表的ID
     */
    @Column(name = "active_vote_base_id")
    private Integer activeVoteBaseId;

    /**
     * 对应的音乐ID 音乐配置表中配置
     */
    @Column(name = "active_conf_music_id")
    private Integer activeConfMusicId;

    /**
     * 是否可以重复投票 0默认不开启 1开启
     */
    @Column(name = "active_conf_repeat_vote")
    private Integer activeConfRepeatVote=0;

    /**
     * 投票类型限制例如1天1票 投票类型配置表中设置
     */
    @Column(name = "active_conf_vote_type")
    private Integer activeConfVoteType;

    /**
     * 是否开启用户自己上传作品 0 默认不开启 1 开启
     */
    @Column(name = "active_conf_sign_up")
    private Integer activeConfSignUp=0;

    /**
     * 活动是否需要验证码 0默认不开启 1 开启
     */
    @Column(name = "active_conf_verify")
    private Integer activeConfVerify=0;

    /**
     * 投票数是否需要隐藏 0默认不开启 1开启
     */
    @Column(name = "active_conf_num_hide")
    private Integer activeConfNumHide=0;

    /**
     * 投票用户是否需要隐藏 0默认不开启 1开启
     */
    @Column(name = "active_conf_user_hide")
    private Integer activeConfUserHide=0;

    /**
     * 投票排名是否隐藏 0默认不开启 1开启
     */
    @Column(name = "active_conf_rank_hide")
    private Integer activeConfRankHide=0;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime=new Date();

    /**
     * 最后一次的修改时间
     */
    @Column(name = "update_time")
    private Date updateTime=new Date();

    /**
     * 用于分享的图片
     */
    @Column(name = "active_conf_share_img")
    private String activeConfShareImg="";

    }