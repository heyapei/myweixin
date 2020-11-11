package com.hyp.myweixin.pojo.qubaoming.query.company;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/15 16:41
 * @Description: TODO
 */
@Data
public class CompanyUpdateEmailQuery {


    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户唯一标识", name = "userId", required = true)
    @NotNull(message = "用户必须先登录并授权")
    private Integer userId;
    /**
     * 公司唯一标识
     */
    @ApiModelProperty(value = "公司唯一标识", name = "companyId", required = true)
    @NotNull(message = "必须指定公司唯一标识")
    private Integer companyId;

    /**
     * 邮箱地址
     */
    @ApiModelProperty(value = "公司邮箱地址", name = "companyEmail", required = true)
    @NotNull(message = "Email地址不能为空")
    @Email(message = "Email地址无效")
    private String companyEmail;

    /**
     * 请求时间戳
     */
    @ApiModelProperty(value = "时间戳", name = "nowTime", required = true)
    @NotNull(message = "时间戳必填")
    @Size(max = 13, min = 13, message = "时间戳必须为13位时间戳")
    private String nowTime;
    /**
     * 请求密钥
     */
    @ApiModelProperty(value = "请求密钥", name = "sign", required = true)
    @NotNull(message = "请求密钥必填")
    private String sign;


}
