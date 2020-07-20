package com.hyp.myweixin.pojo.query.voteuserwork;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/20 21:56
 * @Description: TODO
 */
@Data
public class UpdateUserWorkStatusQuery {
    /**
     * 当前活动的ID
     */
    @NotNull
    private Integer userWorkId;
    /**
     * 作品的类型 如果是-1就不按照状态进行查询
     */
    @NotNull
    private Integer workStatus;
    /**
     * 当前用户的ID
     */
    @NotNull
    private Integer userId;
}
