package com.hyp.myweixin.service;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.pojo.modal.WeixinVoteWorkComment;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/20 12:22
 * @Description: TODO
 */
public interface WeixinVoteWorkCommentService {

    /**
     * 添加作品评论
     *
     * @param weixinVoteWorkComment
     * @return
     */
    int addWeixinVoteWorkComment(WeixinVoteWorkComment weixinVoteWorkComment);


    /**
     * 分页查询评论信息
     *
     * @param weixinVoteWorkComment
     * @param pageInfo
     * @return
     */
    PageInfo getWeixinVoteWorkCommentListByPage(WeixinVoteWorkComment weixinVoteWorkComment, PageInfo pageInfo);


    /**
     * 分页查询评论信息的相关内容 包括人员头像
     *
     * @param pageInfo
     * @return
     */
    PageInfo getWeixinVoteWorkCommentVOByPageInfo(PageInfo pageInfo);


    /**
     * 分页查询评论信息 通过查询
     *
     * @param weixinVoteWorkComment
     * @param pageInfo
     * @return
     */
    PageInfo getWeixinVoteWorkCommentVOByPageInfo(WeixinVoteWorkComment weixinVoteWorkComment, PageInfo pageInfo);





    /**
     * 查询最新的一条评论
     *
     * @param workId
     * @return
     */
    WeixinVoteWorkComment getLatestCommentByWorkId(Integer workId);

}
