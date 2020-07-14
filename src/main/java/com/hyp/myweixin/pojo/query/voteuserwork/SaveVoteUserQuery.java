package com.hyp.myweixin.pojo.query.voteuserwork;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/14 19:01
 * @Description: TODO
 */
@ApiModel(value = "保存用户作品用", description = "该对象用户保存用户上传回来的具体作品信息")
@Data
public class SaveVoteUserQuery {
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", name = "userId", required = true)
    @NotNull
    private Integer userId;
    /**
     * 所属活动ID
     */
    @ApiModelProperty(value = "活动ID", name = "activeId", required = true)
    @NotNull
    private Integer activeId;
    /**
     * 作品用户名
     */
    @ApiModelProperty(value = "作品用户名", name = "voteWorkUserName")
    private String voteWorkUserName = "";
    /**
     * 作品名称
     */
    @ApiModelProperty(value = "作品名称", name = "voteWorkName", required = true)
    @NotNull
    private String voteWorkName;
    /**
     * 作品描述
     */
    @ApiModelProperty(value = "作品描述", name = "voteWorkDesc", required = true)
    @NotNull
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
    @NotNull
    private String voteWorkImgS;


}
