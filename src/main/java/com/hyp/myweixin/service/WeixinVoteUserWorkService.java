package com.hyp.myweixin.service;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinVoteUserWork;
import com.hyp.myweixin.pojo.vo.page.activeeditor.UserWorkDetailVO;

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
     * 管理员查看作品详情
     *
     * @param userId
     * @param userWorkId
     * @return
     * @throws MyDefinitionException
     */
    UserWorkDetailVO getUserWorkDetailByWorkId(Integer userId, Integer userWorkId) throws MyDefinitionException;



    /**
     * 获取剩余票数
     *
     * @param baseId
     * @param openId
     * @return
     * @throws MyDefinitionException
     */
    Integer getRemainderByOpenIdTime(Integer baseId, String openId) throws MyDefinitionException;


    /**
     * 通过活动基础表 用户openId 查询用户在这个活动中的投票情况 有时间范围
     *
     * @param baseId    活动ID
     * @param openId    用户标识
     * @param workId    作品ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 在时间范围内容 某个用户在一定时间范围内的投票总数
     * @throws MyDefinitionException
     */
    Integer getWeixinVoteUserWorkNumByOpenIdTime(Integer baseId, String openId, Integer workId,
                                                 Date startTime, Date endTime) throws MyDefinitionException;


    /**
     * 按照时间范围查询用户对某个作品的投票数据 如果没有传入开始结束/结束时间 则直接取今天的时间开始/结束
     *
     * @param openId
     * @param userWorkId
     * @param startTime
     * @param endTime
     * @return
     */
    List<WeixinVoteUserWork> getWeixinVoteUserWorkSByOpenIdTime(String openId, Integer userWorkId, Date startTime, Date endTime);

    /**
     * 查询用户对某个作品的投票数据
     *
     * @param openId
     * @param userWorkId
     * @return
     */
    List<WeixinVoteUserWork> getWeixinVoteUserWorkS(String openId, Integer userWorkId);


    /**
     * 通过openId获取获取用户的信息
     *
     * @param openId     用户唯一值
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
