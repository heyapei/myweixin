package com.hyp.myweixin.pojo.qubaoming.vo.pcenter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/31 14:31
 * @Description: TODO
 */
@Data
public class PCenterActiveVO {

    @ApiModelProperty(value = "活动ID", name = "activeId", required = true)
    private Integer activeId;
    @ApiModelProperty(value = "活动名称", name = "activeName", required = true)
    private String activeName;
    @ApiModelProperty(value = "活动地址", name = "activeAddress", required = true)
    private String activeAddress;
    @ApiModelProperty(value = "活动开始时间", name = "activeStartTime", required = true)
    private Long activeStartTime;
    @ApiModelProperty(value = "活动结束时间", name = "activeEndTime", required = true)
    private Long activeEndTime;
    @ApiModelProperty(value = "公司名称", name = "activeCompanyName", required = true)
    private String activeCompanyName;


}
