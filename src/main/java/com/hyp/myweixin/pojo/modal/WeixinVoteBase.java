package com.hyp.myweixin.pojo.modal;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "weixin_vote_base")
@Mapper
@Data
public class WeixinVoteBase {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 封面图片
     */
    @Column(name = "active_img")
    private String activeImg = "";

    /**
     * 活动名称
     */
    @Column(name = "active_name")
    private String activeName = "";

    /**
     * 活动描述
     */
    @Column(name = "active_desc")
    private String activeDesc = "";

    /**
     * 活动描述图片
     */
    @Column(name = "active_desc_img")
    private String activeDescImg = "";

    /**
     * 活动奖励
     */
    @Column(name = "active_reward")
    private String activeReward = "";

    /**
     * 活动奖励图片
     */
    @Column(name = "active_reward_img")
    private String activeRewardImg = "";

    /**
     * 活动开始时间
     */
    @Column(name = "active_start_time")
    private Date activeStartTime = new Date();

    /**
     * 活动结束时间
     */
    @Column(name = "active_end_time")
    private Date activeEndTime  = new Date();

    /**
     * 是否公开到首页 0默认公开 1不公开
     */
    @Column(name = "active_public")
    private Integer activePublic = 1;

    /**
     * 活动展示优先级 数值越高 优先级越高 默认为0 默认展示
     */
    @Column(name = "active_show_order")
    private Integer activeShowOrder = 0;

    /**
     * 0 待审核 1上线  2未开始 3已结束
     */
    private Integer status = 0;

    /**
     * 活动创建时间
     */
    @Column(name = "create_time")
    private Date createTime = new Date();

    /**
     * 最后的更新时间
     */
    @Column(name = "update_time")
    private Date updateTime = new Date();

    /**
     * 活动创建人Id
     */
    @Column(name = "create_sys_user_id")
    private Integer createSysUserId;


}