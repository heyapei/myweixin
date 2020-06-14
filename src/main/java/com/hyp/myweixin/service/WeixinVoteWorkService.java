package com.hyp.myweixin.service;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinVoteWork;
import com.hyp.myweixin.pojo.vo.page.VoteDetailCompleteVO;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/8 17:30
 * @Description: TODO
 */
public interface WeixinVoteWorkService {
    /**
     * 通过voteBaseId获取当前这个活动有多少人参加
     *
     * @param voteBaseId
     * @return
     */
    Integer getCountWorkByVoteBaseId(Integer voteBaseId);


    /**
     * 通过voteBaseId获取当前这个活动有多少人投票
     *
     * @param voteBaseId
     * @return
     */
    Integer getCountVoteByVoteBaseId(Integer voteBaseId);


    /**
     * 分页查询活动的所有的作品
     *
     * @param weixinVoteWork
     * @param pageInfo
     * @return
     */
    PageInfo getVoteWorkAllWorkByPage(WeixinVoteWork weixinVoteWork, PageInfo pageInfo);

    /**
     * 分页查询活动的人气的作品
     *
     * @param weixinVoteWork
     * @param pageInfo
     * @return
     */
    PageInfo getVoteWorkHotWorkByPage(WeixinVoteWork weixinVoteWork, PageInfo pageInfo);


    /**
     * 通过userWorkId查询当前的详细信息
     *
     * @param userWorkId
     * @return
     */
    VoteDetailCompleteVO getWeixinVoteWorkByUserWorkId(Integer userWorkId);


    /**
     * 通过作品的ID更新被浏览次数
     *
     * @param userWorkId 用户作品ID
     * @return
     */
    int updateVoteWorkViewNum(Integer userWorkId);


    /**
     * 通过作品的ID查询作品
     *
     * @param userWorkId 用户作品ID
     * @return
     */
    WeixinVoteWork getVoteWorkByUserWorkId(Integer userWorkId);



    /**
     * 通过作品的ID更新被投票次数
     *
     * @param userWorkId 用户作品ID
     * @return
     */
    int updateVoteWorkVoteNum(Integer userWorkId);





}
