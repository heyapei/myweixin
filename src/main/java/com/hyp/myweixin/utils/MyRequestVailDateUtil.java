package com.hyp.myweixin.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/6 17:03
 * @Description: TODO
 */
public interface MyRequestVailDateUtil {


    /**
     * 和validateSignMd5逻辑相同 但是请求参数中要有nowTime值，这个值要求是timestamp
     *
     * @param request
     * @param signKey
     * @param minutes 允许请求间隔分钟数
     * @return
     */
    boolean validateSignMd5Date(HttpServletRequest request, String signKey, int minutes);


    /**
     * 一个简单的签名认证，规则：
     * 1. 将请求参数按ascii码排序
     * 2. 拼接为a=value&b=value...这样的字符串（不包含sign）
     * 3. 混合密钥（secret）进行md5获得签名，与请求的签名进行比较
     *
     * @param request 请求的参数
     * @param key     参与加密的key值
     * @return 验证通过返回true，失败返回false
     */
    boolean validateSignMd5(HttpServletRequest request, String key);

}
