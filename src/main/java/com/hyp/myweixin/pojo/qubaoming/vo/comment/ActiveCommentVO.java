package com.hyp.myweixin.pojo.qubaoming.vo.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/22 22:18
 * @Description: TODO
 */
@Data
public class ActiveCommentVO {
    /**
     * 评论内容
     */
    @ApiModelProperty(value = "头像", name = "avatar", required = true)
    private String avatar;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "用户名称", name = "userName", required = true)
    private String userName;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容", name = "commentContent", required = true)
    private String commentContent;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "createTime", required = true)
    private Long createTime;

}
