package com.hyp.myweixin.service;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinVoteUserWork;
import com.hyp.myweixin.pojo.modal.WeixinVoteWork;
import com.hyp.myweixin.pojo.query.userwork.UpdateUserWorkQuery;
import com.hyp.myweixin.pojo.query.voteuserwork.ActiveUserWorkQuery;
import com.hyp.myweixin.pojo.query.voteuserwork.SaveVoteUserQuery;
import com.hyp.myweixin.pojo.query.voteuserwork.UpdateUserWorkStatusQuery;
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
     * 更新用户作品
     * 1. 要求是管理员
     *
     * @param updateUserWorkQuery 前端上传回来的用户作品数据
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer updateWeixinVoteWorkAdmin(UpdateUserWorkQuery updateUserWorkQuery) throws MyDefinitionException;


    /**
     * 通过主键删除作品
     * 1. 要求是管理员权限
     *
     * @param userWorkId 活动ID
     * @param userId     用户ID
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer deleteUserWorkByWorkIdAdmin(Integer userWorkId, Integer userId) throws MyDefinitionException;


    /**
     * 获取userId下所有的作品数据
     *
     * @param userId 用户ID
     * @return
     * @throws MyDefinitionException
     */
    List<WeixinVoteWork> getWeixinVoteWorkListByUserId(Integer userId) throws MyDefinitionException;


    /**
     * 获取activeId下面的根据状态值查询所有符合要求的作品
     *
     * @param activeId 活动ID 必填
     * @param status   状态值 如果为null则不进入查询条件
     * @return
     * @throws MyDefinitionException
     */
    List<WeixinVoteWork> getWeixinVoteWorkListByWorkStatus(Integer activeId, Integer status) throws MyDefinitionException;


    /**
     * 更新作品的状态
     *
     * @param updateUserWorkStatusQuery
     * @return 返回更新的行数
     * @throws MyDefinitionException
     */
    Integer updateUserWorkStatus(UpdateUserWorkStatusQuery updateUserWorkStatusQuery) throws MyDefinitionException;


    /**
     * 通过查询条件获取当前活动下面的数据 当然分页查询
     *
     * @param activeUserWorkQuery 活动下作品的数据
     * @return 作品列表
     * @throws MyDefinitionException
     */
    PageInfo<WeixinVoteWork> getUserWorkListByTypePage(ActiveUserWorkQuery activeUserWorkQuery) throws MyDefinitionException;


    /**
     * 通过userID和activeId查询某个人在某个活动中提交作品的内容
     *
     * @param userId   用户ID
     * @param activeId 活动ID
     * @return 作品列表
     * @throws MyDefinitionException
     */
    List<WeixinVoteWork> getWeiXinVoteWorkListByUserId(Integer userId, Integer activeId) throws MyDefinitionException;


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

    /**
     * 通过主键删除作品
     *
     * @param pkId 主键
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer deleteByPkId(Integer pkId) throws MyDefinitionException;


    /**
     * 按照主键更新选择性数据
     *
     * @param weixinVoteWork 数据
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer updateSelectiveByPkId(WeixinVoteWork weixinVoteWork) throws MyDefinitionException;

}
