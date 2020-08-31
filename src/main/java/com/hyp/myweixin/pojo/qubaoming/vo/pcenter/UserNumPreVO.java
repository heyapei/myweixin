package com.hyp.myweixin.pojo.qubaoming.vo.pcenter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/31 15:52
 * @Description: TODO
 */
@Data
public class UserNumPreVO {

    public static UserNumPreVO init() {
        UserNumPreVO userNumPreVO = new UserNumPreVO();
        userNumPreVO.setUserId(0);
        userNumPreVO.setCompanyEnrollNum(0);
        userNumPreVO.setActiveCreateNum(0);
        userNumPreVO.setActiveCollectionNum(0);
        userNumPreVO.setActiveSignUpNum(0);
        return userNumPreVO;
    }



    @ApiModelProperty(value = "用户ID", name = "userId", required = true)
    private Integer userId;

    @ApiModelProperty(value = "公司收藏数量", name = "companyEnrollNum", required = true)
    private Integer companyEnrollNum;

    @ApiModelProperty(value = "活动创建数量", name = "activeCreateNum", required = true)
    private Integer activeCreateNum;

    @ApiModelProperty(value = "活动收藏数量", name = "activeCollectionNum", required = true)
    private Integer activeCollectionNum;

    @ApiModelProperty(value = "活动创建数量", name = "activeSignUpNum", required = true)
    private Integer activeSignUpNum;

}
