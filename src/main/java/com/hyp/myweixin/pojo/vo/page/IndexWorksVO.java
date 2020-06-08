package com.hyp.myweixin.pojo.vo.page;

import lombok.Data;

import java.util.Date;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/8 17:33
 * @Description: TODO
 */
@Data
public class IndexWorksVO {

    /**
     * 主键
     */

    private Integer id;

    /**
     * 封面图片
     */

    private String activeImg = "";

    /**
     * 活动名称
     */

    private String activeName = "";

    /**
     * 活动描述
     */

    private String activeDesc = "";

    /**
     * 活动描述图片
     */

    private String activeDescImg = "";

    /**
     * 活动奖励
     */

    private String activeReward = "";

    /**
     * 活动奖励图片
     */

    private String activeRewardImg = "";

    /**
     * 活动开始时间
     */

    private Date activeStartTime = new Date();

    /**
     * 活动结束时间
     */

    private Date activeEndTime = new Date();

    /**
     * 是否公开到首页 0默认公开 1不公开
     */

    private Integer activePublic = 1;

    /**
     * 活动展示优先级 数值越高 优先级越高 默认为0 默认展示
     */

    private Integer activeShowOrder = 0;

    /**
     * 0 待审核 1上线  2未开始 3已结束
     */
    private Integer status = 0;

    /**
     * 活动创建时间
     */

    private Date createTime = new Date();

    /**
     * 最后的更新时间
     */

    private Date updateTime = new Date();

    /**
     * 活动创建人Id
     */
    private Integer createSysUserId;


    /**
     * 该活动参数人数的总数
     */
    private Integer voteWorkJoinCount;

    /**
     * 该活动被投票的总数
     */
    private Integer voteWorkVoteCount;

    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;

}
