package com.hyp.myweixin.utils.bdmaputil;

import com.hyp.myweixin.pojo.dto.bdmap.BDMapResSimpleDTO;
import com.hyp.myweixin.pojo.dto.bdmap.gpstoaddress.BDMapGpsToAddressDTO;
import com.hyp.myweixin.pojo.dto.bdmap.iptogps.BDMapIpToGpsDTO;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/7/6 15:27
 * @Description: TODO
 */
public interface BDMapUtilService {


    /**
     * 通过IP地址查询对应的数据
     *
     * @param ip
     * @return
     */
    BDMapResSimpleDTO getBDMapResSimpleDTOByIp(String ip);


    /**
     * 通过坐标 获取大致的地理位置（街道信息）
     *
     * @param location
     * @return
     */
    BDMapGpsToAddressDTO getGeocodeByIpAddressGeneralNoAsync(String location);

    /**
     * 获取定位信息
     *
     * @param ipAddress
     * @return
     */
    BDMapIpToGpsDTO getIpPositionNoAsync(String ipAddress);

}
