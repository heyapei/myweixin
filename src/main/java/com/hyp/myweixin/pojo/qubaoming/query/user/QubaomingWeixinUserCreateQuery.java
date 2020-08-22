package com.hyp.myweixin.pojo.qubaoming.query.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/15 23:22
 * @Description: TODO
 */

@Data
public class QubaomingWeixinUserCreateQuery {


    /**
     * 用户微信中的唯一标识
     */
    @ApiModelProperty(value = "用户微信中的唯一标识", name = "openId", required = true)
    @NotNull(message = "用户微信中的唯一标识必填")
    @Size(min = 1, message = "评论内容必填")
    private String openId;

    /**
     * 头像地址
     */
    @ApiModelProperty(value = "头像地址", name = "avatarUrl", required = true)
    @NotNull(message = "头像地址必填")
    private String avatarUrl;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称", name = "nickName", required = true)
    @NotNull(message = "昵称必填")
    private String nickName;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别", name = "gender", required = true)
    @NotNull(message = "性别必填")
    private String gender;

    /**
     * 城市
     */
    @ApiModelProperty(value = "城市", name = "city", required = true)
    @NotNull(message = "城市必填")
    private String city;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份", name = "province", required = true)
    @NotNull(message = "省份必填")
    private String province;

    /**
     * 城市
     */
    @ApiModelProperty(value = "城市", name = "country", required = true)
    @NotNull(message = "城市必填")
    private String country;

    @ApiModelProperty(value = "时间戳", name = "nowTime", required = true)
    @NotNull(message = "时间戳必填")
    @Size(max = 13, min = 13, message = "时间戳必须为13位时间戳")
    private String nowTime;


    @ApiModelProperty(value = "请求密钥", name = "sign", required = true)
    @NotNull(message = "请求密钥必填")
    private String sign;

}
