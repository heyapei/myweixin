package com.hyp.myweixin.pojo.qubaoming.vo.active;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/18 20:53
 * @Description: TODO
 */
@Data
public class AddActiveShareImgQuery {

    @ApiModelProperty(value = "活动分享图", name = "activeShareImg", required = true)
    @NotNull(message = "活动分享图必填")
    private String activeShareImg;

    @ApiModelProperty(value = "活动唯一标识", name = "activeId", required = true)
    @NotNull(message = "活动唯一标识必填")
    private Integer activeId;


    @ApiModelProperty(value = "时间戳", name = "nowTime", required = true)
    @NotNull(message = "时间戳必填")
    @Size(max = 13, min = 13, message = "时间戳必须为13位时间戳")
    private String nowTime;


    @ApiModelProperty(value = "请求密钥", name = "sign", required = true)
    @NotNull(message = "请求密钥必填")
    private String sign;
}
