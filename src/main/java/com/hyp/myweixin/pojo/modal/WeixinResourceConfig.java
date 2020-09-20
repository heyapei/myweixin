package com.hyp.myweixin.pojo.modal;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author heyapei
 */
@Table(name = "weixin_resource_config")
@Mapper
@Data
public class WeixinResourceConfig {
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
     * 资源类型 默认为0 默认资源 1头部信息 2投票展示信息
     */
    private Integer type;

    /**
     * 资源类型描述
     */
    private String description;


    public enum ConfigType {

        HeadImg(0, "头部图片"),
        QuBaoMingRotationChart(6, "趣报名轮播图");


        private Integer configType;
        private String configTypeDesc;

        ConfigType(Integer configType, String configTypeDesc) {
            this.configType = configType;
            this.configTypeDesc = configTypeDesc;
        }

        public Integer getConfigType() {
            return configType;
        }

        public String getConfigTypeDesc() {
            return configTypeDesc;
        }

        @Override
        public String toString() {
            return configType + ": " + configTypeDesc;
        }

    }


}