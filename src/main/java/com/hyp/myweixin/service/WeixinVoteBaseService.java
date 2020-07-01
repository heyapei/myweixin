package com.hyp.myweixin.service;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.pojo.vo.page.ActiveWorkRankVO;
import com.hyp.myweixin.pojo.vo.page.VoteDetailByWorkIdVO;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/7 20:09
 * @Description: TODO
 */
public interface WeixinVoteBaseService {

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
     * @return
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
