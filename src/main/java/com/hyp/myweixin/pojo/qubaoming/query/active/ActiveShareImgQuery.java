package com.hyp.myweixin.pojo.qubaoming.query.active;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/31 13:48
 * @Description: TODO
 */
@Data
public class ActiveShareImgQuery {

    @ApiModelProperty(value = "场景", name = "scene", required = true)
    @NotNull(message = "必须指定场景")
    private String scene;

    @ApiModelProperty(value = "所要到达的页面", name = "page", required = true)
    private String page;

    @ApiModelProperty(value = "活动ID", name = "activeId", required = true)
    @NotNull(message = "必须指定活动ID")
    private Integer activeId;

    @ApiModelProperty(value = "时间戳", name = "nowTime", required = true)
    @NotNull(message = "时间戳必填")
    @Size(max = 13, min = 13, message = "时间戳必须为13位时间戳")
    private String nowTime;

    @ApiModelProperty(value = "请求密钥", name = "sign", required = true)
    @NotNull(message = "请求密钥必填")
    private String sign;
}
