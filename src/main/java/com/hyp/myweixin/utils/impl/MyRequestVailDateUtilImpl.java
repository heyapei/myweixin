package com.hyp.myweixin.utils.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import com.hyp.myweixin.utils.dateutil.MyDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/6 17:06
 * @Description: TODO
 */
@Slf4j
@Service
public class MyRequestVailDateUtilImpl implements MyRequestVailDateUtil {

    @Override
    public boolean validateSignMd5Date(HttpServletRequest request, String signKey, int minutes) {

        long requestNowTime = 0;
        String nowTime = request.getParameter("nowTime");
        if (StringUtils.isEmpty(nowTime)) {
            throw new MyDefinitionException("请求缺少时间戳数据");
        } else {
            if (nowTime.length() < 13) {
                return false;
            } else {
                boolean parsable = NumberUtils.isParsable(nowTime);
                if (parsable) {
                    try {
                        requestNowTime = Long.parseLong(nowTime);
                    } catch (NumberFormatException e) {
                        throw new MyDefinitionException("请求参数时间戳错误");
                    }
                } else {
                    return false;
                }
            }
        }

        float distanceTimeByType = MyDateUtil.getDistanceTimeByType(requestNowTime, System.currentTimeMillis(), 1);
        //log.info("请求过来的参数：" + requestNowTime + "本地时间：" + System.currentTimeMillis() + "数据差值：" + distanceTimeByType);
        if (distanceTimeByType > minutes) {
            throw new MyDefinitionException("不允许频繁发送相同请求");
        }

        boolean b = validateSignMd5(request, signKey);
        return b;
    }

    /**
     * 一个简单的签名认证，规则：
     * 1. 将请求参数按ascii码排序
     * 2. 拼接为a=value&b=value...这样的字符串（不包含sign）
     * 3. 混合密钥（secret）进行md5获得签名，与请求的签名进行比较
     *
     * @param request 请求的参数
     * @param signKey 参与加密的key值
     * @return 验证通过返回true，失败返回false
     */
    @Override
    public boolean validateSignMd5(HttpServletRequest request, String signKey) {
        //获得请求签名，如sign=19e907700db7ad91318424a97c54ed57
        String requestSign = request.getParameter("sign");
        if (StringUtils.isEmpty(requestSign)) {
            throw new MyDefinitionException("请求缺少密钥数据");
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
        String sign = DigestUtils.md5Hex(linkString + signKey);
        log.info("测试阶段需要知道原始字符串：" + linkString + signKey);
        log.info("加密后：" + sign + "==请求过来的数据：" + requestSign);
        //比较
        return StringUtils.equals(sign, requestSign);
    }
}
