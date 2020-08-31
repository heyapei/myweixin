package com.hyp.myweixin.pojo.qubaoming.vo.active;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/31 19:42
 * @Description: TODO
 */
@Data
public class ActiveShareImgVO {

    @ApiModelProperty(value = "活动名称", name = "activeName", required = true)
    private String activeName;

    @ApiModelProperty(value = "活动介绍", name = "activeDesc", required = true)
    private String activeDesc;

    @ApiModelProperty(value = "活动图片", name = "activeImg", required = true)
    private String activeImg;

    @ApiModelProperty(value = "活动微信二维码", name = "activeAddress", required = true)
    private String activeWechatImg;

    @ApiModelProperty(value = "活动结束时间", name = "activeEndTime", required = true)
    private String activeEndTime;

}
