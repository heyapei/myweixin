package com.hyp.myweixin.pojo.query.activeedit;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/21 19:47
 * @Description: TODO
 */
@Data
public class ActiveEditFourthQuery {

    @NotNull(message = "用户ID不可以为空")
    private Integer userId;

    @NotNull(message = "活动ID不可以为空")
    private Integer activeId;

    /**
     * 性别限制分别为男 女 无
     */
    private String sex = "无";


}
