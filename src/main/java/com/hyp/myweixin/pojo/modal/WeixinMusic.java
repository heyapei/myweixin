package com.hyp.myweixin.pojo.modal;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import javax.persistence.*;

@Table(name = "weixin_music")
@Mapper
@Data
public class WeixinMusic {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 音乐类型
     */
    @Column(name = "music_type")
    private String musicType = "";

    /**
     * 状态值 0正常 1下线
     */
    private Integer status = 0;

    /**
     * 音乐地址
     */
    @Column(name = "music_url")
    private String musicUrl = "";

    /**
     * 音乐适用方向 0适用于任何方向
     */
    @Column(name = "apply_type")
    private Integer applyType = 0;

    /**
     * 名字
     */
    @Column(name = "music_name")
    private String musicName = "";

    /**
     * 描述
     */
    @Column(name = "music_desc")
    private String musicDesc = "";

    }