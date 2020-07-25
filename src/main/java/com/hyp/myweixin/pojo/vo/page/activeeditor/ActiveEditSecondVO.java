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
public class ActiveEditSecondVO {

    @ApiModelProperty(value = "是否展示再首页，默认值为0", name = "showPublic", required = true)
    private Integer showPublic;
    @ApiModelProperty(value = "活动ID", name = "activeId", required = true)
    private Integer activeId;
    @ApiModelProperty(value = "活动分享图", name = "activeShareImg", required = true)
    private String activeShareImg;
    @ApiModelProperty(value = "是否有主办方信息，默认值为0", name = "hasOrganisers", required = true)
    private Integer hasOrganisers;
    @ApiModelProperty(value = "活动描述")
    private String organisersName;
    @ApiModelProperty(value = "主办方手机号")
    private String organisersPhone;
    @ApiModelProperty(value = "主办方微信二维码图片信息")
    private String organisersWeixinCode;
    @ApiModelProperty(value = "主办方logon图片信息")
    private String organisersLogo;


}
