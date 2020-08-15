package com.hyp.myweixin.pojo.qubaoming.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "qubaoming_weixin_user")
public class QubaomingWeixinUser {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 对应的weixinUserInfoSupplyId
     */
    @Column(name = "weixin_user_info_supply_id")
    private Integer weixinUserInfoSupplyId;

    /**
     * 用户微信中的唯一标识
     */
    @Column(name = "open_id")
    private String openId;

    /**
     * 头像地址
     */
    @Column(name = "avatar_url")
    private String avatarUrl;

    /**
     * 昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 城市
     */
    private String city;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String country;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Long createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Long updateTime;

    /**
     * 是否允许活动 0 允许 1不允许
     */
    private Integer enable;

    public enum ENABLEENUM {

        ENABLE(0, "正常"),
        UN_ENABLE(1, "禁用");

        /**
         * 类型码
         */
        private Integer code;
        /**
         * 描述
         */
        private String msg;

        ENABLEENUM(Integer type, String msg) {
            this.code = type;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        @Override
        public String toString() {
            return "ENABLEENUM{" +
                    "code=" + code +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }

    /**
     * 备用字段
     */
    private String ext1;

    /**
     * 备用字段
     */
    private String ext2;

    /**
     * 备用字段
     */
    private String ext3;

    /**
     * 备用字段
     */
    private String ext4;

    /**
     * 备用字段
     */
    private String ext5;

}