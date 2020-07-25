package com.hyp.myweixin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.WeixinVoteWorkCommentMapper;
import com.hyp.myweixin.pojo.modal.WeixinVoteUser;
import com.hyp.myweixin.pojo.modal.WeixinVoteWork;
import com.hyp.myweixin.pojo.modal.WeixinVoteWorkComment;
import com.hyp.myweixin.pojo.vo.page.WeixinVoteWorkCommentVO;
import com.hyp.myweixin.service.WeixinVoteUserService;
import com.hyp.myweixin.service.WeixinVoteWorkCommentService;
import com.hyp.myweixin.service.WeixinVoteWorkService;
import com.hyp.myweixin.service.smallwechatapi.WeixinSmallContentDetectionApiService;
import com.hyp.myweixin.utils.MyEntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/20 12:26
 * @Description: TODO
 */
@Service
@Slf4j
public class WeixinVoteWorkCommentServiceImpl implements WeixinVoteWorkCommentService {


    @Autowired
    private WeixinVoteWorkCommentMapper weixinVoteWorkCommentMapper;
    @Autowired
    private WeixinVoteUserService weixinVoteUserService;
    @Autowired
    private WeixinVoteWorkService weixinVoteWorkService;
    @Autowired
    private WeixinSmallContentDetectionApiService weixinSmallContentDetectionApiService;

    /**
     * 添加作品评论
     *
     * @param weixinVoteWorkComment
     * @return
     */
    @Override
    public int addWeixinVoteWorkComment(WeixinVoteWorkComment weixinVoteWorkComment) {

        WeixinVoteUser userById = weixinVoteUserService.getUserById(weixinVoteWorkComment.getVoteUserId());
        if (userById == null) {
            log.error("保存用户评论错误，需要保存的数据：{}，错误原因：{}", weixinVoteWorkComment.toString(), "未发现评论用户");
            throw new MyDefinitionException("保存用户评论错误，未发现评论用户");
        }

        WeixinVoteWork voteWorkByUserWorkId = weixinVoteWorkService.getVoteWorkByUserWorkId(weixinVoteWorkComment.getVoteWorkId());
        if (voteWorkByUserWorkId == null) {
            log.error("保存用户评论错误，需要保存的数据：{}，错误原因：{}", weixinVoteWorkComment.toString(), "未发现作品");
            throw new MyDefinitionException("保存用户评论错误，未发现作品");
        }

        Boolean aBoolean = null;
        try {
            aBoolean = weixinSmallContentDetectionApiService.checkMsgSecCheckApi(weixinVoteWorkComment.getWorkComment(),
                    null);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }

        if (aBoolean == null || aBoolean == false) {
            throw new MyDefinitionException("当前提交的文字内容违规，请重新输入");
        }

        int commentOr = 1;
        WeixinVoteWorkComment latestCommentByWorkId = getLatestCommentByWorkId(weixinVoteWorkComment.getVoteWorkId());
        if (latestCommentByWorkId != null) {
            commentOr = latestCommentByWorkId.getCommentOr() + 1;
        }

        weixinVoteWorkComment.setCommentOr(commentOr);

        try {
            weixinVoteWorkCommentMapper.insertUseGeneratedKeys(weixinVoteWorkComment);
        } catch (Exception e) {
            log.error("保存用户评论错误，需要保存的数据：{}，错误原因：{}", weixinVoteWorkComment.toString(), e.toString());
            throw new MyDefinitionException("保存用户评论错误");
        }

        return weixinVoteWorkComment.getId();
    }

    /**
     * 分页查询评论信息
     *
     * @param weixinVoteWorkComment
     * @param pageInfo
     * @return
     */
    @Override
    public PageInfo getWeixinVoteWorkCommentListByPage(WeixinVoteWorkComment weixinVoteWorkComment, PageInfo pageInfo) {
        Example example = new Example(WeixinVoteWorkComment.class);
        Example.Criteria criteria = example.createCriteria();
        example.orderBy("showOrder").desc();
        example.orderBy("likeCount").desc();
        example.orderBy("createTime").asc();


        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        //TODO　weixinVoteWorkComment用于条件查询
        if (weixinVoteWorkComment != null) {
            criteria.andEqualTo("voteWorkId", weixinVoteWorkComment.getVoteWorkId());
        }
        List<WeixinVoteWorkComment> weixinVoteWorkCommentList = weixinVoteWorkCommentMapper.selectByExample(example);
        // 如果这里需要返回VO，那么这里一定先把查询值放进去，让分页信息存储成功。然后再setList加入VO信息
        pageInfo = new PageInfo(weixinVoteWorkCommentList);
        return pageInfo;
    }

    /**
     * 分页查询评论信息的相关内容 包括人员头像
     *
     * @param pageInfo
     * @return
     */
    @Override
    public PageInfo getWeixinVoteWorkCommentVOByPageInfo(PageInfo pageInfo) {
        List<WeixinVoteWorkComment> list = pageInfo.getList();
        List<WeixinVoteWorkCommentVO> list1 = new ArrayList<>();
        for (WeixinVoteWorkComment weixinVoteWorkComment : list) {
            WeixinVoteWorkCommentVO weixinVoteWorkCommentVO = MyEntityUtil.entity2VM(weixinVoteWorkComment, WeixinVoteWorkCommentVO.class);
            WeixinVoteUser weixinVoteUser = weixinVoteUserService.getUserById(weixinVoteWorkComment.getVoteUserId());
            if (weixinVoteUser != null) {
                weixinVoteWorkCommentVO.setAvatarUrl(weixinVoteUser.getAvatarUrl());
                weixinVoteWorkCommentVO.setNickName(weixinVoteUser.getNickName());
                list1.add(weixinVoteWorkCommentVO);
            }
        }
        pageInfo.setList(list1);
        return pageInfo;
    }

    /**
     * 分页查询评论信息 通过查询
     *
     * @param weixinVoteWorkComment
     * @param pageInfo
     * @return
     */
    @Override
    public PageInfo getWeixinVoteWorkCommentVOByPageInfo(WeixinVoteWorkComment weixinVoteWorkComment, PageInfo pageInfo) {
        PageInfo weixinVoteWorkCommentListByPage = getWeixinVoteWorkCommentListByPage(weixinVoteWorkComment, pageInfo);
        if (weixinVoteWorkCommentListByPage == null) {
            return null;
        } else {
            return getWeixinVoteWorkCommentVOByPageInfo(weixinVoteWorkCommentListByPage);
        }
    }

    /**
     * 查询最新的一条评论
     *
     * @param workId
     * @return
     */
    @Override
    public WeixinVoteWorkComment getLatestCommentByWorkId(Integer workId) {
        WeixinVoteWorkComment weixinVoteWorkComment = null;
        Example example = new Example(WeixinVoteWorkComment.class);
        Example.Criteria criteria = example.createCriteria();
        example.orderBy("createTime").desc();
        List<WeixinVoteWorkComment> weixinVoteWorkCommentList = weixinVoteWorkCommentMapper.selectByExample(example);
        if (weixinVoteWorkCommentList != null && weixinVoteWorkCommentList.size() > 0) {
            weixinVoteWorkComment = weixinVoteWorkCommentList.get(0);
        }
        return weixinVoteWorkComment;
    }
}
