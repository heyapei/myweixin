package com.hyp.myweixin.pojo.qubaoming.vo.active;

import com.hyp.myweixin.pojo.qubaoming.model.WechatCompany;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/18 18:23
 * @Description: TODO
 */
@Data
public class ActiveByShowActiveCompleteVO {


    @ApiModelProperty(value = "活动ID", name = "id", required = true)
    private Integer id;

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称", name = "activeName", required = true)
    private String activeName;

    /**
     * 活动封面图
     */
    @ApiModelProperty(value = "活动封面图去分号", name = "activeImgList", required = true)
    private String[] activeImgList;

    /**
     * 活动封面图
     */
    @ApiModelProperty(value = "活动封面图", name = "activeImg", required = true)
    private String activeImg;

    /**
     * 活动描述
     */
    @ApiModelProperty(value = "活动描述", name = "activeDesc", required = true)
    private String activeDesc;

    /**
     * 活动描述图片
     */
    @ApiModelProperty(value = "活动描述图片去除分号", name = "activeDescImgList", required = true)
    private String[] activeDescImgList;

    /**
     * 活动描述图片
     */
    @ApiModelProperty(value = "活动描述图片", name = "activeDescImg", required = true)
    private String activeDescImg;




    /**
     * 活动详情
     */
    @ApiModelProperty(value = "分享数量", name = "activeShareNum", required = true)
    private String activeDetail;

    /**
     * 活动详情图片
     */
    @ApiModelProperty(value = "活动详情图片", name = "activeDetailImg", required = true)
    private String activeDetailImg;

    /**
     * 活动详情图片
     */
    @ApiModelProperty(value = "活动详情图片去分号", name = "activeDetailImgList", required = true)
    private String[] activeDetailImgList;

    /**
     * 活动状态 0 未上线 1 上线 2下线 3暂停 4未完成
     */
    @ApiModelProperty(value = "活动状态 0 未上线 1 上线 2下线 3暂停 4未完成", name = "activeStatus", required = true)
    private Integer activeStatus;


    /**
     * 分享数量
     */
    @ApiModelProperty(value = "分享数量", name = "activeShareNum", required = true)
    private Integer activeShareNum;

    /**
     * 查看数量
     */
    @ApiModelProperty(value = "查看数量", name = "activeViewNum", required = true)
    private Integer activeViewNum;

    /**
     * 参与数量
     */
    @ApiModelProperty(value = "参与数量", name = "activeJoinNum", required = true)
    private Integer activeJoinNum;

    /**
     * 收藏数量
     */
    @ApiModelProperty(value = "收藏数量", name = "activeCollectionNum", required = true)
    private Integer activeCollectionNum;

    /**
     * 展示优先级
     */
    @ApiModelProperty(value = "展示优先级", name = "activeShowOrder", required = true)
    private Integer activeShowOrder;

    /**
     * 对应的公司主体ID
     */
    @ApiModelProperty(value = "对应的公司主体ID", name = "activeCompanyId", required = true)
    private Integer activeCompanyId;


    @ApiModelProperty(value = "公司信息", name = "wechatCompany", required = true)
    private WechatCompany wechatCompany;

    /**
     * 创建人ID
     */
    @ApiModelProperty(value = "分享数量", name = "activeShareNum", required = true)
    private Integer activeUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "分享数量", name = "activeShareNum", required = true)
    private Long createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "分享数量", name = "activeShareNum", required = true)
    private Long updateTime;

    /**
     * 备用字段
     */
    @ApiModelProperty(value = "备用字段", name = "ext1", required = true)
    private String ext1;

    /**
     * 备用字段
     */
    @ApiModelProperty(value = "备用字段", name = "ext2", required = true)
    private String ext2;

    /**
     * 备用字段
     */
    @ApiModelProperty(value = "备用字段", name = "ext3", required = true)
    private String ext3;

    /**
     * 备用字段
     */
    @ApiModelProperty(value = "备用字段", name = "ext4", required = true)
    private String ext4;

    /**
     * 备用字段
     */
    @ApiModelProperty(value = "备用字段", name = "ext5", required = true)
    private String ext5;

}
