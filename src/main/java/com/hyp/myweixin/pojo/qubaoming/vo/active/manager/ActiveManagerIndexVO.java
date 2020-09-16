package com.hyp.myweixin.pojo.qubaoming.vo.active.manager;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/22 10:32
 * @Description: TODO
 */
@Data
public class ActiveManagerIndexVO {
    @ApiModelProperty(value = "活动Id", name = "activeId", required = true)
    private Integer activeId;
    @ApiModelProperty(value = "活动名称", name = "activeName", required = true)
    private String activeName;
    @ApiModelProperty(value = "创建时间", name = "createTime", required = true)
    private String createTime;
    @ApiModelProperty(value = "活动状态", name = "activeStatus", required = true)
    private String activeStatus;
    @ApiModelProperty(value = "活动图片", name = "activeImgList", required = true)
    private String[] activeImgList;
    @ApiModelProperty(value = "查看次数", name = "activeViewNum", required = true)
    private Integer activeViewNum;
}
