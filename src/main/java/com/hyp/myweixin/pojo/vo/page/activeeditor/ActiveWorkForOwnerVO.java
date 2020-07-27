package com.hyp.myweixin.pojo.vo.page.activeeditor;

import lombok.Data;

import java.util.Date;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/26 20:13
 * @Description: TODO 我发起的活动数据
 */
@Data
public class ActiveWorkForOwnerVO {

    /**
     * 主键
     */
    private Integer activeId;

    /**
     * 封面图
     */
    private String activeImg;

    /**
     * 活动名称
     */
    private String activeName;


    /**
     * 活动开始时间
     */
    private Date activeStartTime;

    /**
     * 活动结束时间
     */
    private Date activeEndTime;

    /**
     * 是否公开到首页
     */
    private String activeIsPublic;

    /**
     * 活动当前状态
     */
    private String activeStatus;

    /**
     * 活动创建时间
     */
    private Date createTime;

    /**
     * 被浏览总次数
     */
    private Integer viewCountNum;
    /**
     * 被投票总次数
     */
    private Integer voteCountNum;

    /**
     * 作品数
     */
    private Integer voteWorkNum;


}
