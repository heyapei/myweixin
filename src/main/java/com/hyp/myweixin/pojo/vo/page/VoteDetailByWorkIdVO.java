package com.hyp.myweixin.pojo.vo.page;

import lombok.Data;

import java.util.Date;


/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/10 19:25
 * @Description: TODO
 */
@Data
public class VoteDetailByWorkIdVO {
    private String activeImg;
    private String activeName;
    private Integer activeJoinCount;
    private Integer activeVoteCount;
    private Integer activeViewCount;
    private Date activeStartTime;
    private Date activeEndTime;
    private String organisersName;
    private String organisersLogoImg;
    private String activeMusic;
    private String activeBgImg;
    private String organisersWeixinQrCode;

    private String organisersPhone;

    /**
     * 创建人id
     */
    private Integer activeCreateUserId;
    /**
     * 是否开启用户自己上传作品 0 默认开启 1 不开启
     */
    private Integer activeConfSignUp;

    /**
     * 创建人的id
     */
    private Integer systemUserId;


    /**
     * 允许上传开始的时间
     */
    private Date activeUploadStartTime;


    /**
     * 允许上传结束的时间
     */
    private Date activeUploadEndTime;

}
