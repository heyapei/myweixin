package com.hyp.myweixin.pojo.modal;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import javax.persistence.*;

@Table(name = "weixin_vote_conf")
@Mapper
@Data
public class WeixinVoteConf {


    public static WeixinVoteConf init() {
        WeixinVoteConf weixinVoteConf = new WeixinVoteConf();
        weixinVoteConf.setActiveVoteBaseId(0);
        weixinVoteConf.setActiveConfMusicId(0);
        weixinVoteConf.setActiveConfRepeatVote(0);
        weixinVoteConf.setActiveConfVoteType("1;1");
        weixinVoteConf.setActiveConfSignUp(0);
        weixinVoteConf.setActiveConfVerify(0);
        weixinVoteConf.setActiveConfNumHide(0);
        weixinVoteConf.setActiveConfUserHide(0);
        weixinVoteConf.setActiveConfRankHide(0);
        weixinVoteConf.setCreateTime(new Date());
        weixinVoteConf.setUpdateTime(new Date());
        weixinVoteConf.setActiveUploadStartTime(new Date());
        weixinVoteConf.setActiveUploadEndTime(new Date());
        weixinVoteConf.setActiveConfSex("");
        weixinVoteConf.setActiveConfRegion("");
        weixinVoteConf.setActiveConfNeedWeixin(0);
        weixinVoteConf.setActiveConfNeedPhone(0);
        weixinVoteConf.setActiveConfShareImg("");
        return weixinVoteConf;
    }
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 对应的投票基础表的ID
     */
    @Column(name = "active_vote_base_id")
    private Integer activeVoteBaseId;

    /**
     * 对应的音乐ID 音乐配置表中配置
     */
    @Column(name = "active_conf_music_id")
    private Integer activeConfMusicId;

    /**
     * 是否可以重复投票 0默认不开启 1开启
     */
    @Column(name = "active_conf_repeat_vote")
    private Integer activeConfRepeatVote;

    /**
     * 改成string类型的，格式为 类型;票数
     * 例如：1,4 每天；4票
     * 投票类型限制例如1天1票 投票类型配置表中设置
     */
    @Column(name = "active_conf_vote_type")
    private String activeConfVoteType;

    /**
     * 是否开启用户自己上传作品 0 默认开启 1 不开启
     */
    @Column(name = "active_conf_sign_up")
    private Integer activeConfSignUp;

    public enum ActiveConfSignUpEnum{
        CAN_SIGN_UP(0, "允许用户上传"),
        CANT_SIGN_UP(1, "禁止用户上传");

        /**
         * 类型码
         */
        private Integer code;
        /**
         * 描述
         */
        private String msg;

        ActiveConfSignUpEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        @Override
        public String toString() {
            return "ActiveConfSignUpEnum{" +
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
    }

    /**
     * 活动是否需要验证码 0默认不开启 1 开启
     */
    @Column(name = "active_conf_verify")
    private Integer activeConfVerify;

    /**
     * 投票数是否需要隐藏 0默认不开启 1开启
     */
    @Column(name = "active_conf_num_hide")
    private Integer activeConfNumHide;

    /**
     * 投票用户是否需要隐藏 0默认不开启 1开启
     */
    @Column(name = "active_conf_user_hide")
    private Integer activeConfUserHide;

    /**
     * 投票排名是否隐藏 0默认不开启 1开启
     */
    @Column(name = "active_conf_rank_hide")
    private Integer activeConfRankHide;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 最后一次的修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 允许上传开始时间
     */
    @Column(name = "active_upload_start_time")
    private Date activeUploadStartTime;

    /**
     * 允许上传结束时间
     */
    @Column(name = "active_upload_end_time")
    private Date activeUploadEndTime;

    /**
     * 性别限制 （无 男 女）
     */
    @Column(name = "active_conf_sex")
    private String activeConfSex;

    /**
     * 地区限制（无 地理位置）
     */
    @Column(name = "active_conf_region")
    private String activeConfRegion;

    /**
     * 是否需要微信号（0 默认不需要 1 需要）
     */
    @Column(name = "active_conf_need_weixin")
    private Integer activeConfNeedWeixin;

    public enum ActiveConfNeedWeixinEnum{
        NEED_WEIXIN(1, "不需要"),
        NOT_NEED_WEIXIN(0, "需要");

        /**
         * 类型码
         */
        private Integer code;
        /**
         * 描述
         */
        private String msg;

        ActiveConfNeedWeixinEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        @Override
        public String toString() {
            return "ActiveConfNeedWeixinEnum{" +
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
    }

    /**
     * 是否需要手机号（0 默认不需要 1 需要）
     */
    @Column(name = "active_conf_need_phone")
    private Integer activeConfNeedPhone;

    public enum ActiveConfNeedPhoneEnum{
        NEED_PHONE(1, "不需要"),
        NOT_NEED_PHONE(0, "需要");

        /**
         * 类型码
         */
        private Integer code;
        /**
         * 描述
         */
        private String msg;

        ActiveConfNeedPhoneEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        @Override
        public String toString() {
            return "ActiveConfNeedPhoneEnum{" +
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
    }

    /**
     * 用于分享的图片
     */
    @Column(name = "active_conf_share_img")
    private String activeConfShareImg;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取对应的投票基础表的ID
     *
     * @return active_vote_base_id - 对应的投票基础表的ID
     */
    public Integer getActiveVoteBaseId() {
        return activeVoteBaseId;
    }

    /**
     * 设置对应的投票基础表的ID
     *
     * @param activeVoteBaseId 对应的投票基础表的ID
     */
    public void setActiveVoteBaseId(Integer activeVoteBaseId) {
        this.activeVoteBaseId = activeVoteBaseId;
    }

    /**
     * 获取对应的音乐ID 音乐配置表中配置
     *
     * @return active_conf_music_id - 对应的音乐ID 音乐配置表中配置
     */
    public Integer getActiveConfMusicId() {
        return activeConfMusicId;
    }

    /**
     * 设置对应的音乐ID 音乐配置表中配置
     *
     * @param activeConfMusicId 对应的音乐ID 音乐配置表中配置
     */
    public void setActiveConfMusicId(Integer activeConfMusicId) {
        this.activeConfMusicId = activeConfMusicId;
    }

    /**
     * 获取是否可以重复投票 0默认不开启 1开启
     *
     * @return active_conf_repeat_vote - 是否可以重复投票 0默认不开启 1开启
     */
    public Integer getActiveConfRepeatVote() {
        return activeConfRepeatVote;
    }

    /**
     * 设置是否可以重复投票 0默认不开启 1开启
     *
     * @param activeConfRepeatVote 是否可以重复投票 0默认不开启 1开启
     */
    public void setActiveConfRepeatVote(Integer activeConfRepeatVote) {
        this.activeConfRepeatVote = activeConfRepeatVote;
    }

    /**
     * 获取是否开启用户自己上传作品 0 默认开启 1 不开启
     *
     * @return active_conf_sign_up - 是否开启用户自己上传作品 0 默认开启 1 不开启
     */
    public Integer getActiveConfSignUp() {
        return activeConfSignUp;
    }

    /**
     * 设置是否开启用户自己上传作品 0 默认开启 1 不开启
     *
     * @param activeConfSignUp 是否开启用户自己上传作品 0 默认开启 1 不开启
     */
    public void setActiveConfSignUp(Integer activeConfSignUp) {
        this.activeConfSignUp = activeConfSignUp;
    }

    /**
     * 获取活动是否需要验证码 0默认不开启 1 开启
     *
     * @return active_conf_verify - 活动是否需要验证码 0默认不开启 1 开启
     */
    public Integer getActiveConfVerify() {
        return activeConfVerify;
    }

    /**
     * 设置活动是否需要验证码 0默认不开启 1 开启
     *
     * @param activeConfVerify 活动是否需要验证码 0默认不开启 1 开启
     */
    public void setActiveConfVerify(Integer activeConfVerify) {
        this.activeConfVerify = activeConfVerify;
    }

    /**
     * 获取投票数是否需要隐藏 0默认不开启 1开启
     *
     * @return active_conf_num_hide - 投票数是否需要隐藏 0默认不开启 1开启
     */
    public Integer getActiveConfNumHide() {
        return activeConfNumHide;
    }

    /**
     * 设置投票数是否需要隐藏 0默认不开启 1开启
     *
     * @param activeConfNumHide 投票数是否需要隐藏 0默认不开启 1开启
     */
    public void setActiveConfNumHide(Integer activeConfNumHide) {
        this.activeConfNumHide = activeConfNumHide;
    }

    /**
     * 获取投票用户是否需要隐藏 0默认不开启 1开启
     *
     * @return active_conf_user_hide - 投票用户是否需要隐藏 0默认不开启 1开启
     */
    public Integer getActiveConfUserHide() {
        return activeConfUserHide;
    }

    /**
     * 设置投票用户是否需要隐藏 0默认不开启 1开启
     *
     * @param activeConfUserHide 投票用户是否需要隐藏 0默认不开启 1开启
     */
    public void setActiveConfUserHide(Integer activeConfUserHide) {
        this.activeConfUserHide = activeConfUserHide;
    }

    /**
     * 获取投票排名是否隐藏 0默认不开启 1开启
     *
     * @return active_conf_rank_hide - 投票排名是否隐藏 0默认不开启 1开启
     */
    public Integer getActiveConfRankHide() {
        return activeConfRankHide;
    }

    /**
     * 设置投票排名是否隐藏 0默认不开启 1开启
     *
     * @param activeConfRankHide 投票排名是否隐藏 0默认不开启 1开启
     */
    public void setActiveConfRankHide(Integer activeConfRankHide) {
        this.activeConfRankHide = activeConfRankHide;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取最后一次的修改时间
     *
     * @return update_time - 最后一次的修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置最后一次的修改时间
     *
     * @param updateTime 最后一次的修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取允许上传开始时间
     *
     * @return active_upload_start_time - 允许上传开始时间
     */
    public Date getActiveUploadStartTime() {
        return activeUploadStartTime;
    }

    /**
     * 设置允许上传开始时间
     *
     * @param activeUploadStartTime 允许上传开始时间
     */
    public void setActiveUploadStartTime(Date activeUploadStartTime) {
        this.activeUploadStartTime = activeUploadStartTime;
    }

    /**
     * 获取允许上传结束时间
     *
     * @return active_upload_end_time - 允许上传结束时间
     */
    public Date getActiveUploadEndTime() {
        return activeUploadEndTime;
    }

    /**
     * 设置允许上传结束时间
     *
     * @param activeUploadEndTime 允许上传结束时间
     */
    public void setActiveUploadEndTime(Date activeUploadEndTime) {
        this.activeUploadEndTime = activeUploadEndTime;
    }

    /**
     * 获取性别限制 （无 男 女）
     *
     * @return active_conf_sex - 性别限制 （无 男 女）
     */
    public String getActiveConfSex() {
        return activeConfSex;
    }

    /**
     * 设置性别限制 （无 男 女）
     *
     * @param activeConfSex 性别限制 （无 男 女）
     */
    public void setActiveConfSex(String activeConfSex) {
        this.activeConfSex = activeConfSex;
    }

    /**
     * 获取地区限制（无 地理位置）
     *
     * @return active_conf_region - 地区限制（无 地理位置）
     */
    public String getActiveConfRegion() {
        return activeConfRegion;
    }

    /**
     * 设置地区限制（无 地理位置）
     *
     * @param activeConfRegion 地区限制（无 地理位置）
     */
    public void setActiveConfRegion(String activeConfRegion) {
        this.activeConfRegion = activeConfRegion;
    }

    /**
     * 获取是否需要微信号（0 默认不需要 1 需要）
     *
     * @return active_conf_need_weixin - 是否需要微信号（0 默认不需要 1 需要）
     */
    public Integer getActiveConfNeedWeixin() {
        return activeConfNeedWeixin;
    }

    /**
     * 设置是否需要微信号（0 默认不需要 1 需要）
     *
     * @param activeConfNeedWeixin 是否需要微信号（0 默认不需要 1 需要）
     */
    public void setActiveConfNeedWeixin(Integer activeConfNeedWeixin) {
        this.activeConfNeedWeixin = activeConfNeedWeixin;
    }

    /**
     * 获取是否需要手机号（0 默认不需要 1 需要）
     *
     * @return active_conf_need_phone - 是否需要手机号（0 默认不需要 1 需要）
     */
    public Integer getActiveConfNeedPhone() {
        return activeConfNeedPhone;
    }

    /**
     * 设置是否需要手机号（0 默认不需要 1 需要）
     *
     * @param activeConfNeedPhone 是否需要手机号（0 默认不需要 1 需要）
     */
    public void setActiveConfNeedPhone(Integer activeConfNeedPhone) {
        this.activeConfNeedPhone = activeConfNeedPhone;
    }

    /**
     * 获取用于分享的图片
     *
     * @return active_conf_share_img - 用于分享的图片
     */
    public String getActiveConfShareImg() {
        return activeConfShareImg;
    }

    /**
     * 设置用于分享的图片
     *
     * @param activeConfShareImg 用于分享的图片
     */
    public void setActiveConfShareImg(String activeConfShareImg) {
        this.activeConfShareImg = activeConfShareImg;
    }
}