package com.hyp.myweixin.pojo.qubaoming.query.message;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/10/15 19:21
 * @Description: TODO
 */
@Data
public class UserSubmitMessageQuery {

    @ApiModelProperty(value = "时间戳", name = "nowTime", required = true)
    @NotNull(message = "时间戳必填")
    @Size(max = 13, min = 13, message = "时间戳必须为13位时间戳")
    private String nowTime;

    @ApiModelProperty(value = "请求密钥", name = "sign", required = true)
    @NotNull(message = "请求密钥必填")
    private String sign;
    
    @ApiModelProperty(value = "用户ID", name = "userId", required = true)
    @NotNull(message = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "活动ID", name = "activeId", required = true)
    @NotNull(message = "活动ID")
    private Integer activeId;


    @ApiModelProperty(value = "消息模板ID", name = "messageId", required = true)
    @NotNull(message = "消息模板ID")
    private String messageId;


}
