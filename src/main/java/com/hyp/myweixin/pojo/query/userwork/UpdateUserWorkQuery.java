package com.hyp.myweixin.pojo.query.userwork;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/2 18:05
 * @Description: TODO
 */
@Data
public class UpdateUserWorkQuery {
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", name = "userId", required = true)
    @NotNull(message = "用户必须为登录状态")
    private Integer userId;
    /**
     * 所属活动ID
     */
    @ApiModelProperty(value = "作品ID", name = "userWorkId", required = true)
    @NotNull(message = "作品指定不明确")
    private Integer userWorkId;
    /**
     * 作品用户名
     */
    @ApiModelProperty(value = "作品用户名", name = "voteWorkUserName")
    private String voteWorkUserName = "";
    /**
     * 作品名称
     */
    @ApiModelProperty(value = "作品名称", name = "voteWorkName", required = true)
    @NotNull(message = "作品名称")
    private String voteWorkName;
    /**
     * 作品描述
     */
    @ApiModelProperty(value = "作品描述", name = "voteWorkDesc", required = true)
    @NotNull(message = "作品描述")
    private String voteWorkDesc;
    /**
     * 用户手机号
     */
    @ApiModelProperty(value = "用户手机号", name = "userPhone")
    private String userPhone = "";

    /**
     * 用户微信
     */
    @ApiModelProperty(value = "用户微信", name = "userWeixin")
    private String userWeixin = "";
    /**
     * 参赛作品图片
     */
    @ApiModelProperty(value = "参赛作品图片", name = "voteWorkImgS", required = true)
    @NotNull(message = "作品图片")
    private String voteWorkImgS;
}
