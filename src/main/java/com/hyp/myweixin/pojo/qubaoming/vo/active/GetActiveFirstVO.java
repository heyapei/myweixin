package com.hyp.myweixin.pojo.qubaoming.vo.active;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/9/17 20:27
 * @Description: TODO
 */
@Data
public class GetActiveFirstVO {

    @ApiModelProperty(value = "活动ID", name = "activeId", required = true)
    private Integer activeId;

    @ApiModelProperty(value = "用户ID", name = "userId", required = true)
    private Integer userId;

    @ApiModelProperty(value = "活动图片", name = "activeImgS", required = true)
    private String[] activeImgS;

    @ApiModelProperty(value = "活动名称", name = "activeName", required = true)
    private String activeName;

    @ApiModelProperty(value = "活动描述", name = "activeDesc", required = true)
    private String activeDesc;

    @ApiModelProperty(value = "活动描述图片", name = "activeDescImgS", required = true)
    private String[] activeDescImgS;

    @ApiModelProperty(value = "活动详情", name = "activeDetail", required = true)
    private String activeDetail;

    @ApiModelProperty(value = "活动详情图片", name = "activeDetailImgS", required = true)
    private String[] activeDetailImgS;


}
