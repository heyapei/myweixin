package com.hyp.myweixin.pojo.qubaoming.query.active;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/31 15:10
 * @Description: TODO
 */
@Data
public class ActiveShowByCompanyIdQuery {

    @ApiModelProperty(value = "分页查询页码数，默认第一页", name = "pageNum", required = false)
    private Integer pageNum = 1;

    @ApiModelProperty(value = "分页查询每页大小，默认每页5条", name = "pageSize", required = false)
    private Integer pageSize = 5;

    @ApiModelProperty(value = "时间戳", name = "nowTime", required = true)
    @NotNull(message = "时间戳必填")
    @Size(max = 13, min = 13, message = "时间戳必须为13位时间戳")
    private String nowTime;

    @ApiModelProperty(value = "请求密钥", name = "sign", required = true)
    @NotNull(message = "请求密钥必填")
    private String sign;


    @ApiModelProperty(value = "公司主体唯一标识", name = "wechatCompanyId", required = true)
    @NotNull(message = "必须指定需要查看的公司主体唯一标识")
    private Integer wechatCompanyId;


}
