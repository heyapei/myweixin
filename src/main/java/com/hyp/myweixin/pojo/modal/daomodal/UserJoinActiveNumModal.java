package com.hyp.myweixin.pojo.modal.daomodal;

import lombok.Data;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/7/27 12:02
 * @Description: TODO 用于数据查询的值接收 好像我又不咋需要了
 */
@Data
public class UserJoinActiveNumModal {

    /**
     * openId
     */
    private String openId;

    /**
     * 作品id
     */
    private Integer workId;
}
