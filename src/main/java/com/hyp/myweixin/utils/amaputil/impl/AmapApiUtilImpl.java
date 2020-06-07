package com.hyp.myweixin.utils.amaputil.impl;

import com.alibaba.fastjson.JSONObject;
import com.hyp.myweixin.pojo.dto.AmapIpToAddressDTO;
import com.hyp.myweixin.utils.MyHttpClientUtil;
import com.hyp.myweixin.utils.amaputil.AmapApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/7 11:27
 * @Description: TODO
 */
@Service
@Slf4j
@PropertySource("classpath:amap-key.properties")
public class AmapApiUtilImpl implements AmapApiUtil {

    @Value("${url}")
    private String url;
    @Value("${key}")
    private String key;
    @Autowired
    private MyHttpClientUtil httpClientUtil;


    /**
     * 通过IP地址获取位置地理信息
     *
     * @param ipAddress
     */
    @Async("threadPoolTaskExecutor")
    @Override
    public AmapIpToAddressDTO getIpPosition(String ipAddress) {
        if (ipAddress == null) {
            log.error("通过IP地址获取位置地理信息参数为空，不合法");
            return null;
        }
        String realUrl = url + "/v3/ip?ip=" + ipAddress + "&output=json&key=" + key;
        String map = httpClientUtil.getParameter(realUrl, null, null);
        if (map == null) {
            log.error("高德地图没有返回IP对应的地址数据,需要转换的地址信息：{}", ipAddress);
        } else {
            AmapIpToAddressDTO amapIpToAddressDTO = null;
            try {
                amapIpToAddressDTO = JSONObject.parseObject(map, AmapIpToAddressDTO.class);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("高德地图Ip转物理地址失败：" + e.toString());
            }
            return amapIpToAddressDTO;
        }
        return null;
    }

    /**
     * 通过经纬度信息 获取大致的地理位置（街道信息）
     *
     * @param location
     */
    @Async("threadPoolTaskExecutor")
    @Override
    public String getGeocodeByLocationGeneral(String location) {
        if (location == null) {
            log.error("请求参数不可以为空");
        } else {
            String realUrl = url + "/v3/geocode/regeo";
            //key:e033227643647e0176dce382d56ff30c
            //location:112.2338927,34.48078048;112.6624238,34.83072271
            Map<String, String> parameterMap = new HashMap<>(2);
            parameterMap.put("key", key);
            parameterMap.put("location", location);
            String map = httpClientUtil.postMap(realUrl, parameterMap, null);
            log.info("返回值：" + map);
            if (map == null) {
                log.error("高德地图没有返回经纬度对应的大致地址数据,需要转换的信息：{}", location);
            } else {
                String generalAddress = null;
                try {
                    JSONObject jsonObject = JSONObject.parseObject(map);
                    JSONObject regeocode = jsonObject.getJSONObject("regeocode");
                    generalAddress = regeocode.getString("formatted_address");
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("高德地图经纬度转物理地址失败：" + e.toString());
                }
                return generalAddress;
            }
        }
        return null;
    }

    /**
     * 通过坐标 获取大致的地理位置（街道信息）
     *
     * @param location
     */
    @Override
    public String getGeocodeByIpAddressGeneralNoAsync(String location) {
        if (location == null) {
            log.error("通过坐标获取大致的地理位置（街道信息），要求坐标不为空");
            return null;
        }
        String realUrl = url + "/v3/geocode/regeo";
        String s = location.replaceAll(";", "|");
        Map<String, String> parameterMap = new HashMap<>(2);
        parameterMap.put("key", key);
        parameterMap.put("location", s);
        String map2 = httpClientUtil.postMap(realUrl, parameterMap, null);
        if (map2 == null) {
            log.error("高德地图没有返回经纬度对应的大致地址数据,需要转换的信息：{}", s);
            return null;
        } else {
            String generalAddress = null;
            try {
                JSONObject jsonObject = JSONObject.parseObject(map2);
                if (jsonObject.containsKey("regeocode")) {
                    JSONObject regeocode = jsonObject.getJSONObject("regeocode");
                    if (regeocode.containsKey("formatted_address")) {
                        generalAddress = regeocode.getString("formatted_address");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("高德地图经纬度转物理地址失败：" + e.toString());
            }
            return generalAddress;
        }
    }


    @Override
    public AmapIpToAddressDTO getIpPositionNoAsync(String ipAddress) {
        if (ipAddress == null) {
            log.error("通过IP地址获取位置地理信息参数为空，不合法");
            return null;
        }
        String realUrl = url + "/v3/ip?ip=" + ipAddress + "&output=json&key=" + key;
        String map = httpClientUtil.getParameter(realUrl, null, null);
        if (map == null) {
            log.error("高德地图没有返回IP对应的地址数据,需要转换的地址信息：{}", ipAddress);
        } else {
            AmapIpToAddressDTO amapIpToAddressDTO = null;
            try {
                amapIpToAddressDTO = JSONObject.parseObject(map, AmapIpToAddressDTO.class);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("高德地图Ip转物理地址失败：" + e.toString());
            }
            return amapIpToAddressDTO;
        }
        return null;
    }


}
