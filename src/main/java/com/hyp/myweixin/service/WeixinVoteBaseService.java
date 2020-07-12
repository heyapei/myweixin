package com.hyp.myweixin.service;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.pojo.vo.page.ActiveWorkRankVO;
import com.hyp.myweixin.pojo.vo.page.VoteDetailByWorkIdVO;
import com.hyp.myweixin.pojo.vo.page.VoteDetailTwoByWorkIdVO;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/7 20:09
 * @Description: TODO
 */
public interface WeixinVoteBaseService {


    /**
     * 通过活动信息页面点入活动详情页 通过ID查询
     *
     * @param voteWorkId 活动的主键
     * @return
     * @throws MyDefinitionException
     */
    VoteDetailTwoByWorkIdVO getVoteDetailTwoByWorkIdVOById(Integer voteWorkId) throws MyDefinitionException;


    /**
     * 更新内容 按照主键 只更新有值的内容
     *
     * @param weixinVoteBase 活动内容
     * @return
     */
    int updateVoteBaseVote(WeixinVoteBase weixinVoteBase);


    /**
     * 查询用户名下的活动按照活动的状态值
     *
     * @param userId 用户ID
     * @param status 活动状态值
     * @return
     */
    List<WeixinVoteBase> getWeixinVoteBaseByUserIdAndStatus(int userId, int status);


    /**
     * 通过活动的ID查询活动的详情 完整实体类
     *
     * @param workId 活动ID
     * @return
     */
    WeixinVoteBase getWeixinVoteBaseByWorkId(Integer workId);


    /**
     * 保存活动基础表
     *
     * @param weixinVoteBase
     * @return 主键
     */
    Integer saveVoteBase(WeixinVoteBase weixinVoteBase);


    /**
     * 分页查询活动下作品的排行榜
     *
     * @param activeId 活动ID
     * @param pageInfo 分页信息
     * @return
     */
    ActiveWorkRankVO getActiveWorkRank(Integer activeId, PageInfo pageInfo);


    /**
     * 分页查询投票活动列表
     *
     * @param weixinVoteBase
     * @param pageInfo
     * @return
     */
    PageInfo getVoteWorkByPage(WeixinVoteBase weixinVoteBase, PageInfo pageInfo);


    /**
     * 通过活动的ID查询活动的详情
     *
     * @param workId 活动ID
     * @return
     */
    VoteDetailByWorkIdVO getVoteWorkByWorkId(Integer workId);


    /**
     * 通过活动的ID更新被浏览次数
     *
     * @param workId 活动ID
     * @return
     */
    int updateVoteBaseViewNum(Integer workId);


    /**
     * 通过workId记录点赞数
     *
     * @param workId 活动ID
     * @return
     */
    int updateVoteBaseVoteNum(Integer workId);


}
