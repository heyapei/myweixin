package com.hyp.myweixin.pojo.modal;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "weixin_vote_organisers")
@Mapper
@Data
public class WeixinVoteOrganisers {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 对应的投票活动ID
     */
    @Column(name = "vote_base_id")
    private Integer voteBaseId;

    /**
     * 名称
     */
    private String name = "";

    /**
     * logo图片
     */
    @Column(name = "logo_img")
    private String logoImg = "";

    /**
     * 描述
     */
    @Column(name = "organisers_desc")
    private String organisersDesc = "";

    /**
     * 联系电话
     */
    private String phone = "";

    /**
     * 公司地址
     */
    private String address = "";

    /**
     * 公司名称
     */
    private String company = "";

    /**
     * 公司类型
     */
    private String type = "";

    /**
     * 公司主营业务
     */
    @Column(name = "job_major")
    private String jobMajor = "";

    /**
     * 公司创建时间
     */
    @Column(name = "build_time")
    private Date buildTime = new Date();

    /**
     * 法人名称
     */
    private String corporate = "";


    /**
     * 公司二维码图片路径
     */
    @Column(name = "weixin_qr_code")
    private String weixinQrCode = "";

}