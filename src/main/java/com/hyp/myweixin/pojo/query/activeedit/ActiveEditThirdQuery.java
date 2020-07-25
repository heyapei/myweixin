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
public class ActiveEditThirdQuery {

    @NotNull(message = "用户ID不可以为空")
    private Integer userId;

    @NotNull(message = "活动ID不可以为空")
    private Integer activeId;

    /**
     * 活动开始时间
     */
    @NotNull(message = "活动开始时间必填")
    private String activeStartTime;
    /**
     * 活动结束时间
     */
    @NotNull(message = "活动结束时间必填")
    private String activeEndTime;
    /**
     * 是否可以重复投票
     * 是否可以重复投票 0默认不开启 1开启
     */

    private Integer activeConfRepeatVote = 0;
    /**
     * 重复投票次数
     * 值1,值2 值1：1按照每天多少票 2按照总共多少票 值2：票数量
     */
    private String activeConfVoteType = "1;1";
    /**
     * 是否开启在线报名
     * 是否开启用户自己上传作品 0 开启 1 不开启 默认不开启
     */
    private Integer activeConfSignUp = 1;

    /**
     * 允许报名开始时间
     */
    //@NotNull(message = "允许报名开始时间必填")
    private String activeUploadStartTime;
    /**
     * 允许报名结束时间
     */
    //@NotNull(message = "允许报名结束时间必填")
    private String activeUploadEndTime;

    /**
     * 是否需要微信号（0 默认不需要 1 需要）
     */
    private Integer activeConfNeedWeixin = 0;
    /**
     * 是否需要手机号（0 默认不需要 1 需要）
     */
    private Integer activeConfNeedPhone = 0;


}
