package com.hyp.myweixin.pojo.vo.page;

import lombok.Data;

import java.util.Date;


/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/10 19:25
 * @Description: TODO 用于通过活动信息页面点入活动详情页
 */
@Data
public class VoteDetailTwoByWorkIdVO {

    public static VoteDetailTwoByWorkIdVO init() {
        VoteDetailTwoByWorkIdVO voteDetailTwoByWorkIdVO = new VoteDetailTwoByWorkIdVO();
        voteDetailTwoByWorkIdVO.setActiveImg("");
        voteDetailTwoByWorkIdVO.setActiveName("");
        voteDetailTwoByWorkIdVO.setActiveStartTime(new Date());
        voteDetailTwoByWorkIdVO.setActiveEndTime(new Date());
        voteDetailTwoByWorkIdVO.setOrganisersName("");
        voteDetailTwoByWorkIdVO.setOrganisersLogoImg("");
        voteDetailTwoByWorkIdVO.setOrganisersPhone("");
        voteDetailTwoByWorkIdVO.setActiveRule("");
        voteDetailTwoByWorkIdVO.setActiveRewardDesc("");
        voteDetailTwoByWorkIdVO.setActiveConfRepeatVote(0);
        voteDetailTwoByWorkIdVO.setActiveConfVoteType(1);
        return voteDetailTwoByWorkIdVO;

    }


    /**
     * 活动图
     */
    private String activeImg;
    /**
     * 活动名称
     */
    private String activeName;

    /**
     * 活动介绍
     */
    private String activeDesc;
    /**
     * 活动图片
     */
    private String[] activeDescImgS;
    /**
     * 活动开始时间
     */
    private Date activeStartTime;
    /**
     * 互动结束时间
     */
    private Date activeEndTime;
    /**
     * 活动组办方名称
     */
    private String organisersName;
    /**
     * 活动组办方logo
     */
    private String organisersLogoImg;

    /**
     * 活动组办方联系电话
     */
    private String organisersPhone;
    /**
     * 活动规则
     */
    private String activeRule;

    /**
     * 活动奖励描述
     */
    private String activeRewardDesc;
    /**
     * 活动奖励图片
     */
    private String[] activeRewardImgS;


    /**
     * 是否允许投票
     */
    private Integer activeConfRepeatVote;
    /**
     * 允许投票模式
     */
    private Integer activeConfVoteType;

    /**
     * 允许投票次数
     */
    private Integer activeConfVoteTypeNum;



}
