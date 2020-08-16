package com.hyp.myweixin.pojo.qubaoming.model;

import lombok.Data;

import javax.persistence.*;
@Data
@Table(name = "wechat_company")
public class WechatCompany {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 名称
     */
    @Column(name = "company_name")
    private String companyName;

    /**
     * logo图片
     */
    @Column(name = "logo_img")
    private String logoImg;

    /**
     * 描述
     */
    @Column(name = "organisers_desc")
    private String organisersDesc;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 公司地址
     */
    private String address;

    /**
     * 公司名称（充当公司简介）
     */
    private String company;

    /**
     * 公司类型
     */
    private String type;

    /**
     * 公司主营业务
     */
    @Column(name = "job_major")
    private String jobMajor;

    /**
     * 公司创建时间
     */
    @Column(name = "build_time")
    private Long buildTime;

    /**
     * 法人名称
     */
    private String corporate;

    /**
     * 公司二维码路径
     */
    @Column(name = "weixin_qr_code")
    private String weixinQrCode;

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

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Long createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Long updateTime;

    /**
     * 分享次数
     */
    @Column(name = "company_share_num")
    private Integer companyShareNum;

    /**
     * 查看次数
     */
    @Column(name = "company_view_num")
    private Integer companyViewNum;

    /**
     * 收藏次数
     */
    @Column(name = "company_collection_num")
    private Integer companyCollectionNum;

    /**
     * 使用次数
     */
    @Column(name = "company_used_num")
    private Integer companyUsedNum;

    /**
     * 0 标识默认 默认使用的公司主体内容
     */
    @Column(name = "company_show_order")
    private Integer companyShowOrder;

    public enum CompanyShowOrderEnum {
        ORDINARY_COMPANY(0, "普通"),
        DEFAULT_COMPANY(1, "默认");
        /**
         * 类型码
         */
        private Integer code;
        /**
         * 描述
         */
        private String msg;

        @Override
        public String toString() {
            return "CompanyShowOrderEnum{" +
                    "code=" + code +
                    ", msg='" + msg + '\'' +
                    '}';
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        CompanyShowOrderEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }


}