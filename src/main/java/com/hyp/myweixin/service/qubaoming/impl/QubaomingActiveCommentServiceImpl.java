package com.hyp.myweixin.service.qubaoming.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.qubaoming.QubaomingActiveCommentMapper;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveComment;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingWeixinUser;
import com.hyp.myweixin.pojo.qubaoming.query.comment.ActiveCommentPageQuery;
import com.hyp.myweixin.pojo.qubaoming.vo.comment.ActiveCommentVO;
import com.hyp.myweixin.service.qubaoming.QubaomingActiveCommentService;
import com.hyp.myweixin.service.qubaoming.QubaomingWeixinUserService;
import com.hyp.myweixin.service.smallwechatapi.WeixinSmallContentDetectionApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/22 13:08
 * @Description: TODO
 */
@Slf4j
@Service
public class QubaomingActiveCommentServiceImpl implements QubaomingActiveCommentService {

    @Autowired
    private QubaomingActiveCommentMapper qubaomingActiveCommentMapper;
    @Autowired
    private QubaomingWeixinUserService qubaomingWeixinUserService;
    @Autowired
    private WeixinSmallContentDetectionApiService weixinSmallContentDetectionApiService;


    /**
     * 分页查询评价信息 按照时间先后顺序
     *
     * @param activeCommentPageQuery
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public PageInfo getPageInfoActiveCommentByActiveCommentPageQuery(ActiveCommentPageQuery activeCommentPageQuery) throws MyDefinitionException {
        if (activeCommentPageQuery == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        PageHelper.startPage(activeCommentPageQuery.getPageNum(), activeCommentPageQuery.getPageSize());
        PageInfo pageInfo = null;
        Example example = new Example(QubaomingActiveComment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("activeId", activeCommentPageQuery.getActiveId());
        example.orderBy("createTime").asc();
        List<QubaomingActiveComment> qubaomingActiveComments = null;
        try {
            qubaomingActiveComments = selectListByExample(example);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        pageInfo = new PageInfo(qubaomingActiveComments);


        List<ActiveCommentVO> qubaomingActiveCommentList = new ArrayList<>();
        for (QubaomingActiveComment qubaomingActiveComment : qubaomingActiveComments) {
            ActiveCommentVO activeCommentVO = new ActiveCommentVO();

            activeCommentVO.setCommentContent(qubaomingActiveComment.getCommentContent());
            activeCommentVO.setCreateTime(qubaomingActiveComment.getCreateTime());
            QubaomingWeixinUser qubaomingWeixinUser = null;
            try {
                qubaomingWeixinUser = qubaomingWeixinUserService.selectByPkId(qubaomingActiveComment.getUserId());
            } catch (MyDefinitionException e) {
                // do nothing
            }
            if (qubaomingWeixinUser != null) {
                activeCommentVO.setAvatar(qubaomingWeixinUser.getAvatarUrl());
                activeCommentVO.setUserName(qubaomingWeixinUser.getNickName());
            }
            qubaomingActiveCommentList.add(activeCommentVO);
        }

        pageInfo.setList(qubaomingActiveCommentList);
        return pageInfo;
    }

    /**
     * 添加评论信息
     *
     * @param userId
     * @param activeId
     * @param commentContent
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public Integer addActiveCommentByUserIdActiveIdContent(Integer userId, Integer activeId, String commentContent) throws MyDefinitionException {

        if (userId == null || activeId == null || commentContent == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        try {
            qubaomingWeixinUserService.validateUserRight(userId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        String accessTokenByAppName = null;
        try {
            accessTokenByAppName = weixinSmallContentDetectionApiService.getAccessTokenByAppName("qubaoming");
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        /*文本验证接口*/
        Boolean aBoolean = false;
        try {
            aBoolean = weixinSmallContentDetectionApiService.checkMsgSecCheckApi(commentContent, accessTokenByAppName);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (!aBoolean) {
            throw new MyDefinitionException("当前文字违规重新填写");
        }
        QubaomingActiveComment qubaomingActiveComment = QubaomingActiveComment.init();
        qubaomingActiveComment.setUserId(userId);
        qubaomingActiveComment.setActiveId(activeId);
        qubaomingActiveComment.setCommentContent(commentContent);
        Integer integer = null;
        try {
            integer = insertReturnPk(qubaomingActiveComment);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        return integer;
    }

    /**
     * 创建基础信息 并返回主键信息 要求参数必须是完整的数据
     *
     * @param qubaomingActiveComment
     * @return 主键
     * @throws MyDefinitionException
     */
    @Override
    public Integer insertReturnPk(QubaomingActiveComment qubaomingActiveComment) throws MyDefinitionException {
        if (qubaomingActiveComment == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        Integer pkId = null;
        try {
            pkId = null;
            int i = qubaomingActiveCommentMapper.insertUseGeneratedKeys(qubaomingActiveComment);
            if (i > 0) {
                pkId = qubaomingActiveComment.getId();
            }
        } catch (Exception e) {
            log.error("创建趣报名活动评论基础信息并返回主键信息操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("创建活动评论信息操作过程错误");
        }
        return pkId;
    }

    /**
     * 根据主键删除数据
     *
     * @param pkId 主键
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer deleteByPk(Integer pkId) throws MyDefinitionException {
        if (pkId == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        int i = 0;
        try {
            i = qubaomingActiveCommentMapper.deleteByPrimaryKey(pkId);
        } catch (Exception e) {
            log.error("根据主键删除趣报名活动评论数据操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("删除活动评论数据操作过程错误");
        }
        return i;
    }

    /**
     * 更新有值的数据信息 要求必须有主键信息
     *
     * @param qubaomingActiveComment
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer updateSelective(QubaomingActiveComment qubaomingActiveComment) throws MyDefinitionException {
        if (qubaomingActiveComment == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        int i = 0;
        try {
            i = qubaomingActiveCommentMapper.updateByPrimaryKeySelective(qubaomingActiveComment);
        } catch (Exception e) {
            log.error("更新有值的趣报名活动评论数据信息操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("更新活动评论数据信息操作过程错误");
        }
        return i;
    }

    /**
     * 通过主键查找数据 如果没有返回null
     *
     * @param pkId 主键
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    @Override
    public QubaomingActiveComment selectByPkId(Integer pkId) throws MyDefinitionException {
        if (pkId == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        QubaomingActiveComment qubaomingActiveComment = null;
        try {
            qubaomingActiveComment = qubaomingActiveCommentMapper.selectByPrimaryKey(pkId);
        } catch (Exception e) {
            log.error("通过主键查找趣报名活动评论数据操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("查找活动评论数据操作过程错误");
        }
        return qubaomingActiveComment;
    }

    /**
     * 通过活动ID查找数据 如果没有返回null
     *
     * @param activeId 活动ID
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    @Override
    public List<QubaomingActiveComment> selectListByActiveId(Integer activeId) throws MyDefinitionException {
        if (activeId == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        Example example = new Example(QubaomingActiveComment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("activeId", activeId);
        List<QubaomingActiveComment> qubaomingActiveComments = null;
        try {
            qubaomingActiveComments = qubaomingActiveCommentMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("通过活动ID查找趣报名活动评论数据操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("查找活动评论数据操作过程错误");
        }

        return qubaomingActiveComments;
    }


    /**
     * 通过查询条件查找数据 如果没有返回null
     *
     * @param example 查询条件
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    @Override
    public List<QubaomingActiveComment> selectListByExample(Example example) throws MyDefinitionException {
        if (example == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        List<QubaomingActiveComment> qubaomingActiveComments = null;
        try {
            qubaomingActiveComments = qubaomingActiveCommentMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("通过查询条件查找趣报名活动评论数据操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("查找活动评论数据操作过程错误");
        }
        return qubaomingActiveComments;
    }
}
