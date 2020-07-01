package com.hyp.myweixin.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/30 22:03
 * @Description: TODO 活动创建  接收
 */
@Data
public class WeixinVoteWorkDTO {



    /*活动的初始数据*/
    /**
     * 封面图片
     */
    @NotNull(message = "活动封面图片必填")
    private String activeImg;

    /**
     * 活动名称
     */
    @NotNull(message = "活动名称必填")
    private String activeName;

    /**
     * 活动描述
     */
    @NotNull(message = "活动描述必填")
    private String activeDesc;

    /**
     * 活动描述图片
     */
    @NotNull(message = "活动描述图片必填")
    private String activeDescImg;

    /**
     * 活动奖励
     */
    private String activeReward;

    /**
     * 活动奖励图片
     */
    private String activeRewardImg;

    /**
     * 活动开始时间
     */
    @NotNull(message = "活动开始时间必填")
    private Date activeStartTime;

    /**
     * 活动结束时间
     */
    @NotNull(message = "活动结束时间必填")
    private Date activeEndTime;

    /**
     * 是否公开到首页 0默认公开 1不公开
     */
    @NotNull(message = "是否公布到首页必填  0默认公开 1不公开 ")
    private Integer activePublic;
    /**
     * 活动创建人Id
     */
    @NotNull(message = "活动创建人必填")
    private Integer createSysUserId;


    /*活动配置数据*/
    /**
     * 对应的投票基础表的ID
     */
    private Integer activeVoteBaseId;

    /**
     * 对应的音乐ID 音乐配置表中配置
     */
    private Integer activeConfMusicId;

    /**
     * 是否可以重复投票 0默认不开启 1开启
     */
    private Integer activeConfRepeatVote;

    /**
     * 投票类型限制例如1天1票 投票类型配置表中设置
     */
    private Integer activeConfVoteType;

    /**
     * 是否开启用户自己上传作品 0 默认开启 1 不开启
     */
    private Integer activeConfSignUp;

    /**
     * 活动是否需要验证码 0默认不开启 1 开启
     */
    private Integer activeConfVerify;

    /**
     * 投票数是否需要隐藏 0默认不开启 1开启
     */
    private Integer activeConfNumHide;

    /**
     * 投票用户是否需要隐藏 0默认不开启 1开启
     */
    private Integer activeConfUserHide;

    /**
     * 投票排名是否隐藏 0默认不开启 1开启
     */
    private Integer activeConfRankHide;

    /**
     * 最后一次的修改时间
     */
    private Date updateTime;

    /**
     * 允许上传开始时间
     */
    private Date activeUploadStartTime;

    /**
     * 允许上传结束时间
     */
    private Date activeUploadEndTime;

    /**
     * 性别限制 （无 男 女）
     */
    private String activeConfSex;

    /**
     * 地区限制（无 地理位置）
     */
    private String activeConfRegion;

    /**
     * 是否需要微信号（0 默认不需要 1 需要）
     */
    private Integer activeConfNeedWeixin;

    /**
     * 是否需要手机号（0 默认不需要 1 需要）
     */
    private Integer activeConfNeedPhone;
    /**
     * 用于分享的图片
     */
    private String activeConfShareImg;





    /*主办方信息*/
    /**
     * 名称
     */
    private String name;
    /**
     * logo图片
     */
    private String logoImg;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 公司二维码图片路径
     */
    private String weixinQrCode;


}
