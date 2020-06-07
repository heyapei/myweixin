package com.hyp.myweixin.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/7 0:26
 * @Description: TODO
 */
public interface MyIpMacUtil {


    /**
     * 获取移动设备的类型
     *
     * @param userAgent
     * @return
     */
    String getMobileType(String userAgent);

    /**
     * 获取移动设备的名称
     *
     * @param userAgent
     * @return
     */
    String getMobileName(String userAgent);

    /**
     * 获取IP地址
     *
     * @param request
     * @return
     */
    String getRealIP(HttpServletRequest request);

    /**
     * 判断是否为CDN网络来源
     *
     * @param request
     * @return
     */
    boolean isComeFromCDN(HttpServletRequest request);

    /**
     * 判断是否为内网IP
     *
     * @param ipAddress
     * @return
     */
    boolean isInnerIP(String ipAddress);

    /**
     * ip地址转long类型数据
     *
     * @param ipAddress
     * @return
     */
    long ipToLong(String ipAddress);

    /**
     * long类型转IP
     *
     * @param longIp
     * @return
     */
    String longToIP(long longIp);


}
