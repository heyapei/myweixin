package com.hyp.myweixin.pojo.modal;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import javax.persistence.*;

/**
 * @author heyapei
 */
@Table(name = "weixin_vote_work")
@Mapper
@Data
public class WeixinVoteWork {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 作品人姓名
     */
    @Column(name = "vote_work_user_name")
    private String voteWorkUserName = "";

    /**
     * 作品人用户Id
     */
    @Column(name = "vote_work_user_id")
    private Integer voteWorkUserId;

    /**
     * 作品人手机号
     */
    @Column(name = "vote_work_user_phone")
    private String voteWorkUserPhone ="";

    /**
     * 作品人微信号
     */
    @Column(name = "vote_work_user_weixin")
    private String voteWorkUserWeixin ="";

    /**
     * 作品描述
     */
    @Column(name = "vote_work_desc")
    private String voteWorkDesc ="";

    /**
     * 作品名称
     */
    @Column(name = "vote_work_name")
    private String voteWorkName ="";

    /**
     * 作品视频文件Id
     */
    @Column(name = "vote_work_video_url")
    private String voteWorkVideoUrl ="";

    /**
     * 作品创建时间
     */
    @Column(name = "vote_work_create_time")
    private Date voteWorkCreateTime = new Date();

    /**
     * 作品所属活动的ID
     */
    @Column(name = "active_vote_base_id")
    private Integer activeVoteBaseId ;

    /**
     * 作品的状态 0默认等待审核 1上线 2下线
     */
    @Column(name = "vote_work_status")
    private Integer voteWorkStatus;

    /**
     * 作品的展示排序
     */
    @Column(name = "vote_work_show_order")
    private Integer voteWorkShowOrder;

    /**
     * 作品被投票次数
     */
    @Column(name = "vote_work_count_num")
    private Integer voteWorkCountNum;

    /**
     * 作品图片
     */
    @Column(name = "vote_work_img")
    private String voteWorkImg ="";

}