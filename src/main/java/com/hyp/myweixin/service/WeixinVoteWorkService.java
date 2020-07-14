package com.hyp.myweixin.service;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinVoteWork;
import com.hyp.myweixin.pojo.query.voteuserwork.SaveVoteUserQuery;
import com.hyp.myweixin.pojo.vo.page.VoteDetailCompleteVO;
import com.hyp.myweixin.pojo.vo.page.WeixinVoteUserWorkDiffVO;
import com.hyp.myweixin.pojo.vo.result.Result;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/8 17:30
 * @Description: TODO
 */
public interface WeixinVoteWorkService {

    /**
     * 用户上传个人的作品  需要完整属性
     *
     * @param saveVoteUserQuery 前端上传回来的用户作品数据
     * @return 如果有错误返回错误信息
     * @throws MyDefinitionException
     */
    Result createWeixinVoteWorkReturnPK(SaveVoteUserQuery saveVoteUserQuery) throws MyDefinitionException;


    /**
     * 查询一个活动下面有多少作品数量了 通过活动ID查询
     *
     * @param voteWorkId 活动的主键
     * @return 作品数量
     * @throws MyDefinitionException
     */
    Integer getUserWorkCountByActiveId(Integer voteWorkId) throws MyDefinitionException;


    /**
     * 获取作品点赞比当前作品多的作品
     *
     * @param activeId
     * @param workId
     * @return
     */
    List<WeixinVoteWork> getThanWorkWeixinVoteWork(Integer activeId, Integer workId);


    /**
     * 获取作品点赞比当前作品少的作品
     *
     * @param activeId
     * @param workId
     * @return
     */
    List<WeixinVoteWork> getLessWorkWeixinVoteWork(Integer activeId, Integer workId);


    /**
     * 获取作品在活动中的排名
     *
     * @param activeId
     * @param workId
     * @return
     */
    Integer getRankNumByUserWorkId(Integer activeId, Integer workId);

    /**
     * 查询当前作品的差距
     *
     * @param workId
     * @return
     */
    WeixinVoteUserWorkDiffVO getUserWorkDiff(Integer workId);


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

    /**
     * 保存用户作品 返回主键 需要完整属性
     *
     * @param weixinVoteWork
     * @return 创建完成后的主键
     * @throws MyDefinitionException
     */
    Integer saveWeixinVoteWorkReturnPK(WeixinVoteWork weixinVoteWork) throws MyDefinitionException;


}
