package com.hyp.myweixin.pojo.qubaoming.query.active;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/8/18 14:23
 * @Description: TODO
 */
@Data
public class ShowActiveByPageQuery {


    @ApiModelProperty(value = "分页查询页码数，默认第一页", name = "pageNum", required = false)
    private Integer pageNum = 1;

    @ApiModelProperty(value = "分页查询每页大小，默认每页5条", name = "pageSize", required = false)
    private Integer pageSize = 5;

    @ApiModelProperty(value = "时间戳", name = "nowTime", required = true)
    @NotNull(message = "时间戳必填")
    @Size(max = 13, min = 13, message = "时间戳必须为13位时间戳")
    private String nowTime;

    @ApiModelProperty(value = "请求密钥", name = "sign", required = true)
    @NotNull(message = "请求密钥必填")
    private String sign;


    @ApiModelProperty(value = "用户ID 允许不登陆 但是必传 传0标识未登录", name = "userId", required = true)
    @NotNull(message = "用户ID必传，0标识未登录")
    private Integer userId;


    @ApiModelProperty(value = "活动查询参数", name = "activeQuery", required = true)
    private String activeQuery;



}
