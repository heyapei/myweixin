package com.hyp.myweixin.pojo.qubaoming.query.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/22 13:15
 * @Description: TODO
 */
@Data
public class AddActiveCommentQuery {

    @ApiModelProperty(value = "用户ID 允许不登陆 但是必传 传0标识未登录", name = "userId", required = true)
    @NotNull(message = "用户ID必传")
    private Integer userId;


    @ApiModelProperty(value = "活动ID", name = "activeId", required = true)
    @NotNull(message = "必须指定活动ID")
    private Integer activeId;

    @ApiModelProperty(value = "评论内容", name = "commentContent", required = true)
    @NotNull(message = "评论内容不能为空")
    @Size(min = 1, message = "评论内容必填")
    private String commentContent;

    @ApiModelProperty(value = "时间戳", name = "nowTime", required = true)
    @NotNull(message = "时间戳必填")
    @Size(max = 13, min = 13, message = "时间戳必须为13位时间戳")
    private String nowTime;

    @ApiModelProperty(value = "请求密钥", name = "sign", required = true)
    @NotNull(message = "请求密钥必填")
    private String sign;


}
