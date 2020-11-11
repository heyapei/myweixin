package com.hyp.myweixin.pojo.qubaoming.query.enroll;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/20 20:48
 * @Description: TODO 查询报名人员头像信息的查询数据
 */
@Data
public class SignUpUserInfoListQuery {

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "活动ID", name = "activeId", required = true)
    @NotNull(message = "活动ID必填")
    private Integer activeId;


    @ApiModelProperty(value = "时间戳", name = "nowTime", required = true)
    @NotNull(message = "时间戳必填")
    @Size(max = 13, min = 13, message = "时间戳必须为13位时间戳")
    private String nowTime;


    @ApiModelProperty(value = "请求密钥", name = "sign", required = true)
    @NotNull(message = "请求密钥必填")
    private String sign;


    @ApiModelProperty(value = "分页查询页码数，默认第一页", name = "pageNum", required = false)
    private Integer pageNum = 1;

    @ApiModelProperty(value = "分页查询每页大小，默认每页5条", name = "pageSize", required = false)
    private Integer pageSize = 5;


}
