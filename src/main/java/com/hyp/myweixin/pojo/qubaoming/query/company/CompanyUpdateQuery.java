package com.hyp.myweixin.pojo.qubaoming.query.company;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/16 18:10
 * @Description: TODO
 */
@Data
public class CompanyUpdateQuery {


    @ApiModelProperty(value = "公司主体唯一标识", name = "wechatCompanyId", required = true)
    @NotNull(message = "必须指定需要查看的公司主体唯一标识")
    private Integer wechatCompanyId;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户唯一标识", name = "userId", required = true)
    @NotNull(message = "用户必须先登录并授权")
    private Integer userId;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", name = "companyName", required = true)
    @NotNull(message = "公司名称不能为空")
    private String companyName;

    /**
     * 公司名称（充当公司简介）
     */
    @ApiModelProperty(value = "公司名称0--充当公司简介", name = "company", required = false)
    private String company;

    /**
     * logo图片
     */
    @ApiModelProperty(value = "logo图片", name = "logoImg", required = false)
    private String logoImg;

    /**
     * 描述
     */
    @ApiModelProperty(value = "联系电话", name = "phone", required = false)
    private String organisersDesc;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话", name = "phone", required = false)
    private String phone;

    /**
     * 公司地址
     */
    @ApiModelProperty(value = "公司地址", name = "address", required = false)
    private String address;


    /**
     * 公司类型
     */
    @ApiModelProperty(value = "公司类型", name = "type", required = false)
    private String type;

    /**
     * 公司主营业务
     */
    @ApiModelProperty(value = "公司主营业务", name = "jobMajor", required = false)
    private String jobMajor;

    /**
     * 公司创建时间
     */
    @ApiModelProperty(value = "公司创建时间", name = "buildTime", required = false)
    private Long buildTime;

    /**
     * 法人名称
     */
    @ApiModelProperty(value = "法人名称", name = "corporate", required = false)
    private String corporate;

    /**
     * 公司二维码路径
     */
    @ApiModelProperty(value = "公司二维码路径", name = "weixinQrCode", required = false)
    private String weixinQrCode;

    /**
     * 备用字段
     */
    @ApiModelProperty(value = "备用字段", name = "ext1", required = false)
    private String ext1;

    /**
     * 备用字段
     */
    @ApiModelProperty(value = "备用字段", name = "ext2", required = false)
    private String ext2;

    /**
     * 备用字段
     */
    @ApiModelProperty(value = "备用字段", name = "ext3", required = false)
    private String ext3;

    /**
     * 备用字段
     */
    @ApiModelProperty(value = "备用字段", name = "ext4", required = false)
    private String ext4;

    /**
     * 备用字段
     */
    @ApiModelProperty(value = "备用字段", name = "ext5", required = false)
    private String ext5;


    @ApiModelProperty(value = "时间戳", name = "nowTime", required = true)
    @NotNull(message = "时间戳必填")
    @Size(max = 13, min = 13, message = "时间戳必须为13位时间戳")
    private String nowTime;


    @ApiModelProperty(value = "请求密钥", name = "sign", required = true)
    @NotNull(message = "请求密钥必填")
    private String sign;

}
