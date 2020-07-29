package com.hyp.myweixin.pojo.vo.page.activeeditor;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/21 19:47
 * @Description: TODO
 */
@Data
public class ActiveEditThirdVO {

    @ApiModelProperty(value = "活动ID", name = "activeId", required = true)
    private Integer activeId;


    @ApiModelProperty(value = "活动开始时间", name = "activeStartTime", required = true)
    private Date activeStartTime;

    @ApiModelProperty(value = "活动结束时间", name = "activeEndTime", required = true)
    private Date activeEndTime;

    /**
     * 是否可以重复投票
     * 是否可以重复投票 0默认不开启 1开启
     */
    private Integer activeConfRepeatVote;
    /**
     * 重复投票次数
     * 值1,值2 值1：1按照每天多少票 2按照总共多少票 值2：票数量
     */
    private String activeConfVoteType;
    /**
     * 是否开启在线报名
     * 是否开启用户自己上传作品 0 开启 1 不开启 默认不开启
     */
    private Integer activeConfSignUp;
    /**
     * 允许报名开始时间
     */
    private Date activeUploadStartTime;
    /**
     * 允许报名结束时间
     */
    private Date activeUploadEndTime;
    /**
     * 是否需要微信号（0 默认不需要 1 需要）
     */
    private Integer activeConfNeedWeixin;
    /**
     * 是否需要手机号（0 默认不需要 1 需要）
     */
    private Integer activeConfNeedPhone;


    /**
     * 以下都是一些格式化好的时间数据 具体按照前缀来看是为谁的
     * Date是yyyy-MM-dd
     * Time是hh:MM
     */
    private String activeStartTimeDate;
    private String activeStartTimeDateTime;

    private String activeEndTimeDate;
    private String activeEndTimeDateTime;


    private String activeUploadStartTimeDate;
    private String activeUploadStartTimeDateTime;

    private String activeUploadEndTimeDate;
    private String activeUploadEndTimeDateTime;


}
