package com.hyp.myweixin.pojo.vo.page.activeeditor;

import lombok.Data;

import java.util.Date;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/23 21:51
 * @Description: TODO 用户上传是否允许
 */
@Data
public class UserUploadRightVO {

    /**
     * 是否允许上传最后判断结果
     */
    private Boolean allowUploadResult;
    /**
     * 配置文件中是否可以上传
     */
    private Boolean allowUploadConf;
    /**
     * 是否已经上传了
     */
    private Boolean hasUploaded;

    /**
     * 允许上传开始时间
     */
    private Date allowUploadStartTimeConf;
    /**
     * 允许上传结束时间
     */
    private Date allowUploadEndTimeConf;
    /**
     * 活动开始时间
     */
    private Date activeStartTime;
    /**
     * 活动结束时间
     */
    private Date activeEndTime;

    /**
     * 是否需要微信号
     */
    private Boolean needUserWeixin;
    /**
     * 是否需要手机号
     */
    private Boolean needUserPhone;

    /**
     * 描述信息
     */
    private String message;


}
