package com.hyp.myweixin.pojo.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "weixin_user_option_log")
@Mapper
@Data
@AllArgsConstructor
public class WeixinUserOptionLog {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * ip地址
     * 默认为 127.0.0.1地址
     */
    private Long ip;

    /**
     * 大致的地理信息
     */
    @Column(name = "general_address")
    private String generalAddress;

    /**
     * 省份信息
     */
    @Column(name = "province")
    private String province;
    /**
     * 城市信息
     */
    @Column(name = "city")
    private String city;

    /**
     * 设备类型
     */
    @Column(name = "device_type")
    private String deviceType;

    /**
     * 设备名称
     */
    @Column(name = "device_name")
    private String deviceName;

    /**
     * 操作类型
     */
    @Column(name = "option_type")
    private Integer optionType;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime = new Date();

    /**
     * 操作对象1
     */
    @Column(name = "option_object")
    private String optionObject = "";
    /**
     * 操作对象2
     */
    @Column(name = "option_object_two")
    private String optionObjectTwo = "";
    /**
     * 操作说明
     */
    @Column(name = "option_desc")
    private String optionDesc = "";


    public WeixinUserOptionLog() {
        this.optionType = 0;
        this.createTime = new Date();
        this.deviceName = "未知设备名称";
        this.deviceType = "未知设备类型";
        this.generalAddress = "未知地理未知";
        this.ip = 2130706433L;
        this.city = "未知城市信息";
        this.province = "未知省份信息";
    }
}