package com.hyp.myweixin.pojo.vo.page.activeeditor;

import lombok.Data;

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


}
