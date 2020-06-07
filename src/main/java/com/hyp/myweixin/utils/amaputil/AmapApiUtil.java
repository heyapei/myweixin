package com.hyp.myweixin.utils.amaputil;

import com.hyp.myweixin.pojo.dto.AmapIpToAddressDTO;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/7 11:21
 * @Description: TODO 高德地图的API
 */
public interface AmapApiUtil {

    /**
     * 通过IP地址获取位置地理信息
     *
     * @param ipAddress
     * @return
     */
    AmapIpToAddressDTO getIpPosition(String ipAddress);

    /**
     * 通过经纬度信息 获取大致的地理位置（街道信息）
     *
     * @param location
     * @return
     */
    String getGeocodeByLocationGeneral(String location);

    /**
     * 通过坐标 获取大致的地理位置（街道信息）
     *
     * @param location
     * @return
     */
    String getGeocodeByIpAddressGeneralNoAsync(String location);

    /**
     * 不使用异步处理
     *
     * @param ipAddress
     * @return
     */
    AmapIpToAddressDTO getIpPositionNoAsync(String ipAddress);


}
