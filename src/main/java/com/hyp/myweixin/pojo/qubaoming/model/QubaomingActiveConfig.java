package com.hyp.myweixin.pojo.qubaoming.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "qubaoming_active_config")
public class QubaomingActiveConfig {
    @Id
    private Integer id;

    /**
     * 活动ID
     */
    @Column(name = "active_base_id")
    private Integer activeId;

    /**
     * 活动类型 0 线下活动 1线上活动
     */
    @Column(name = "active_type")
    private Integer activeType;

    /**
     * 活动地点
     */
    @Column(name = "active_address")
    private String activeAddress;

    /**
     * 活动开始时间
     */
    @Column(name = "active_start_time")
    private Long activeStartTime;

    /**
     * 活动结束时间
     */
    @Column(name = "active_end_time")
    private Long activeEndTime;

    /**
     * 开始报名时间
     */
    @Column(name = "sign_up_start_time")
    private Long signUpStartTime;

    /**
     * 结束报名时间
     */
    @Column(name = "sign_up_end_time")
    private Long signUpEndTime;

    /**
     * 报名人数最大值 0 不限制 其他值为最大限制值
     */
    @Column(name = "active_join_num_max")
    private Integer activeJoinNumMax;

    /**
     * 报名必选项 1姓名 2年纪 3电话 4性别
     */
    @Column(name = "active_require_option")
    private String activeRequireOption;

    public enum ActiveRequireOptionEnum {
        NAME(1, "姓名"),
        AGE(2, "年龄"),
        PHONE(3, "电话"),
        GENDER(4, "性别");

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
            return "ActiveRequireOptionEnum{" +
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

        ActiveRequireOptionEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
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