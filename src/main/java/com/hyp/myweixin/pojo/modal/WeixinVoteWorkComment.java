package com.hyp.myweixin.pojo.modal;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "weixin_vote_work_comment")
@Mapper
@Data
public class WeixinVoteWorkComment {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 用户ID
     */
    @Column(name = "vote_user_id")
    private Integer voteUserId;

    /**
     * 作品id
     */
    @Column(name = "vote_work_id")
    private Integer voteWorkId;

    /**
     * 作品评论
     */
    @Column(name = "work_comment")
    private String workComment;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime = new Date();

    /**
     * 是否展示 0展示 1不展示
     */
    @Column(name = "comment_status")
    private Integer commentStatus = 0;

    /**
     * 展示排序
     */
    @Column(name = "show_order")
    private Integer showOrder = 0;

    /**
     * 评论喜欢总数
     */
    @Column(name = "like_count")
    private Integer likeCount = 0;
    /**
     * 评论序号
     */
    @Column(name = "comment_or")
    private Integer commentOr;

}