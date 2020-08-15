package com.hyp.myweixin.pojo.qubaoming.model;

import com.hyp.myweixin.utils.dateutil.MyDateUtil;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "qubaoming_active_base")
public class QubaomingActiveBase {

    /**
     * 数据实例化 不实例化ID
     *
     * @return
     */
    public static QubaomingActiveBase init() {
        return init(null);
    }

    /**
     * 数据实例化 实例化ID
     *
     * @return
     */
    public static QubaomingActiveBase initWithDefaultId() {
        return init(0);
    }


    /**
     * 指定实例化ID
     *
     * @param id
     * @return
     */
    public static QubaomingActiveBase initWithId(Integer id) {
        return init(id);
    }

    /**
     * 数据实例化
     *
     * @param id
     * @return
     */
    private static QubaomingActiveBase init(Integer id) {
        QubaomingActiveBase qubaomingActiveBase = new QubaomingActiveBase();
        if (id != null) {
            if (id > 0) {
                qubaomingActiveBase.setId(id);
            } else {
                qubaomingActiveBase.setId(0);
            }
        }
        qubaomingActiveBase.setActiveName("");
        qubaomingActiveBase.setActiveImg("");
        qubaomingActiveBase.setActiveDesc("");
        qubaomingActiveBase.setActiveDescImg("");
        qubaomingActiveBase.setActiveType(0);
        qubaomingActiveBase.setActiveDetail("");
        qubaomingActiveBase.setActiveDetailImg("");
        qubaomingActiveBase.setActiveStatus(0);
        qubaomingActiveBase.setActiveShareNum(0);
        qubaomingActiveBase.setActiveViewNum(0);
        qubaomingActiveBase.setActiveJoinNum(0);
        qubaomingActiveBase.setActiveCollectionNum(0);
        qubaomingActiveBase.setActiveShowOrder(0);
        qubaomingActiveBase.setActiveCompanyId(0);
        qubaomingActiveBase.setActiveUserId(0);
        qubaomingActiveBase.setCreateTime(System.currentTimeMillis());
        qubaomingActiveBase.setUpdateTime(System.currentTimeMillis());
        qubaomingActiveBase.setExt1("");
        qubaomingActiveBase.setExt2("");
        qubaomingActiveBase.setExt3("");
        qubaomingActiveBase.setExt4("");
        qubaomingActiveBase.setExt5("");
        return qubaomingActiveBase;
    }


    @Id
    private Integer id;

    /**
     * 活动名称
     */
    @Column(name = "active_name")
    private String activeName;

    /**
     * 活动封面图
     */
    @Column(name = "active_img")
    private String activeImg;

    /**
     * 活动描述
     */
    @Column(name = "active_desc")
    private String activeDesc;

    /**
     * 活动描述图片
     */
    @Column(name = "active_desc_img")
    private String activeDescImg;

    /**
     * 活动类型 0 线下活动 1线上活动
     */
    @Column(name = "active_type")
    private Integer activeType;

    /**
     * 活动详情
     */
    @Column(name = "active_detail")
    private String activeDetail;

    /**
     * 活动详情图片
     */
    @Column(name = "active_detail_img")
    private String activeDetailImg;

    /**
     * 活动状态 0 未上线 1 上线 2下线 3暂停 4未完成
     */
    @Column(name = "active_status")
    private Integer activeStatus;

    public enum ActiveStatusEnum {
        UN_VERIFY(0, "待审核"),
        ONLINE(1, "上线"),
        PAUSE(5, "暂停"),
        END(3, "已结束"),
        UN_COMPLETE(4, "未完成"),
        UN_RELEASE(7, "未发布（复制）"),
        DELETE(6, "已删除"),
        OFFLINE(2, "下线");

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

        ActiveStatusEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }


    /**
     * 分享数量
     */
    @Column(name = "active_share_num")
    private Integer activeShareNum;

    /**
     * 查看数量
     */
    @Column(name = "active_view_num")
    private Integer activeViewNum;

    /**
     * 参与数量
     */
    @Column(name = "active_join_num")
    private Integer activeJoinNum;

    /**
     * 收藏数量
     */
    @Column(name = "active_collection_num")
    private Integer activeCollectionNum;

    /**
     * 展示优先级
     */
    @Column(name = "active_show_order")
    private Integer activeShowOrder;

    /**
     * 对应的公司主体ID
     */
    @Column(name = "active_company_id")
    private Integer activeCompanyId;

    /**
     * 创建人ID
     */
    @Column(name = "active_user_id")
    private Integer activeUserId;

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