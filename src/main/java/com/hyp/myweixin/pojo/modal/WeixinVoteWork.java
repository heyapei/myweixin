package com.hyp.myweixin.pojo.modal;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author heyapei
 * 活动下面具体到用户的作品
 */
@Table(name = "weixin_vote_work")
@Mapper
@Data
public class WeixinVoteWork {


    /**
     * 数据实例化
     * @return
     */
    public static WeixinVoteWork init() {
        WeixinVoteWork weixinVoteWork = new WeixinVoteWork();
        weixinVoteWork.setVoteWorkUserName("");
        weixinVoteWork.setVoteWorkUserId(0);
        weixinVoteWork.setVoteWorkUserPhone("");
        weixinVoteWork.setVoteWorkUserWeixin("");
        weixinVoteWork.setVoteWorkDesc("");
        weixinVoteWork.setVoteWorkName("");
        weixinVoteWork.setVoteWorkVideoUrl("");
        weixinVoteWork.setVoteWorkCreateTime(new Date());
        weixinVoteWork.setActiveVoteBaseId(0);
        weixinVoteWork.setVoteWorkStatus(0);
        weixinVoteWork.setVoteWorkShowOrder(0);
        weixinVoteWork.setVoteWorkCountNum(0);
        weixinVoteWork.setVoteWorkImg("");
        weixinVoteWork.setVoteWorkCountViewNum(0);
        weixinVoteWork.setVoteWorkOr(0);
        return weixinVoteWork;
    }

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
    private String voteWorkUserPhone = "";

    /**
     * 作品人微信号
     */
    @Column(name = "vote_work_user_weixin")
    private String voteWorkUserWeixin = "";

    /**
     * 作品描述
     */
    @Column(name = "vote_work_desc")
    private String voteWorkDesc = "";

    /**
     * 作品名称
     */
    @Column(name = "vote_work_name")
    private String voteWorkName = "";

    /**
     * 作品视频文件Id
     */
    @Column(name = "vote_work_video_url")
    private String voteWorkVideoUrl = "";

    /**
     * 作品创建时间
     */
    @Column(name = "vote_work_create_time")
    private Date voteWorkCreateTime = new Date();

    /**
     * 作品所属活动的ID
     */
    @Column(name = "active_vote_base_id")
    private Integer activeVoteBaseId;

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
    private String voteWorkImg = "";
    /**
     * 作品被浏览次数
     */
    @Column(name = "vote_work_count_view_num")
    private Integer voteWorkCountViewNum = 0;

    /**
     * 作品在活动中的序号
     */
    @Column(name = "vote_work_or")
    private Integer voteWorkOr;

}