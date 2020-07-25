package com.hyp.myweixin.service.smallwechatapi.impl;

import com.alibaba.fastjson.JSONObject;
import com.hyp.myweixin.config.weixin.WeChatPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.service.smallwechatapi.WeixinSmallContentDetectionApiService;
import com.hyp.myweixin.utils.MyHttpClientUtil;
import com.hyp.myweixin.utils.redis.MyRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/25 19:54
 * @Description: TODO
 */
@Slf4j
@Service
@PropertySource("classpath:wechat.properties")
public class WeixinSmallContentDetectionApiServiceImpl implements WeixinSmallContentDetectionApiService {


    /**
     * 获取token用url
     */
    @Value("${weixin.small.access.token.url}")
    private String GET_ACCESS_TOKEN_URL;

    /**
     * 将获取回来的token存入redis，
     */
    @Value("${weixin.small.access.token.redis.key}")
    private String ACCESS_TOKEN_REDIS_KEY;
    /**
     * 过期时间设置为6000s
     */
    @Value("${weixin.small.access.token.redis.expires.time}")
    private Long ACCESS_TOKEN_REDIS_EXPIRE;

    /**
     * 文字检测链接
     */
    @Value("${weixin.small.check.msg_sec_check.url}")
    private String MSG_SEC_CHECK_URL;


    @Autowired
    private WeChatPropertiesValue weChatPropertiesValue;

    @Autowired
    private MyHttpClientUtil myHttpClientUtil;
    @Autowired
    private MyRedisUtil myRedisUtil;


    /**
     * 获取小程序全局唯一后台接口调用凭据（access_token）
     * 调用绝大多数后台接口时都需使用 access_token，开发者需要进行妥善保存
     * https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
     *
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public JSONObject getAccessToken() throws MyDefinitionException {

        Object o = myRedisUtil.get(ACCESS_TOKEN_REDIS_KEY);
        if (o != null) {
            JSONObject jsonObject = null;
            String redisValue = String.valueOf(o);
            try {
                jsonObject = JSONObject.parseObject(redisValue);
            } catch (Exception e) {
                log.error("从redis获取微信token转换数据失败，错误原因：{}", e.toString());
                throw new MyDefinitionException("从redis获取微信token转换数据失败");
            }
            return jsonObject;
        }

        Map<String, Object> parameterMap = new HashMap<>(3);
        parameterMap.put("grant_type", "client_credential");
        parameterMap.put("appid", weChatPropertiesValue.getAppid());
        parameterMap.put("secret", weChatPropertiesValue.getAppSecret());
        String resultAccessToken = null;
        try {
            resultAccessToken = myHttpClientUtil.getParameter(GET_ACCESS_TOKEN_URL, parameterMap, null);
        } catch (Exception e) {
            log.error("获取微信token请求失败，失败原因：{}", e.toString());
            throw new MyDefinitionException("获取微信token请求失败");
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(resultAccessToken);
        } catch (Exception e) {
            log.error("获取微信token转换数据失败，错误原因：{}", e.toString());
            throw new MyDefinitionException("获取微信token转换数据失败");
        }

        if (jsonObject == null) {
            throw new MyDefinitionException("获取微信token失败");
        } else {
            if (jsonObject.containsKey("access_token")) {
                myRedisUtil.set(ACCESS_TOKEN_REDIS_KEY, jsonObject, ACCESS_TOKEN_REDIS_EXPIRE);
            } else {
                throw new MyDefinitionException("从微信系统中拉取的数据中并没有包含token数据，而是" + resultAccessToken + "" +
                        "，该数据不符合要求");
            }
        }
        return jsonObject;
    }
}
