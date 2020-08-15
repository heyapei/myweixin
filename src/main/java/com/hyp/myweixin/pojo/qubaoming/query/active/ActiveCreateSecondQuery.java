package com.hyp.myweixin.pojo.qubaoming.query.active;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/15 18:44
 * @Description: TODO
 */
@Data
public class ActiveCreateSecondQuery {


    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", name = "userId", required = true)
    @NotNull(message = "用户必须先登录并授权")
    private Integer userId;

    @ApiModelProperty(value = "活动ID", name = "activeId", required = true)
    @NotNull(message = "必须先获取活动ID")
    private Integer activeId;


    /**
     * 活动地点
     */
    @ApiModelProperty(value = "活动地点", name = "activeAddress", required = true)
    @NotNull(message = "必须指定活动地点")
    private String activeAddress;

    /**
     * 活动开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @ApiModelProperty(value = "活动开始时间", name = "activeStartTime", required = true)
    @NotNull(message = "活动开始时间必填")
    private Long activeStartTime;

    /**
     * 活动结束时间
     */
    @ApiModelProperty(value = "活动结束时间", name = "activeEndTime", required = true)
    @NotNull(message = "活动结束时间必填")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Long activeEndTime;

    /**
     * 开始报名时间
     */
    @ApiModelProperty(value = "开始报名时间", name = "signUpStartTime", required = true)
    @NotNull(message = "开始报名时间必填")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Long signUpStartTime;

    /**
     * 结束报名时间
     */
    @ApiModelProperty(value = "结束报名时间", name = "signUpEndTime", required = true)
    @NotNull(message = "结束报名时间必填")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Long signUpEndTime;


    /**
     * 报名必选项 1姓名 2年纪 3电话 4性别
     */
    @ApiModelProperty(value = "报名必选项 1姓名 2年纪 3电话 4性别，使用';'分割", name = "activeRequireOption", required = true)
    @NotNull(message = "报名必选项必填")
    private String activeRequireOption;


    /**
     * 报名人数最大值 0 不限制 其他值为最大限制值
     */
    @ApiModelProperty(value = "报名人数最大值 0不限制，其他值为最大限制值", name = "activeJoinNumMax", required = true)
    private Integer activeJoinNumMax;

}
