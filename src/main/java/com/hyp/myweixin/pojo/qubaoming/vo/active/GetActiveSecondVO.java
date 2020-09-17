package com.hyp.myweixin.pojo.qubaoming.vo.active;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/9/17 20:45
 * @Description: TODO
 */
@Data
public class GetActiveSecondVO {

    @ApiModelProperty(value = "活动ID", name = "activeId", required = true)
    private Integer activeId;

    @ApiModelProperty(value = "用户ID", name = "userId", required = true)
    private Integer userId;

    @ApiModelProperty(value = "活动类型 0 线下活动 1线上活动", name = "activeType", required = true)
    private Integer activeType;


    @ApiModelProperty(value = "活动地点", name = "activeAddress", required = true)
    private String activeAddress;

    @ApiModelProperty(value = "活动最大人数", name = "activeJoinNumMax", required = true)
    private Integer activeJoinNumMax;

    @ApiModelProperty(value = "获取必填项", name = "activeRequireOption", required = true)
    private String activeRequireOption;

    /**
     * 以下都是一些格式化好的时间数据 具体按照前缀来看是为谁的
     * Date是yyyy-MM-dd
     * Time是hh:MM
     */
    @ApiModelProperty(value = "活动开始日期", required = true)
    private String activeStartTimeDate;
    @ApiModelProperty(value = "活动开始时间", required = true)
    private String activeStartTimeDateTime;
    @ApiModelProperty(value = "活动结束日期", required = true)
    private String activeEndTimeDate;
    @ApiModelProperty(value = "活动结束时间", required = true)
    private String activeEndTimeDateTime;

    @ApiModelProperty(value = "活动报名开始日期", required = true)
    private String activeSignUpStartTimeDate;
    @ApiModelProperty(value = "活动报名开始时间", required = true)
    private String activeSignUpStartTimeDateTime;
    @ApiModelProperty(value = "活动报名结束日期", required = true)
    private String activeSignUpEndTimeDate;
    @ApiModelProperty(value = "活动报名结束时间", required = true)
    private String activeSignUpEndTimeDateTime;


}
