package com.hyp.myweixin.pojo.vo.page.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/27 22:04
 * @Description: TODO 返回详细的用户补充信息视图
 */
@Data
public class UserInfoSupplyDetailVO {

    /**
     * 主键
     */
    @ApiModelProperty(name = "主键")
    private Integer id;

    /**
     * 用户ID 对应weixinvoteuser的id
     */
    @ApiModelProperty(name = "用户ID")
    private Integer userId;

    /**
     * 真实姓名
     */
    @ApiModelProperty(name = "真实姓名")
    private String realName;

    /**
     * 收件地址
     */
    @ApiModelProperty(name = "收件地址")
    private String address;

    /**
     * 邮件地址
     */
    @ApiModelProperty(name = "邮件地址")
    private String email;

    /**
     * 微信号
     */
    @ApiModelProperty(name = "微信号")
    private String wechatNumber;

    /**
     * 生日 采用时间戳格式
     */
    @ApiModelProperty(name = "生日")
    private Long birthday;

    /**
     * 真实的手机号
     */
    @ApiModelProperty(name = "手机号")
    private String phone;

    /**
     * 性别 1男 2女 0未知 默认未知
     */
    @ApiModelProperty(name = "性别 1男 2女 0未知 默认未知")
    private Integer gender;

    /**
     * 信息更新时间
     */
    @ApiModelProperty(name = "更新时间")
    private Long updateDate;
}


//~ Formatted by Jindent --- http://www.jindent.com
