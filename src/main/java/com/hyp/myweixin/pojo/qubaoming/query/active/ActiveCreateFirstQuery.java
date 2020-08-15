package com.hyp.myweixin.pojo.qubaoming.query.active;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/15 17:34
 * @Description: TODO
 */
@Data
public class ActiveCreateFirstQuery {

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", name = "userId", required = true)
    @NotNull(message = "用户必须先登录并授权")
    private Integer userId;

    @ApiModelProperty(value = "活动ID", name = "activeId", required = true)
    @NotNull(message = "必须先获取活动ID")
    private Integer activeId;


    @ApiModelProperty(value = "文字描述", name = "text", required = true)
    @NotNull(message = "文字描述不能为空")
    private String text;


    @ApiModelProperty(value = "图片信息", name = "img", required = false)
    private String img;


    @ApiModelProperty(value = "当前数据类型", name = "type", required = false)
    @NotNull(message = "数据类型必须指定")
    private String type;


}
