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

    @Value("${qutoupiao.appid}")
    private String quTouPiaoAppId;

    @Value("${qutoupiao.appsecret}")
    private String quTouPiaoAppSecret;


    @Value("${qubaoming.appid}")
    private String quBaoMingAppId;

    @Value("${qubaoming.appsecret}")
    private String quBaoMingAppSecret;




}
