package com.hyp.myweixin.pojo.vo.page;

import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/20 13:23
 * @Description: TODO 作品与其他作品的差距
 */
@Data
public class WeixinVoteUserWorkDiffVO {

    /**
     * 当前作品的信息
     */
    private WeixinVoteWorkSimpleVO myWeixinVoteWorkSimpleVO;

    /**
     * 距离第一名的票数差距
     */
    private Integer bestDiff;
    /**
     * 第一名的个人信息
     */
    private WeixinVoteWorkSimpleVO bestWeixinVoteWorkSimpleVO;
    /**
     * 距离上一名次的票数差距
     */
    private Integer preDiff;
    /**
     * 上一名的个人信息
     */
    private WeixinVoteWorkSimpleVO preWeixinVoteWorkSimpleVO;
    /**
     * 距离下一名次的票数差距
     */
    private Integer nextDiff;
    /**
     * 下一名的个人信息
     */
    private WeixinVoteWorkSimpleVO nextWeixinVoteWorkSimpleVO;
    /*
    *//**
     * 被浏览总次数
     *//*
    private Integer viewCount;
    *//**
     * 被投票总次数
     *//*
    private Integer voteCount;
    *//**
     * 当前作品在活动中的序号
     *//*
    private Integer voteWorkOr;
    *//**
     * 作品的排名
     *//*
    private Integer voteWorkRank;*/

}
