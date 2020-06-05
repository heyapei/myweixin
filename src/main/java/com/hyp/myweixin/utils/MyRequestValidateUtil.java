package com.hyp.myweixin.utils;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/5 19:34
 * @Description: TODO
 */
@Slf4j
public class MyRequestValidateUtil {


    SecretKeyPropertiesValue secretKeyPropertiesValue = new SecretKeyPropertiesValue();

    /**
     * 一个简单的签名认证，规则：
     * 1. 将请求参数按ascii码排序
     * 2. 拼接为a=value&b=value...这样的字符串（不包含sign）
     * 3. 混合密钥（secret）进行md5获得签名，与请求的签名进行比较
     */
    public boolean validateSign(HttpServletRequest request) {
        //获得请求签名，如sign=19e907700db7ad91318424a97c54ed57
        String requestSign = request.getParameter("sign");
        if (StringUtils.isEmpty(requestSign)) {
            return false;
        }
        List<String> keys = new ArrayList<String>(request.getParameterMap().keySet());
        //排除sign参数
        keys.remove("sign");
        //排序
        Collections.sort(keys);

        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            //拼接字符串
            sb.append(key).append("=").append(request.getParameter(key)).append("&");
        }
        String linkString = sb.toString();
        //去除最后一个'&'
        linkString = StringUtils.substring(linkString, 0, linkString.length() - 1);
        //混合密钥md5
        String sign = DigestUtils.md5Hex(linkString + secretKeyPropertiesValue.getMd5Key());
        log.info("测试阶段需要知道原始字符串："+linkString + secretKeyPropertiesValue.getMd5Key());
        log.info("加密后："+sign+"==请求过来的数据："+requestSign);
        //比较
        return StringUtils.equals(sign, requestSign);
    }

}
