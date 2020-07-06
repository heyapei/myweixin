package com.hyp.myweixin.utils.bdmaputil.impl;

import com.alibaba.fastjson.JSONObject;
import com.hyp.myweixin.pojo.dto.bdmap.BDMapResSimpleDTO;
import com.hyp.myweixin.pojo.dto.bdmap.gpstoaddress.BDMapGpsToAddressDTO;
import com.hyp.myweixin.pojo.dto.bdmap.iptogps.BDMapIpToGpsDTO;
import com.hyp.myweixin.utils.MyHttpClientUtil;
import com.hyp.myweixin.utils.bdmaputil.BDMapUtilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/7/6 15:29
 * @Description: TODO
 */
@Service
@Slf4j
@PropertySource("classpath:amap-key.properties")
public class BDMapUtilServiceImpl implements BDMapUtilService {


    @Value("${url-baidu}")
    private String url;
    @Value("${key-baidu}")
    private String key;
    @Autowired
    private MyHttpClientUtil httpClientUtil;


    @Override
    public BDMapResSimpleDTO getBDMapResSimpleDTOByIp(String ip) {

        BDMapIpToGpsDTO ipPositionNoAsync = getIpPositionNoAsync(ip);
        if (ipPositionNoAsync == null) {
            log.error("百度地图没有返回IP对应的经纬度数据,需要转换的信息：{}", ip);
            return null;
        }
        BDMapGpsToAddressDTO geocodeByIpAddressGeneralNoAsync = getGeocodeByIpAddressGeneralNoAsync(
                ipPositionNoAsync.getContent().getPoint().getY() + ","
                        + ipPositionNoAsync.getContent().getPoint().getX());
        if (geocodeByIpAddressGeneralNoAsync == null) {
            log.error("百度地图没有返回经纬度对应的大致地址数据,需要转换的信息：{}", ipPositionNoAsync.toString());
            return null;
        }

        BDMapResSimpleDTO bdMapResSimpleDTO = new BDMapResSimpleDTO();
        bdMapResSimpleDTO.setCity(ipPositionNoAsync.getContent().getAddress_detail().getCity());
        bdMapResSimpleDTO.setProvince(ipPositionNoAsync.getContent().getAddress_detail().getProvince());
        bdMapResSimpleDTO.setLocation(geocodeByIpAddressGeneralNoAsync.getResult().getLocation().getLng() + "," + geocodeByIpAddressGeneralNoAsync.getResult().getLocation().getLat());
        bdMapResSimpleDTO.setGeneralAddress(geocodeByIpAddressGeneralNoAsync.getResult().getFormatted_address());
        bdMapResSimpleDTO.setIp(ip);

        return bdMapResSimpleDTO;
    }

    @Override
    public BDMapGpsToAddressDTO getGeocodeByIpAddressGeneralNoAsync(String location) {
        if (location == null) {
            log.error("通过坐标获取大致的地理位置（街道信息），要求坐标不为空");
            return null;
        }
        String realUrl = url + "/reverse_geocoding/v3/";

        Map<String, String> parameterMap = new HashMap<>(2);
        parameterMap.put("ak", key);
        parameterMap.put("output", "json");
        parameterMap.put("coordtype", "wgs84ll");
        parameterMap.put("location", location);
        String map = httpClientUtil.postMap(realUrl, parameterMap, null);
        if (map == null) {
            log.error("百度地图没有返回经纬度对应的大致地址数据,需要转换的信息：{}", location);
            return null;
        } else {
            BDMapGpsToAddressDTO bdMapGpsToAddressDTO = null;


            try {
                bdMapGpsToAddressDTO = JSONObject.parseObject(map, BDMapGpsToAddressDTO.class);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("百度地图Ip转物理地址失败：" + e.toString());
            }
            return bdMapGpsToAddressDTO;
        }
    }

    @Override
    public BDMapIpToGpsDTO getIpPositionNoAsync(String ipAddress) {
        if (ipAddress == null) {
            log.error("通过IP地址获取位置地理信息参数为空，不合法");
            return null;
        }
        String realUrl = url + "/location/ip?ip=" + ipAddress + "&output=json&ak=" + key + "&coor=bd09ll";
        String map = httpClientUtil.getParameter(realUrl, null, null);
        if (map == null) {
            log.error("百度地图没有返回IP对应的地址数据,需要转换的地址信息：{}", ipAddress);
        } else {
            BDMapIpToGpsDTO bdMapIpToGpsDTO = null;
            try {
                bdMapIpToGpsDTO = JSONObject.parseObject(map, BDMapIpToGpsDTO.class);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("百度地图Ip转物理地址失败：" + e.toString());
            }
            return bdMapIpToGpsDTO;
        }
        return null;
    }
}
