package com.hyp.myweixin.pojo.modal;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "weixin_resource")
@Mapper
@Data
public class WeixinResource {


    /**
     * 数据初始化
     *
     * @return
     */
    public static WeixinResource init() {
        WeixinResource weixinResource = new WeixinResource();
        weixinResource.setResourceConfigId(0);
        weixinResource.setType("");
        weixinResource.setPath("");
        weixinResource.setName("");
        weixinResource.setRealName("");
        weixinResource.setStatus(0);
        weixinResource.setSize(0L);
        weixinResource.setMd5("");
        weixinResource.setTitle("");
        weixinResource.setDescription("");
        weixinResource.setCreateTime(new Date());
        weixinResource.setCreateUserId(0);
        weixinResource.setThumbnailPath1("");
        weixinResource.setThumbnailPath2("");
        weixinResource.setThumbnailPath3("");
        weixinResource.setThumbnailUrl1("");
        weixinResource.setThumbnailUrl2("");
        weixinResource.setThumbnailUrl3("");
        return weixinResource;
    }

    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 资源类型
     */
    @Column(name = "resource_config_id")
    private Integer resourceConfigId;

    /**
     * 文件后缀名 如png mp4
     */
    private String type;

    /**
     * 资源路径
     */
    private String path;

    /**
     * 名字系统分配的名称
     */
    private String name;

    /**
     * 文件真实的名称
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 状态 默认为0 开始状态 1关闭
     */
    private Integer status;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 文件MD5值
     */
    private String md5;

    /**
     * 文件title主要为文件未能正确加载页面显示
     */
    private String title;

    /**
     * 文件的描述
     */
    private String description;

    /**
     * 创建日期
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人userId
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    public enum Status {

        Allow(0, "可以被正常使用");


        private Integer state;
        private String stateDesc;

        Status(Integer state, String stateDesc) {
            this.state = state;
            this.stateDesc = stateDesc;
        }

        public Integer getState() {
            return state;
        }

        public String getStateDesc() {
            return stateDesc;
        }

        @Override
        public String toString() {
            return state + ": " + stateDesc;
        }

    }

    /**
     * 压缩图片1地址
     */
    @Column(name = "thumbnail_path")
    private String thumbnailPath1;
    /**
     * 压缩图片2地址
     */
    @Column(name = "thumbnail_path2")
    private String thumbnailPath2;
    /**
     * 压缩图片3地址
     */
    @Column(name = "thumbnail_path3")
    private String thumbnailPath3;
    /**
     * 缩略图1
     */
    @Column(name = "thumbnail_url1")
    private String thumbnailUrl1;
    /**
     * 缩略图2
     */
    @Column(name = "thumbnail_url2")
    private String thumbnailUrl2;
    /**
     * 缩略图3
     */
    @Column(name = "thumbnail_url3")
    private String thumbnailUrl3;


}