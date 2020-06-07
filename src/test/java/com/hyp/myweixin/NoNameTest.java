package com.hyp.myweixin;

import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.utils.MyHttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/7 18:04
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class NoNameTest {


    @Autowired
    private MyHttpClientUtil myHttpClientUtil;

    @Test
    public void contextLoads() {

        WeixinVoteBase weixinVoteBase = new WeixinVoteBase();
        System.out.println("查看是否可以有默认值："+weixinVoteBase.toString());

        String code = "071XkLoW0ChBy02B5RlW0x2qoW0XkLoj";
        String appId = "wx09609fe79142649a";
        String secret = "9eb2c3ea68055398afb0280fbbcd6ebe";
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";

        String parameter = myHttpClientUtil.getParameter(url, null, null);
        System.out.println("换取的结果：" + parameter);
    }


}
