package com.hyp.myweixin.pojo.vo.page.activeeditor;

import lombok.Data;

import java.util.Date;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/29 22:33
 * @Description: TODO 分享活动的ID
 */
@Data
public class ShareActiveVO {

    /**
     * 活动ID
     */
    private Integer activeId;

    /**
     * 活动名称
     */
    private String activeName;

    /**
     * 活动分享图
     */
    private String activeShareImg;

    /**
     * 活动主页图
     */
    private String activeImg;

    /**
     * 活动描述
     */
    private String activeDesc;

    /**
     * 活动结束时间
     */
    private Date activeEndTime;

    /**
     * 活动结束时间 格式化 yyyy-MM-dd HH:mm
     */
    private String  activeEndTimeFormat;

    /**
     * 微信分享图
     */
    private String weixinShareQrCode;




}
