package com.hyp.myweixin.pojo.vo.page.activeeditor;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/21 19:47
 * @Description: TODO
 */
@Data
public class ActiveEditFirstVO {

    @ApiModelProperty(value = "用户ID", name = "userId", required = true)
    private Integer userId;
    @ApiModelProperty(value = "活动ID", name = "activeId", required = true)
    private Integer activeId;
    @ApiModelProperty(value = "活动图片信息")
    private String activeImg;
    @ApiModelProperty(value = "活动标题")
    private String activeName;
    @ApiModelProperty(value = "活动描述")
    private String activeDesc;
    @ApiModelProperty(value = "活动描述图片")
    private String[] activeDescImgS;
    @ApiModelProperty(value = "活动奖励")
    private String activeReward;
    @ApiModelProperty(value = "活动奖励图片")
    private String[] activeRewardImgS;


}
