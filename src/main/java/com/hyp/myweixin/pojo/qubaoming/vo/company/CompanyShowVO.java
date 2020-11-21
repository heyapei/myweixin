package com.hyp.myweixin.pojo.qubaoming.vo.company;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/16 16:54
 * @Description: TODO
 */
@Data
public class CompanyShowVO {

    /**
     * 主键
     */
    @ApiModelProperty(value = "唯一标识")
    private Integer id;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String companyName;

    /**
     * logo图片
     */
    @ApiModelProperty(value = "logo图片")
    private String logoImg;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String organisersDesc;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String phone;

    /**
     * 公司地址
     */
    @ApiModelProperty(value = "公司地址")
    private String address;

    /**
     * 公司名称（充当公司简介）
     */
    @ApiModelProperty(value = "公司简介")
    private String company;

    /**
     * 公司类型
     */
    @ApiModelProperty(value = "公司类型")
    private String type;

    /**
     * 公司主营业务
     */
    @ApiModelProperty(value = "公司主营业务")
    private String jobMajor;

    /**
     * 公司创建时间
     */
    @ApiModelProperty(value = "公司创建时间")
    private Long buildTime;

    /**
     * 法人名称
     */
    @ApiModelProperty(value = "法人名称")
    private String corporate;

    /**
     * 公司二维码路径
     */
    @ApiModelProperty(value = "公司二维码路径")
    private String weixinQrCode;

    /**
     * 分享次数
     */
    @ApiModelProperty(value = "分享次数")
    private Integer companyShareNum;

    /**
     * 查看次数
     */
    @ApiModelProperty(value = "查看次数")
    private Integer companyViewNum;

    /**
     * 收藏次数
     */
    @ApiModelProperty(value = "收藏次数")
    private Integer companyCollectionNum;

    /**
     * 使用次数
     */
    @ApiModelProperty(value = "使用次数")
    private Integer companyUsedNum;

    @ApiModelProperty(value = "默认使用的公司主体内容 多个中最大值为默认的公司选项")
    private Integer companyShowOrder;


    @ApiModelProperty(value = "公司邮箱")
    private String[] companyEmails;

}
