package com.hyp.myweixin.config.secretkey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 请求加密的密码
 *
 * @author heyapei
 */


@Component
@PropertySource("classpath:secret-key.properties")
public class SecretKeyPropertiesValue {

    /**
     * md5验证的密码
     */
    private static String md5key;
    public String getMd5Key() {
        return md5key;
    }
    @Value("${md5.key}")
    private void setMd5Key(String value) {
        md5key = value;
    }

}