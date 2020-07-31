package com.hyp.myweixin.pojo.vo.page;

import lombok.Data;

import java.util.Date;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/13 20:28
 * @Description: TODO
 */
@Data
public class PageEditWorkDetailVO {

    /**
     * 活动名称
     */
    private String activeName;
    /**
     * 活动创建时间
     */
    private Date activeCreateTime;
    /**
     * 活动结束时间
     */
    private Date activeEndTime;
    /**
     * 活动当前id
     */
    private Integer activeId;

    /**
     * 待审核作品总数
     */
    private Integer activeUnREVIEWNum;

    /**
     * 当前活动作品数量
     */
    private Integer activeJoinCount;
    /**
     * 当前活动总浏览量
     */
    private Integer activeViewCount;
    /**
     * 活动图片
     */
    private String activeImg;

}
