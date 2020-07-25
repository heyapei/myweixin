package com.hyp.myweixin.pojo.vo.page.activeeditor;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/21 19:47
 * @Description: TODO
 */
@Data
public class ActiveEditFourthVO {


    @NotNull(message = "活动ID不可以为空")
    private Integer activeId;

    /**
     * 性别限制分别为男 女 无
     */
    private String sex;


}
