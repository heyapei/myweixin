package com.hyp.myweixin.pojo.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "weixin_user_option_config")
@Mapper
@Data
@AllArgsConstructor
public class WeixinUserOptionConfig {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 关键字 最好几个字
     */
    @Column(name = "key_word")
    private String keyWord;

    /**
     * 分类信息 0投票小程序
     */
    private Integer classify;

    /**
     * 类型 默认为0进入程序 1点赞
     */
    private Integer type;

    /**
     * 类型描述
     */
    private String description;

    /**
     * 是否有效 0有效 1无效
     */
    private Integer status = 0;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime = new Date();


    public enum typeEnum {

        INTO_WEiXIN_VOTE(0, "进入微信投票小程序"),
        VIEW_WEiXIN_VOTE_WORK(1, "浏览投票活动"),
        INTO_WEiXIN_VOTE_USER_WORK(2, "浏览具体的用户作品"),
        VOTE_WEiXIN_VOTE_WORK(3, "对具体用户作品投票");

        /**
         * 类型码
         */
        private Integer type;
        /**
         * 描述
         */
        private String msg;

        typeEnum(Integer type, String msg) {
            this.type = type;
            this.msg = msg;
        }

        public int getType() {
            return type;
        }

        public String getMsg() {
            return msg;
        }

        @Override
        public String toString() {
            return type + ": " + msg;
        }
    }


}