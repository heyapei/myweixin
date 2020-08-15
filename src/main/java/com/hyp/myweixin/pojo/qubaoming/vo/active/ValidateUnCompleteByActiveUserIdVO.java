package com.hyp.myweixin.pojo.qubaoming.vo.active;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/15 15:49
 * @Description: TODO
 */
@Data
public class ValidateUnCompleteByActiveUserIdVO {

    @ApiModelProperty(value = "活动Id", name = "activeId", required = true)
    private Integer activeId;
    @ApiModelProperty(value = "用户Id", name = "userId", required = true)
    private Integer userId;
    @ApiModelProperty(value = "是否已有默认主办方信息", name = "hasCompanyInfo", required = true)
    private Boolean hasCompanyInfo;

}
