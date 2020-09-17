package com.hyp.myweixin.pojo.qubaoming.query.active;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/9/17 20:24
 * @Description: TODO
 */
@Data
public class GetActiveFirstQuery {

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", name = "userId", required = true)
    @NotNull(message = "用户必须先登录并授权")
    private Integer userId;

    @ApiModelProperty(value = "活动ID", name = "activeId", required = true)
    @NotNull(message = "必须先获取活动ID")
    private Integer activeId;

    @ApiModelProperty(value = "时间戳", name = "nowTime", required = true)
    @NotNull(message = "时间戳必填")
    @Size(max = 13, min = 13, message = "时间戳必须为13位时间戳")
    private String nowTime;


    @ApiModelProperty(value = "请求密钥", name = "sign", required = true)
    @NotNull(message = "请求密钥必填")
    private String sign;
}
