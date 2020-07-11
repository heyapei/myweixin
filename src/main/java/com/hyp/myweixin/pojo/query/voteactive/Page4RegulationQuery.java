package com.hyp.myweixin.pojo.query.voteactive;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/8 18:51
 * @Description: TODO
 */
@Data
public class Page4RegulationQuery {


    @NotNull(message = "用户ID不可以为空")
    private Integer userId;

    @NotNull(message = "活动ID不可以为空")
    private Integer voteWorkId;

    /**性别限制分别为男 女 无*/
    private String sex = "无";


}
