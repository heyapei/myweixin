package com.hyp.myweixin.service;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.pojo.modal.WeixinVoteUserWork;

import java.util.Date;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/14 18:57
 * @Description: TODO
 */
public interface WeixinVoteUserWorkService {


    /**
     * 按照时间范围查询用户对某个作品的投票数据 如果没有传入开始结束/结束时间 则直接取今天的时间开始/结束
     * @param openId
     * @param userWorkId
     * @param startTime
     * @param endTime
     * @return
     */
     List<WeixinVoteUserWork> getWeixinVoteUserWorkSByOpenIdTime(String openId, Integer userWorkId, Date startTime, Date endTime);

    /**
     * 通过openId获取获取用户的信息
     *
     * @param openId 用户唯一值
     * @param userWorkId 用户作品ID
     * @return
     */
    List<WeixinVoteUserWork> getWeixinVoteUserWorkSByOpenId(String openId, Integer userWorkId);


    /**
     * 判断当前投票的合法性
     *
     * @param weixinVoteUserWork
     * @return
     */
    String judgeVoteLegal(WeixinVoteUserWork weixinVoteUserWork);


    /**
     * 保存用户对某个作品的投票记录
     *
     * @param weixinVoteUserWork
     * @return
     */
    int addUserVote(WeixinVoteUserWork weixinVoteUserWork);


    /**
     * 查询活动下面的作品
     *
     * @param activeId
     * @return
     */
    List<WeixinVoteUserWork> getWeixinVoteUserWorkByWorkId(Integer activeId);


    /**
     * 获取用户作品投票人的信息
     *
     * @param weixinVoteUserWork
     * @param pageInfo
     * @return
     */
    PageInfo getWeixinVoteUserWorkByPage(WeixinVoteUserWork weixinVoteUserWork, PageInfo pageInfo);


    /**
     * 获取投票人的头像信息
     *
     * @param pageInfo
     * @return
     */
    PageInfo getWeixinVoteUserWorkSimpleVOByPageInfo(PageInfo pageInfo);

}
