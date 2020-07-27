package com.hyp.myweixin.pojo.modal;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author heyapei
 */
@Table(name = "weixin_user_info_supply")
@Data
public class WeixinUserInfoSupply {


    /**
     * 初始化数据
     *
     * @return
     */
    public static WeixinUserInfoSupply init() {
        WeixinUserInfoSupply weixinUserInfoSupply = new WeixinUserInfoSupply();
        weixinUserInfoSupply.setRealName("");
        weixinUserInfoSupply.setUserId(0);
        weixinUserInfoSupply.setAddress("");
        weixinUserInfoSupply.setEmail("");
        weixinUserInfoSupply.setBirthday(0L);
        weixinUserInfoSupply.setPhone("");
        weixinUserInfoSupply.setGender(0);
        weixinUserInfoSupply.setExt1(0);
        weixinUserInfoSupply.setExt2(0);
        weixinUserInfoSupply.setExt3("");
        weixinUserInfoSupply.setExt4("");
        weixinUserInfoSupply.setExt5("");
        weixinUserInfoSupply.setWechatNumber("");
        weixinUserInfoSupply.setCreateDate(System.currentTimeMillis());
        weixinUserInfoSupply.setUpdateDate(System.currentTimeMillis());
        return weixinUserInfoSupply;
    }

    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 用户ID 对应weixinvoteuser的id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 真实姓名
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 收件地址
     */
    private String address;

    /**
     * 邮件地址
     */
    private String email;

    /**
     * 微信号
     */
    @Column(name = "wechat_number")
    private String wechatNumber;

    /**
     * 生日 采用时间戳格式
     */
    private Long birthday;

    /**
     * 真实的手机号
     */
    private String phone;

    /**
     * 性别 1男 2女 0未知 默认未知
     */
    private Integer gender;

    public enum GenderEnum {
        UNKNOWN(0, "未知"),
        MALE(1, "男"),
        FEMALE(2, "女"),
        ;

        private Integer code;
        private String msg;

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        GenderEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }


    /**
     * 备用字段
     */
    private Integer ext1;

    /**
     * 备用字段2
     */
    private Integer ext2;

    /**
     * 备用字段3
     */
    private String ext3;

    /**
     * 备用字段4
     */
    private String ext4;

    /**
     * 备用字段5
     */
    private String ext5;


    /**
     * 信息创建时间
     */
    @Column(name = "create_date")
    private Long createDate;
    /**
     * 信息更新时间
     */
    @Column(name = "update_date")
    private Long updateDate;

}