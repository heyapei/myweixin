package com.hyp.myweixin.pojo.qubaoming.query.pcenter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/31 15:51
 * @Description: TODO
 */
@Data
public class UserNumPreQuery {

    @ApiModelProperty(value = "用户ID 允许不登陆", name = "userId", required = true)
    private Integer userId = 0;

    @ApiModelProperty(value = "时间戳", name = "nowTime", required = true)
    @NotNull(message = "时间戳必填")
    @Size(max = 13, min = 13, message = "时间戳必须为13位时间戳")
    private String nowTime;

    @ApiModelProperty(value = "请求密钥", name = "sign", required = true)
    @NotNull(message = "请求密钥必填")
    private String sign;


}
