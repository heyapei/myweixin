package com.hyp.myweixin.pojo.dto.bdmap;

import lombok.Data;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/7/6 15:49
 * @Description: TODO 百度地图简单的数据实体类
 */
@Data
public class BDMapResSimpleDTO {
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 大致实体位置
     */
    private String generalAddress;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 经纬度
     */
    private String location;
}
