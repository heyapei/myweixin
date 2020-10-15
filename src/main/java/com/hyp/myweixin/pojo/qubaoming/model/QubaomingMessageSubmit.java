package com.hyp.myweixin.pojo.qubaoming.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "qubaoming_message_submit")
public class QubaomingMessageSubmit {

    public static QubaomingMessageSubmit init() {
        QubaomingMessageSubmit qubaomingMessageSubmit = new QubaomingMessageSubmit();
        qubaomingMessageSubmit.setMessageId("");
        qubaomingMessageSubmit.setUserId(0);
        qubaomingMessageSubmit.setActiveId(0);
        qubaomingMessageSubmit.setSubmitTime(new Date());
        qubaomingMessageSubmit.setSendTime(new Date());
        qubaomingMessageSubmit.setStatus(0);
        qubaomingMessageSubmit.setExt1("");
        qubaomingMessageSubmit.setExt2("");
        qubaomingMessageSubmit.setExt3("");
        return qubaomingMessageSubmit;
    }


    @Id
    private Integer id;

    @Column(name = "message_id")
    private String messageId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "active_id")
    private Integer activeId;

    @Column(name = "submit_time")
    private Date submitTime;

    @Column(name = "send_time")
    private Date sendTime;

    /**
     * 0默认未发送 1已发送 -1发送失败
     */
    private Integer status;

    public enum StatusEnum {
        UN_SEND(0, "未发送"),
        SUCCESS(1, "成功"),
        FAIL(-1, "失败");

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
            return "ActiveStatusEnum{" +
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

        StatusEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }


    private String ext1;

    private String ext2;

    private String ext3;

}