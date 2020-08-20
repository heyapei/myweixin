package com.hyp.myweixin.pojo.qubaoming.query.enroll;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/20 20:48
 * @Description: TODO
 */
@Data
public class UserSignUpActiveQuery {

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "活动ID", name = "activeId", required = true)
    private Integer activeId;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", name = "userId", required = true)
    private Integer userId;

    /**
     * 用户报名数据
     */
    @ApiModelProperty(value = "报名的信息", name = "signUpInfo", required = true)
    @NotNull(message = "报名必填信息不能为空")
    private String signUpInfo;

    @ApiModelProperty(value = "时间戳", name = "nowTime", required = true)
    @NotNull(message = "时间戳必填")
    @Size(max = 13, min = 13, message = "时间戳必须为13位时间戳")
    private String nowTime;


    @ApiModelProperty(value = "请求密钥", name = "sign", required = true)
    @NotNull(message = "请求密钥必填")
    private String sign;


}
