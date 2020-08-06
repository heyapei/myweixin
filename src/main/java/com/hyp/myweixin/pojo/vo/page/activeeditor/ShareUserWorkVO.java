package com.hyp.myweixin.pojo.vo.page.activeeditor;

import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/29 22:33
 * @Description: TODO 分享作品
 * <p>
 * 1.参与的活动名称
 * 2.选手上传的第一张作品图
 * 3.参赛选手的参赛介绍
 * 4.当前活动的结束时间 2020-09-30 20:45
 * 5.带参数的小程序二维码
 */
@Data
public class ShareUserWorkVO {

    private String activeName;

    private String userWorkName;

    private Integer userWorkId;

    private Integer activeId;

    private String activeDesc;

    private String userWorkDesc;

    private String activeEndTimeFormat;

    private String userWorkFirstImg;

    /**
     * 微信分享图
     */
    private String weixinShareQrCode;


}
