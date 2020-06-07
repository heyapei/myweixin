package com.hyp.myweixin.config.weixin;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 微信小程序的配置
 * @author heyapei
 */
@Getter
@Component
@PropertySource("classpath:wechat.properties")
public class WeChatPropertiesValue {

    @Value("${appid}")
    private String appid;

    @Value("${appsecret}")
    private String appSecret;
}
