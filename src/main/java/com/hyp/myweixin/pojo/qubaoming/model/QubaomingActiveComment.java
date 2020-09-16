package com.hyp.myweixin.pojo.qubaoming.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "qubaoming_active_comment")
public class QubaomingActiveComment {

    public static QubaomingActiveComment init() {
        QubaomingActiveComment qubaomingActiveComment = new QubaomingActiveComment();
        qubaomingActiveComment.setId(0);
        qubaomingActiveComment.setUserId(0);
        qubaomingActiveComment.setActiveId(0);
        qubaomingActiveComment.setCommentContent("");
        qubaomingActiveComment.setCreateTime(System.currentTimeMillis());
        qubaomingActiveComment.setUpdateTime(System.currentTimeMillis());
        qubaomingActiveComment.setStatus(0);
        qubaomingActiveComment.setExt1("");
        qubaomingActiveComment.setExt2("");
        qubaomingActiveComment.setExt3("");
        qubaomingActiveComment.setExt4("");
        qubaomingActiveComment.setExt5("");

        return qubaomingActiveComment;

    }


    @Id
    private Integer id;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 活动ID
     */
    @Column(name = "active_id")
    private Integer activeId;

    /**
     * 评论内容
     */
    @Column(name = "comment_content")
    private String commentContent;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Long createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Long updateTime;

    /**
     * 评论状态
     */
    private Integer status;

    /**
     * 备用字段
     */
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;

}