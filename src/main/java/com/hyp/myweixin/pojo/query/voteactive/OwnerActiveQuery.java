package com.hyp.myweixin.pojo.query.voteactive;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/26 17:16
 * @Description: TODO
 */
@Data
public class OwnerActiveQuery {

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", name = "userId", required = true)
    @NotNull
    private Integer userId;

    /**
     * 每页显示大小
     */
    @ApiModelProperty(value = "每页显示大小", name = "pageSize", required = true)
    private Integer pageSize = 5;
    /**
     * 页码
     */
    @ApiModelProperty(value = "页码", name = "pageNum", required = true)
    private Integer pageNum = 1;


    /**
     * 状态值 默认为-1 不参与查询操作
     */
    @ApiModelProperty(value = "活动状态值", name = "activeStatus", required = true)
    private Integer activeStatus = null;

    /**
     * 时间戳
     *//*
    @ApiModelProperty(value = "时间戳", name = "nowTime", required = true)
    @NotNull
    private Long nowTime;

    *//**
     * 密钥
     *//*
    @ApiModelProperty(value = "密钥", name = "sign", required = true)
    @NotNull
    private Long sign;*/


}
