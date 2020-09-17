package com.hyp.myweixin.pojo.qubaoming.vo.active;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/9/17 21:10
 * @Description: TODO
 */
@Data
public class GetActiveThirdVO {

    @ApiModelProperty(value = "活动ID", name = "activeId", required = true)
    private Integer activeId;

    @ApiModelProperty(value = "用户ID", name = "userId", required = true)
    private Integer userId;

    @ApiModelProperty(value = "活动分享图片", name = "activeShareImgS", required = true)
    private String[] activeShareImgS;

    @ApiModelProperty(value = "公司ID", name = "companyId", required = true)
    private Integer companyId;


}
